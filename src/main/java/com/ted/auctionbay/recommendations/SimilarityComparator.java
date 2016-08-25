package com.ted.auctionbay.recommendations;

import java.util.Comparator;

import org.json.JSONException;
import org.json.JSONObject;

public class SimilarityComparator implements Comparator<JSONObject>{

	@Override
	public int compare(JSONObject o1, JSONObject o2) {
			
		try {
			double s1 = o1.getDouble("similarity");
			double s2 = o2.getDouble("similarity");
			if( s1 < s2 ) return 1;
			if( s1 == s2 )return 0;
			return -1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
}
