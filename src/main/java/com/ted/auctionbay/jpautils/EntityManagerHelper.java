package com.ted.auctionbay.jpautils;


import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.recommendations.RecommendationService;
import com.ted.auctionbay.recommendations.RecommendationsInit;

public class EntityManagerHelper {

	private static final EntityManagerFactory emf; 
    private static final ThreadLocal<EntityManager> threadLocal;
   // private static boolean initialize = true;

    static {
    	System.out.println("ENTITY MANAGER HELPER STARTS");
        emf = Persistence.createEntityManagerFactory("auctionbay");      
        threadLocal = new ThreadLocal<EntityManager>();
    
        
    }

    public static EntityManager getEntityManager() {
        EntityManager em = threadLocal.get();

        if (em == null) {
            em = emf.createEntityManager();
            threadLocal.set(em);
            //System.out.println("if em == null");
        }
       /* if(initialize){
        	RecommendationsInit ri = new RecommendationsInit();
        	ri.initRec();
        	initialize = false;
        }*/
       
        
        return em;
    }

    public static void closeEntityManager() {
        EntityManager em = threadLocal.get();
        if (em != null) {
            em.close();
            threadLocal.set(null);
        }
    }

    public static void closeEntityManagerFactory() {
        emf.close();
    }

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }
    
    public static EntityTransaction getTransaction() {
        return getEntityManager().getTransaction();
    }

    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }

    public static void commit() {
        getEntityManager().getTransaction().commit();
    } 

	
}
