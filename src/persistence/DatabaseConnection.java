package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import dto.Table;
import dto.TableRow;

public class DatabaseConnection {

	private BasicDataSource datasource;
	private Connection connection;
	
	private static final DatabaseConnection INSTANCE = new DatabaseConnection();
	private DatabaseConnection() {
		Context envCtx;
		try {
			Context initCtx = new InitialContext();
			envCtx = (Context) initCtx.lookup("java:comp/env");
			datasource = (BasicDataSource) envCtx
					.lookup("jdbc/TransactionsDB");
			connection = datasource.getConnection();;
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static DatabaseConnection getInstance() {
	    return INSTANCE;
	}

	public long createBatch(String filename) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn
					.prepareStatement("INSERT INTO Batch(Filename, StartDate) "
							+ "			VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, filename);
			statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
	        int affectedRows = statement.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating user failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	long id = generatedKeys.getLong(1);
	                return id;
	            } else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void updateBatch(int totalNumberOfRows, int numberOfNewTrans, long batchPK) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn.prepareStatement(
					"UPDATE Batch "
				  + "SET TotalNumberOfRows = ?, NumberOfNewTrans = ?, EndDate = ? "
				  + "WHERE BatchPK = ? ");
			statement.setInt(1, totalNumberOfRows);
			statement.setInt(2, numberOfNewTrans);
			statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			statement.setLong(4, batchPK);
			statement.executeUpdate();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int createTransaction(int transactionID, java.util.Date transactionDate, double amount, long transactionDescriptionPK, long batchPK) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn
					.prepareStatement("INSERT INTO Transaction(TransactionID, TransactionDate, Amount, TransactionDescriptionPK, BatchPK) VALUES(?, ?, ?, ?, ?)");
			statement.setInt(1, transactionID);
			statement.setDate(2, new Date(transactionDate.getTime()));
			statement.setDouble(3, amount);
			statement.setLong(4, transactionDescriptionPK);
			statement.setLong(5, batchPK);
			statement.executeUpdate();
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public long createTransactionDescription(String description, long batchPK) {
		try {
			Connection conn = getConnection();
			PreparedStatement statement = conn
					.prepareStatement("INSERT INTO TransactionDescription(Description, BatchPK) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, description);
			statement.setLong(2, batchPK);
			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating user failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	            	long id = generatedKeys.getLong(1);
	                return id;
	            } else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public Table getBatch() {
		Table table = new Table();
		try {
			Connection conn = getConnection();
			Statement statement = conn.createStatement();
			ResultSet rs = statement
					.executeQuery("SELECT DISTINCT Transaction.TransactionId, Transaction.TransactionDate, "
							+ "			Transaction.Amount, TransactionDescription.Description, "
							+ "			Batch.StartDate, Batch.EndDate, Batch.Filename "
							+ "   FROM Batch, TransactionDescription, Transaction "
							+ "	  WHERE Batch.BatchPK = Transaction.BatchPK "
							+ "   AND Batch.BatchPK = TransactionDescription.BatchPK "
							+ "   AND TransactionDescription.TransactionDescriptionPK = Transaction.TransactionDescriptionPK");
			while (rs.next()) {
				TableRow tr = new TableRow(rs.getInt(1), rs.getDate(2),
						rs.getDouble(3), rs.getString(4), rs.getDate(5),
						rs.getDate(6), rs.getString(7));
				table.addRow(tr);
			}
			statement.close();
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return table;
	}

	private Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed())
			return datasource.getConnection();
		return connection;
	}

}
