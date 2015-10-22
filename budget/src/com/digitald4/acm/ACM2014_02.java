package com.digitald4.acm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ACM2014_02 {
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new FileReader("src/com/digitald4/acm/ACM2014_02.in"));
		String data = "";
		String line = "";
		while ((line = stdin.readLine()) != null) {
			if (line.length() > 0) {
				data += " ";
			}
			data += line;
		}
		List<Integer> values = new ArrayList<Integer>();
		for (String d : data.split(" ")) {
			values.add(Integer.parseInt(d));
		}
		for (int c = 0; c < values.size(); c++) {
			int count = values.get(c);
			Competitor comp = new Competitor();
			for (int x = 0; x < count * 2; x++) {
				comp.judge1Score = values.get(c++);
				comp.judge2Score = values.get(c++);
			}
		}
		System.out.println(data);
		stdin.close();
	}
	
	private static class Competitor {
		public int judge1Score;
		public int judge2Score;
		
		private Competitor() {
		}
	}
	
	@SuppressWarnings("unused")
	private static class JudgeRank implements Comparable<JudgeRank> {
		private Competitor comp;
		private int judge;
		private JudgeRank(Competitor comp, int judge) {
			this.comp = comp;
			this.judge = judge;
		}
		
		public int compareTo(JudgeRank jr) {
			int score = judge == 1 ? comp.judge1Score : comp.judge2Score;
			int cmpScore = judge == 1 ? jr.comp.judge1Score : jr.comp.judge2Score;
			if (score < cmpScore) {
				return -1;
			}
			if (score > cmpScore) {
				return 1;
			}
			return 0;
		}
	}
}
