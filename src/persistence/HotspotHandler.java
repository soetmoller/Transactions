package persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotspotHandler {

	private String hotspot, history, processing;

	public HotspotHandler(String hotspot, String processing, String history) {
		this.hotspot = hotspot;
		this.processing = processing;
		this.history = history;
	}

	void readCVSFile(String name) throws IOException, ClassNotFoundException {
		BufferedReader br = null;
		DatabaseConnection databaseConnection = DatabaseConnection
				.getInstance();
		File file = new File(hotspot + name);
		moveFile(file, processing);
		DateFormat formatter = new SimpleDateFormat("yy/MM/dd");
		try {
			br = new BufferedReader(new FileReader(processing + name));
			int count = 0;
			long batchPK = databaseConnection.createBatch(file.getName());
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("I"))
					continue; // Ignores first row with column headers;
				count++;
				String[] transaction = line.split(",");
				int transactionID = Integer.parseInt(transaction[0]);
				Date transactionDate = formatter.parse(transaction[1]);
				String description = transaction[2];
				double amount = Double.parseDouble(transaction[3]);
				long transactionDescriptionPK = databaseConnection
						.createTransactionDescription(description, batchPK);
				databaseConnection.createTransaction(transactionID,
						transactionDate, amount, transactionDescriptionPK,
						batchPK);
				line = br.readLine();
			}
			databaseConnection.updateBatch(count, count, batchPK);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		moveFile(new File(processing + name), history);
		System.out.println("Done");
	}

	private void moveFile(File file, String toDir) {
		InputStream inStream = null;
		OutputStream outStream = null;
		try {
			File copyFile = new File(toDir + file.getName());
			inStream = new FileInputStream(file);
			outStream = new FileOutputStream(copyFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}
			inStream.close();
			outStream.close();
			System.out.println("File moved from " + file.getParent() + " to "
					+ copyFile.getParent());
			// delete the original file
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
