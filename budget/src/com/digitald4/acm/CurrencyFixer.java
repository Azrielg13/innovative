package com.digitald4.acm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.digitald4.common.util.Pair;

public class CurrencyFixer {
	
	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new FileReader("src/com/digitald4/acm/offersv2.csv"));
		FileWriter fos = new FileWriter("src/com/digitald4/acm/offers.out");
		String line = null;
		Map<String, Pair<String, String>> map = new HashMap<String, Pair<String, String>>();
		while ((line = stdin.readLine()) != null) {
			line = new String(line.getBytes(), "UTF8");
			if (line.length() > 0 && !line.startsWith("#")) {
				String[] data = line.split(",");
				if (data.length > 0) {
					String coCode = data[1].trim();
					Pair<String, String> symbols = map.get(coCode);
					if (symbols == null) {
						symbols = new Pair<String, String>(data[5], data[6]);
						map.put(coCode, symbols);
					}
					data[5] = symbols.getLeft();
					data[6] = symbols.getRight();
					String out = "";
					for (String d : data) {
						if (out.length() > 0) {
							out += ",";
						}
						out += d;
					}
					System.out.println(out);
					fos.write(out + "\n");
				}
			} else {
				System.out.println(line);
				fos.write(line + "\n");
			}
		}
		stdin.close();
		fos.close();
	}
}
