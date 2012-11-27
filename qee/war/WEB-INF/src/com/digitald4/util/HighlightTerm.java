package com.digitald4.util;

// Eddie need your help
// Need to pass in vector of strings for tokens
// Might be a better way to ouput than term.highlightTerm(text, term) like client.highlightTerm(tokens[]) ????
// need to ignore case

import java.util.Vector;

public class HighlightTerm{

	public static String highlightTerms(final String text, final Vector<String> terms){
		String result=text;
		for(String term:terms)
			result = highlightTerm(result,term);
		return result;
	}
	public static String highlightTerm(final String text, final String term){
		String startTag = "<span style=\"background-color: #FFFF00\">";
		String endTag = "</span>";
		if (!term.equals("")){
			String hResult = startTag+term+endTag;
			//System.out.println("---->" + text + "  " + term);
			return text.replace(term, hResult);
		} else {
			return text;
		}
	}
}
