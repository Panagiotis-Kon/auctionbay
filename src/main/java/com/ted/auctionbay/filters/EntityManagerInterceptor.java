package com.ted.auctionbay.filters;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.EntityTransaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.beans.factory.annotation.Autowired;

import com.ted.auctionbay.jpautils.EntityManagerHelper;
import com.ted.auctionbay.recommendations.RecommendationService;

/**
 * Servlet Filter implementation class EntityManagerInterceptor
 */
@WebFilter("/EntityManagerInterceptor")
public class EntityManagerInterceptor implements Filter {

    /**
     * Default constructor. 
     */
	
	
	 
	    
    public EntityManagerInterceptor() {
       // System.out.println("<<<<< EntityManagerInterceptor Constructor >>>>");
     
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		//System.out.println("<<<<< EntityManagerInterceptor Destructor >>>>");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//System.out.println("<<<<< EntityManagerInterceptor doFilter >>>>");
		try {
			EntityManagerHelper.beginTransaction();
			chain.doFilter(request, response);
			EntityManagerHelper.commit();
		} 
		catch (RuntimeException e) 
		{
			EntityTransaction tx = EntityManagerHelper.getTransaction();
			if (tx != null && tx.isActive()) 
				EntityManagerHelper.rollback();
		    throw e;
		    
			 /* if ( EntityManagerHelper.getEntityManager() != null && EntityManagerHelper.getEntityManager().isOpen()) 
                  EntityManagerHelper.rollback();
              throw e;*/
			
		} 
		finally {
			EntityManagerHelper.closeEntityManager();
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		//System.out.println("<<<<< EntityManagerInterceptor Init Filter >>>>");
	}

}

