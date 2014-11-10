package controller;

import java.io.IOException;
import java.nio.file.Paths;

import dto.Table;
import persistence.DatabaseConnection;
import persistence.WatchDir;

public class Controller {
	
	private Thread watchDir;
	
	public void startMonitoringHotspot(String hotspot, String processing, String history) {
		try {
			watchDir = new Thread(new WatchDir(hotspot, processing, history));
			watchDir.setPriority(Thread.MIN_PRIORITY);
			watchDir.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Table getTable() {
		return DatabaseConnection.getInstance().getBatch();
	}
}
