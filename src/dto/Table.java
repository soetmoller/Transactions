package dto;

import java.util.ArrayList;
import java.util.List;

public class Table {
	
	private List<TableRow> rows;
	
	public Table(List<TableRow> rows) {
		rows.addAll(rows);
	}
	
	public Table() {
		rows = new ArrayList<TableRow>();
	}
	
	public void addRow(TableRow tr) {
		rows.add(tr);
	}
	
	public List<TableRow> getTableRows() {
		return rows;
	}

}
