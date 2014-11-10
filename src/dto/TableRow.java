package dto;

import java.util.Date;

public class TableRow {
	
	private int transactionID;
	private Date transactionDate;
	private double amount;
	private String description;
	private Date startDate;
	private Date endDate;
	private String filename;
	
	public TableRow(int transactionID, Date transactionDate, double amount, String description, Date startDate, Date endDate, String filename) {
		this.transactionID = transactionID;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.filename = filename;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String asHTMLTableRow() {
		String s = "<tr>"
				+ "		<td>" + transactionID + "</td>"
				+ "		<td>" + transactionDate + "</td>"
				+ "		<td>" + amount + "</td>"
				+ "		<td>" + description + "</td>"
				+ "		<td>" + startDate + "</td>"
				+ "		<td>" + endDate + "</td>"
				+ "		<td>" + filename + "</td>"
				+ "</tr>";
		return s;
	}
	

}
