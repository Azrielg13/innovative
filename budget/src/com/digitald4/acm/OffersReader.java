package com.digitald4.acm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

public class OffersReader {
	
	private final static String TEMPLATE = "\"OT_VIDEO\",        \"%COUNTRY_CODE%\",\"BASIC\",  \"120807\",\"143900\",   \"%CURRENCY_SYMBOL%\",    \"\",        \"%SPEND_X%\",   \"%GET_Y%\",   \"%CURRENCY_CODE%\",\"91\",\"09/01/2015\"";

	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new FileReader("src/com/digitald4/acm/offers.csv"));
		stdin.readLine(); // Ignore headers
		String line = null;
		TreeSet<Offer> offers = new TreeSet<Offer>();
		while ((line = stdin.readLine()) != null) {
			String[] data = line.split(",");
			if (data.length > 0) {
				Offer offer = new Offer();
				offer.setCountryCode(data[3]);
				offer.setSpendX(data[4]);
				offer.setGetY(data[5]);
				offer.setCurrencyCode(data[6]);
				offers.add(offer);
			}
		}
		for (Offer offer : offers) {
			System.out.println(offer);
		}
		stdin.close();
	}
	
	private static class Offer implements Comparable<Offer>{
		private String countryCode;
		private String currencySymbol;
		private String spendX;
		private String getY;
		private String currencyCode;
		
		public Offer setCountryCode(String countryCode) {
			this.countryCode = countryCode;
			return this;
		}
		
		public Offer setSpendX(String spendX) {
			if (spendX.contains(" ")) {
				this.currencySymbol = spendX.substring(0, spendX.indexOf(" "));
				this.spendX = spendX.substring(spendX.indexOf(" ") + 1);
			} else {
				this.currencySymbol = "$";
				this.spendX = spendX;
			}
			return this;
		}
		
		public Offer setGetY(String getY) {
			this.getY = getY.substring(getY.indexOf(" ") + 1);
			return this;
		}
		
		public Offer setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode.substring(0, 3);
			return this;
		}

		@Override
		public int compareTo(Offer offer) {
			return countryCode.compareTo(offer.countryCode);
		}
		
		public String toString() {
			String line1 = TEMPLATE.replace("%COUNTRY_CODE%", countryCode)
					.replace("%CURRENCY_SYMBOL%", currencySymbol).replace("%SPEND_X%", spendX)
					.replace("%GET_Y%", getY).replace("%CURRENCY_CODE%", currencyCode);
			String line2 = line1.replaceAll("\"BASIC\",  ", "\"PARTNER\",");
			return line1 + "\n" + line2;
		}
	}
}
