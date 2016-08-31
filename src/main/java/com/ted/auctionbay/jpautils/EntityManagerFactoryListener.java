package com.ted.auctionbay.jpautils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.core.SpringVersion;

import com.ted.auctionbay.recommendations.RecommendationEngine;

public class EntityManagerFactoryListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		System.out.println("CONTEXT INITIALIZED");
		System.out.println("version: " + SpringVersion.getVersion());
		RecommendationEngine.run();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		EntityManagerHelper.closeEntityManagerFactory();
	}

}
