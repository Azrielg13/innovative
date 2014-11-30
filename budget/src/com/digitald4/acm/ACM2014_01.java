package com.digitald4.acm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ACM2014_01 {
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new FileReader("src/com/digitald4/acm/ACM2014_01.in"));
		String headers = stdin.readLine();
		List<Column> columns = new ArrayList<Column>();
		for (String colName : headers.split(",")) {
			columns.add(new Column(colName));
		}
		String newCols = stdin.readLine();
		int c = 0;
		for (String newCol : newCols.split(",")) {
			columns.get(c++).newIndex = Integer.parseInt(newCol);
		}
		// Throw away the 3rd line
		stdin.readLine();
		String line;
		while ((line = stdin.readLine()) != null) {
			c = 0;
			for (String data : line.split(",")) {
				columns.get(c++).data.add(data);
			}
		}
		/*for (Column col : columns) {
			System.out.println("col \"" + col + "\"");
		}*/
		TreeSet<Column> result = new TreeSet<Column>();
		for (Column col : columns) {
			if (col.newIndex > 0) {
				result.add(col);
			}
		}
		String out = "";
		for (Column col : result) {
			if (out.length() > 0) {
				out += ",";
			}
			out += col.name;
		}
		System.out.println(out);
		for (int x = 0; x < result.first().data.size(); x++) {
			out = "";
			for (Column col : result) {
				if (out.length() > 0) {
					out += ",";
				}
				out += col.data.get(x);
			}
			System.out.println(out);
		}
		stdin.close();
	}
	
	private static class Column implements Comparable<Column> {
		public String name;
		public int newIndex;
		public List<String> data = new ArrayList<String>();
		
		private Column(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name + data;
		}

		@Override
		public int compareTo(Column column) {
			if (newIndex <= column.newIndex) {
				return -1;
			}
			if (newIndex > column.newIndex) {
				return 1;
			}
			return 0;
		}
	}
}
