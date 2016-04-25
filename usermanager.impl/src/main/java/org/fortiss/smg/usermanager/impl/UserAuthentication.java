package org.fortiss.smg.usermanager.impl;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

import javax.servlet.ServletRequestEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.server.SessionIdManager;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.fortiss.smg.usermanager.impl.UserAuthenticationManager;


public class UserAuthentication {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(UserAuthentication.class);
	
	public static String MySQLPassword(String plainText) throws UnsupportedEncodingException {
	    byte[] utf8 = plainText.getBytes("UTF-8");
	    return "*" + DigestUtils.shaHex(DigestUtils.sha(utf8)).toUpperCase();
	}
	
	private static UserAuthenticationManager am = new UserAuthenticationManager();
	  
	  
	  public static void main(String[] args) throws Exception {
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    
	    while(true) 
	    {
	      System.out.println("Please enter your username:");
	      String name = in.readLine();
	      System.out.println("Please enter your password:");
	      String password = in.readLine();
	      String hashpw = MySQLPassword(password);
	      
	      try 
	      {    
	    	  Authentication request = new UsernamePasswordAuthenticationToken(name, hashpw);
	    	  Authentication result = am.authenticate(request);
	    	  SecurityContextHolder.getContext().setAuthentication(result);
	    	 
	    	  //Obtain current Authenticated User 
	    	  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    	  
          
	    	  if (principal instanceof UserDetails) {
	    		  String username = ((UserDetails)principal).getUsername();
	          
	    	  } else {
	    		  String username = principal.toString();
	 
	    	  }
         	  break;
	       	} 
	      catch(AuthenticationException e) 
	      {
	    	 logger.info("Authentication failed", e);
	    	  System.out.println("Authentication failed: " + e.getMessage());
	      }
	    	
	    }
	    logger.info("Successfully authenticated");
	    System.out.println("Successfully authenticated.");
//	    System.out.println("Successfully authenticated. Security context contains: " +
//	              SecurityContextHolder.getContext().getAuthentication());
	      
	  }
	  
}


