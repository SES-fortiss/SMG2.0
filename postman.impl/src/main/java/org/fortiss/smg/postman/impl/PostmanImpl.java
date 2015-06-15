/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.postman.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Properties;

import org.fortiss.smg.postman.api.NotificationType;
import org.fortiss.smg.postman.api.PostmanInterface;
import org.fortiss.smg.usermanager.api.User;
import org.slf4j.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuth2Token;

import javax.mail.*;
import javax.mail.internet.*;

public class PostmanImpl implements PostmanInterface {

	 private static Logger logger = org.slf4j.LoggerFactory.getLogger( PostmanInterface.class );
	 
	 private String mailFrom,mailHost,mailPort;
	 private String ircServer;
	 
	 // Twitter Para
	 private String twitterConsumerKeyStr,twitterConsumerSecretStr,twitterAccessTokenStr,twitterAccessTokenSecretStr;
		
	 
	 
	 public PostmanImpl(String mailFrom,String mailHost, String mailPort, String ircServer) {
	 	 this.mailFrom = mailFrom;
	 	 this.mailHost = mailHost;
		 this.mailPort = mailPort;
		 this.ircServer = ircServer;
		 
		 twitterConsumerKeyStr = "oOa7ckhM0kN1K6Vv3dcxW70qw";
		 twitterConsumerSecretStr = "m4jMf2hTYz9RJvskzeadZkXi84iNy1POjTWOOUIaXKzOVlPckc";
		 twitterAccessTokenStr = "3005855835-3ihoKHSilVYVfl2NK00vSHwPgpDz3juMU2dLGQX";
		 twitterAccessTokenSecretStr = "AWwa0Nyg7WoeDqNVIA5DEoQDkOd8xt1EH0kwbFvQFLQ1H";
		}
	 
		//@Override
		public boolean isComponentAlive() {
			// TODO Auto-generated method stub
			return false;
		}

		
		//Facebook integration
		/**
		 * <dependency>
  			<groupId>org.facebook4j</groupId>
  			<artifactId>facebook4j-core</artifactId>
			<version>[2.2,)</version>
		   </dependency>
		 */
		
		//Twitter integration
		//E-mail:forttwittertest@gmail.com
		//E-mail-Password: fortTest
		//Twitter-Password: fortTest
		
		public void tweet(String messsage){
			try {
				Twitter twitter = new TwitterFactory().getInstance();
	
				twitter.setOAuthConsumer(twitterConsumerKeyStr, twitterConsumerSecretStr);
				AccessToken accessToken = new AccessToken(twitterAccessTokenStr,
						twitterAccessTokenSecretStr);
				twitter.setOAuthAccessToken(accessToken);
				
				twitter.updateStatus(messsage);

				logger.info("Updated status on Twitter: \"" + messsage + "\"" );
				
			} catch (TwitterException e) {
				logger.debug("Twitter connection failed");
				e.printStackTrace();
			}
		}

		
		@Override
		public void sendMail(User user, String subject, String message, NotificationType type) {
			String to = user.getEmail();
			String from = mailFrom;  //"Fortiss.Coffee@fortiss.org";
			String host = mailHost;  //"merkur.fortiss.org";
			String port = mailPort;  //"25";
			
			// Get system properties
		      Properties properties = System.getProperties();

		      // Setup mail server
		      properties.setProperty("mail.smtp.host", host);
		      properties.setProperty("mail.smtp.port", port);

		      // Get the default Session object.
		      Session session = Session.getDefaultInstance(properties);
		     
		      try{
		          // Create a default MimeMessage object.
		          MimeMessage mimeMessage = new MimeMessage(session);

		          // Set From: header field of the header.
		          mimeMessage.setFrom(new InternetAddress(from));

		          // Set To: header field of the header.
		          mimeMessage.addRecipient(Message.RecipientType.TO,
		                                   new InternetAddress(to));

		          // Set Subject: header field
		          mimeMessage.setSubject(subject);

		          // Send the actual HTML message, as big as you like
		          mimeMessage.setContent(message,
		                             "text/html" );

		          // Send message
		          Transport.send(mimeMessage);
					logger.debug("Message with Subject '" + mimeMessage.getSubject().toString() + "' has been sent to " + 
							user.getName() + " on the E-mail adress " + user.getEmail());
		       }catch (MessagingException mex) {
		    	   logger.debug("Message could not be send to " + 
							user.getName() + " on the E-mail adress " + user.getEmail());
		    	   mex.printStackTrace();
		          
		          
		       }
		    }
		
		public void sendIRCMessage(String text, String channel){

	        // The server to connect to and our details.
	        String server = ircServer;//"192.168.21.240";
	        String nick = "SMG";
	        String login = "SMG";

	        // The channel which the bot will join.
	        //String channel = "##";
	        
	        // Connect directly to the IRC server.
	        Socket socket=null;
	        BufferedWriter writer=null;
	        BufferedReader reader=null;
	        String line = null;
	        StringWriter x = null;
	        
			try {
			socket = new Socket(server, 6667);

	        writer = new BufferedWriter(
	                new OutputStreamWriter(socket.getOutputStream( )));
	        reader = new BufferedReader(
	                new InputStreamReader(socket.getInputStream( )));
	        
	        // Log on to the server.
	        writer.write("NICK " + nick + "\r\n");
	        writer.write("USER " + login + " 8 * : SMG logged in\r\n");
	        writer.flush( );
	        
	        // Read lines from the server until it tells us we have connected.
	        while ((line = reader.readLine( )) != null) {
	            if (line.indexOf("004") >= 0) {
	                // We are now logged in.
	                break;
	            }
	            else if (line.indexOf("433") >= 0) {
	                System.out.println("Nickname is already in use.");
	                return;
	            }
	        }
	        
	        writer.write("PRIVMSG " + channel + " :" + text + "\r\n");
	        writer.flush( );
	        
	        
	        	//This is how you join the channel;
		    	//writer.write("JOIN " + channel + "\r\n");
		    	//writer.flush( );
	        
		        // Keep reading lines from the server.
/*		        while ((line = reader.readLine( )) != null) {
		            if (line.toLowerCase( ).startsWith("PING ")) {
		                // We must respond to PINGs to avoid being disconnected.
		                writer.write("PONG " + line.substring(5) + "\r\n");
		                writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
		                writer.flush( );
		            }
		            else {
		                // Print the raw line received by the bot.
		                System.out.println(line);
		            }
		        }*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    }
		
		      

		

		public String getMailHost() {
			return mailHost;
		}


		public void setMailHost(String mailHost) {
			this.mailHost = mailHost;
		}


		public String getMailPort() {
			return mailPort;
		}


		public void setMailPort(String mailPort) {
			this.mailPort = mailPort;
		}


		public String getIrcServer() {
			return ircServer;
		}


		public void setIrcServer(String ircServer) {
			this.ircServer = ircServer;
		}


		@Override
		public void crashReport(String klass, String message) {
			// TODO Auto-generated method stub
			logger.info("Report received: "+ message);
		}
}
