package com.ted.auctionbay.recommendations;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecommendationsInit extends TimerTask{

	
	private static long delay = 5000;
    private static long period = 1800000;
    @Autowired
	private RecommendationService recommendationServices;
    
	 public void initRec(){   
	  
        	//TimerTask task = new RecommendationService();
		 	System.out.println("Init Rec called");
        	//Timer timer = new Timer();
        	//timer.scheduleAtFixedRate(recommendationServices, delay, period);
        	if(recommendationServices == null){
        		System.out.println("rec service null");
        	}
        	//RecommendationService svc = this.recommendationServices;
        	recommendationServices.start();
        	/*timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                svc.start();
            }
            }, 60*1000, 120*1000);*/
        	System.out.println("Task scheduled");
        	
      
	 }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
