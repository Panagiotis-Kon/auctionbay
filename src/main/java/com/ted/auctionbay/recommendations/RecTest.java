package com.ted.auctionbay.recommendations;

//import com.project.eauctions.controllers.ContextListener;

public class RecTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ContextListener cnt = new ContextListener();
//		cnt.contextInitialized(null);
		RecommendationEngine.run();
		System.out.println(RecommendationEngine.getRecommendationForAllUsers());
	}

}
