package com.ted.auctionbay.recommendations;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.type.YesNoType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.ted.auctionbay.dao.QueryAuction;
import com.ted.auctionbay.entities.auctions.Auction;
import com.ted.auctionbay.entities.users.RegistereduserBidsinAuction;

//@Service
@Component("recommendation")
public class RecommendationService{
	
	@Autowired
	private QueryAuction queryAuction;

	private static float[][] similarityMatrix;
	private static int N;
	private static Map<String,Set<Integer>> auctions;
	private static Map<Integer,String> integerToUsernameMap;
	private static Map<String,Integer> usernameToIntegerMap;
	private static Map<String,Set<Integer>> recommendationMap;
	private final static int MAX_NEIGHBORS = 10;
	
	private static boolean INITIALIZED = false;
	
	public void initRec(){
		
	}
	

	public void start(){
		System.out.println("Scheduler called me");
		auctions = getAuctionsPerUser();
		N = auctions.size();
		similarityMatrix = new float[N][N];
		integerToUsernameMap = createIntegerToUsernameMap();
		usernameToIntegerMap = createUsernameToIntegerMap(integerToUsernameMap);
		recommendationMap = new HashMap<String,Set<Integer>>();
		
		/*for (Integer key : integerToUsernameMap.keySet()) {
		    System.out.println(key + " " + integerToUsernameMap.get(key));
		}*/
		
		int j=0,i=0;
		for( i = 0; i < N; i++){
			for(j = 0; j < N; j++){
				similarityMatrix[i][j] = computeSimilarity(i,j);
				/*if(similarityMatrix[i][j] > 0){
					System.out.println(similarityMatrix[i][j]);
				}*/
			}
		}
		System.out.println("INITIALIZED NOW");
		INITIALIZED = true;
		//System.out.println("auctions:"+auctions);
		
	}
	
	private HashMap<String, Set<Integer>> getAuctionsPerUser() {
		
		if(queryAuction == null){
			System.out.println("Getrunken");
		}
		List<RegistereduserBidsinAuction> aucOfUsers = queryAuction.getAuctionsOfAllUsers();
		if(aucOfUsers == null){
			System.out.println("aucOfUsers == null");
		}
		System.out.println("SIZE OF REC USERS: " + aucOfUsers.size());
		HashMap<String,Set<Integer>> map = new HashMap<String,Set<Integer>>();
		for(RegistereduserBidsinAuction rec : aucOfUsers){
			Set<Integer> set = new HashSet<Integer>();
			if(map.containsKey(rec.getId().getBidder_Username())){
				Set<Integer> s = map.get(rec.getId().getBidder_Username());
				set.addAll(s);
			}
			set.add(rec.getId().getAuctionID());
			
			map.put(rec.getId().getBidder_Username(), set);
		}
		return map;
	}
	
	private TreeMap<Integer, String> createIntegerToUsernameMap() {
		
		TreeMap<Integer,String> map = new TreeMap<Integer,String>();
		Iterator<String> it = auctions.keySet().iterator();
		int k = 0;
		while(it.hasNext()){
			String username = it.next();
			map.put(k,username );
			/*if(username == null){
				System.out.println(k);
				System.exit(0);
			}*/
			k++;
		}
		
		return map;
	}
	
	private Map<String, Integer> createUsernameToIntegerMap(Map<Integer, String> integerToUsernameMap) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		Iterator<Entry<Integer, String>> it = integerToUsernameMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer, String> e = it.next();
			map.put(e.getValue(), e.getKey());
		}
		return map;
	}
	
	private static float computeSimilarity(int i, int j) {
		
		
		String username_i = integerToUsernameMap.get(i);
		String username_j = integerToUsernameMap.get(j);
		
		Set<Integer> auctions_i = auctions.get(username_i);
		Set<Integer> auctions_j = auctions.get(username_j);
		
		/*if(auctions_i == null || auctions_j == null){
			System.out.println("i = "+i);
			System.out.println("j = "+j);
			System.out.println("i = "+auctions_i+"/"+username_i);
			System.out.println("j = "+auctions_j+"/"+username_j);
			System.exit(0);
		}*/
		
		
		
		return Sets.intersection(auctions_i, auctions_j).size();
	}

	public Set<Integer> getRecommendationForUser(String username){
		
		System.out.println("Initialized value: " + INITIALIZED);
		if(!INITIALIZED) {
			System.out.println("NOT INITIALIZED YET");
			return null;
		}
		
		/*System.out.println("USERNAME TO INTEGER MAP PRINT");
		System.out.println("*****************************");
		System.out.println("");
		for (String key : usernameToIntegerMap.keySet()) {
		    System.out.println(key + " " + usernameToIntegerMap.get(key));
		}*/
		System.out.println("");
		
		Integer i = usernameToIntegerMap.get(username);
		
		if(i == null){
			System.out.println("i is null");
			return null;
		}
			
		
		
		JSONObject[] array = new JSONObject[N];
		Set<Integer> recSet = new TreeSet<Integer>();
		
		for(int j = 0; j < N; j++)
			try {
				array[j] = new JSONObject().put("user", j).put("similarity", similarityMatrix[i][j]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		Comparator<JSONObject> comparator = new SimilarityComparator();
		Arrays.sort(array, comparator);
		
		//String username_i = integerToUsernameMap.get(i);
		
		
		Set<Integer> auctions_i = auctions.get(username);
		
		for(int j = 0; j < MAX_NEIGHBORS; j++){
			try {
				if( array[j].getDouble("similarity") > 0 ){
					String username_j;
					username_j = integerToUsernameMap.get(array[j].get("user"));
					Set<Integer> auctions_j = auctions.get(username_j);
					//System.out.println(auctions_j);
					recSet.addAll(Sets.difference(auctions_j, auctions_i).immutableCopy());
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		System.out.println("Printing the SET for the user: " + username);
		System.out.println("--------------------------------------");
		for (int s : recSet) { 
		    System.out.println(s);
		}
		System.out.println("--------------------------------------");
		return  recSet;
	}
	
	public Map<String,Set<Integer>> getRecommendationForAllUsers(){
		
		Iterator<String> it = usernameToIntegerMap.keySet().iterator();
		Map<String,Set<Integer>> map = new HashMap<String,Set<Integer>>();
		while(it.hasNext()){
			String username = it.next();
			Set<Integer> set = getRecommendationForUser(username);
			map.put(username,set);
		}
		return map;
	}



}

class SimilarityComparator implements Comparator<JSONObject>{

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
