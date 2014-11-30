package com.digitald4.acm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class HomeValue {

	public static void main(String[] args) throws IOException {
		BufferedReader stdin = new BufferedReader(new FileReader("src/com/digitald4/acm/RecentHomeSales.txt"));
		String address;
		int avgPriceSqft = 0;
		TreeSet<RecentSale> sales = new TreeSet<RecentSale>();
		while ((address = stdin.readLine()) != null) {
			RecentSale rs = new RecentSale();
			rs.parseAddress(address);
			rs.parseSellsPrice(stdin.readLine());
			rs.parseSellDate(stdin.readLine());
			rs.parseZestimate(stdin.readLine());
			stdin.readLine();
			rs.parseAddress(stdin.readLine());
			rs.parseDetails(stdin.readLine());
			rs.parseLotSize(stdin.readLine());
			rs.parseYearBuilt(stdin.readLine());
			avgPriceSqft += rs.getPriceSqft();
			sales.add(rs);
		}
		stdin.close();
		for (RecentSale rs : sales) {
			System.out.println(rs);
		}
		avgPriceSqft /= sales.size();
		int medianPriceSqft = new ArrayList<RecentSale>(sales).get(sales.size() / 2).getPriceSqft();
		System.out.println("AVERAGE: $" + avgPriceSqft + ", $" + (avgPriceSqft * 4488));
		System.out.println("MEDIAN: $" + medianPriceSqft + ", $" + (medianPriceSqft * 4488));
	}
	
	private static class RecentSale implements Comparable<RecentSale> {
		public String address;
		public int sellsPrice;
		public String sellDate;
		public int zestimate;
		public int beds;
		public int baths;
		public int sqft;
		public double lotSize;
		public int yearBuilt;
		
		public RecentSale parseAddress(String address) {
			this.address = address;
			return this;
		}
		
		public RecentSale parseSellsPrice(String sellsPrice) {
			if (sellsPrice.endsWith("M")) {
				double value = Double.parseDouble(sellsPrice.substring(sellsPrice.indexOf('$') + 1, sellsPrice.length() - 1));
				value *= 1000000;
				this.sellsPrice = (int)value;
			} else {
				this.sellsPrice = Integer.parseInt(sellsPrice.substring(sellsPrice.indexOf('$') + 1).replaceAll(",", ""));
			}
			return this;
		}
		
		public RecentSale parseSellDate(String sellDate) {
			this.sellDate = sellDate.substring(sellDate.lastIndexOf(' ') + 1);
			return this;
		}
		
		public RecentSale parseZestimate(String zestimate) {
			if (zestimate.endsWith("M")) {
				double value = Double.parseDouble(zestimate.substring(zestimate.indexOf('$') + 1, zestimate.length() - 1));
				value *= 1000000;
				this.zestimate = (int)value;
			}
			else {
				this.zestimate = Integer.parseInt(zestimate.substring(zestimate.indexOf('$') + 1)
						.replaceAll("K", "000"));
			}
			return this;
		}
		
		public RecentSale parseDetails(String details) {
			this.beds = Integer.parseInt(details.substring(0, details.indexOf(' ')));
			this.baths = Integer.parseInt(details.substring(details.indexOf(',') + 2, details.indexOf(',') + 3));
			this.sqft = Integer.parseInt(details.substring(details.indexOf("baths,") + 7, details.lastIndexOf(' ')).replaceAll(",", ""));
			return this;
		}
		
		public RecentSale parseLotSize(String lotSize) {
			this.lotSize = Double.parseDouble(lotSize.substring(0, lotSize.indexOf(' ')));
			return this;
		}
		
		public RecentSale parseYearBuilt(String yearBuilt) {
			this.yearBuilt = Integer.parseInt(yearBuilt.substring(yearBuilt.lastIndexOf(' ') + 1));
			return this;
		}
		
		public int getPriceSqft() {
			return (int)Math.round((double)sellsPrice / sqft);
		}
		
		public String toString() {
			return sellDate + ", $" + sellsPrice + ", " + beds + ", " + baths + ", " 
					+ sqft + ", " + lotSize + ", " + address + ", $" + getPriceSqft();
		}

		@Override
		public int compareTo(RecentSale rs) {
			if (getPriceSqft() != rs.getPriceSqft()) {
				return getPriceSqft() > rs.getPriceSqft() ? -1 : 1;
			}
			return sellDate.compareTo(rs.sellDate) * -1;
		}
	}
}
