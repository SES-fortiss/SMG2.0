/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.usermanager.impl;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.usermanager.api.UserManagerInterface;
import org.fortiss.smg.usermanager.api.UserManagerQueueNames;


public class UserAuthenticationManager {
	
	static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
	private UserManagerInterface userManager ;
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(UserAuthenticationManager.class);
	 static {
	    AUTHORITIES.add(new GrantedAuthorityImpl("ROLE_USER"));
	 }
	 UserAuthenticationManager () {
		 DefaultProxy<UserManagerInterface> proxyUserManager = 
				 new DefaultProxy<UserManagerInterface>(UserManagerInterface.class, 
						 UserManagerQueueNames.getUserManagerInterfaceQueue(), 600000);
	    	try {
				userManager = proxyUserManager.init();
			} catch (Exception e) {
				logger.error("Connecting User Authentication Manager to user manager api failed", e);
			}
	}
	 
	 public Authentication authenticate(Authentication auth) throws Exception {
	   if (userManager.validLoginHash(auth.getName(),auth.getCredentials())) {
	     return new UsernamePasswordAuthenticationToken(auth.getName(),
	       auth.getCredentials(), AUTHORITIES);
	   }
	   throw new BadCredentialsException("Bad Credentials");
	 }
	
}


