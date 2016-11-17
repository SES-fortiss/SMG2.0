package org.fortiss.smg.rulesystem.impl.executor;

import java.util.*;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SendEmail {
	private static final Logger logger = LoggerFactory
			.getLogger(SendEmail.class);
	List<String> recipients = new ArrayList<String>();
	File coffeeFile =  new File (System.getProperty("user.home") + "/coffeeFile.csv");

	public String checkEmailId(String s) {
		String action= null;
		try {
			boolean bool = true;
			List <String>mailIDs = new ArrayList<String>();  
			List <String>tempList = new ArrayList<String>();  


			mailIDs= readFile();
			SendEmail.logger.debug(mailIDs.toString());

			Iterator<String> iter = mailIDs.iterator();
			while(iter.hasNext()) { 
				String word = iter.next();
				if(word.equals(s)) {
					iter.remove();
					bool  = false;
					action= "Unsubscribed! You will no longer receive notifications";
				}else {
					tempList.add(word);
				}
			}

			if(bool == true) {
				tempList.add(s);
				action = sendConfirmationMail(s);
				SendEmail.logger.debug("data to be copied to the file" + tempList);

			}
			writeFile(tempList);

			//								        System.out.println("Subscribed!");




		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return action;
	}

	public static String sendConfirmationMail(String s1){
		//			recipients = readFile();
		//			String to = s1;
		String emailIDCheck = " ";
		boolean checked = checkInput(s1);
		if (checked == false){
			SendEmail.logger.debug("incorrect username: TRY AGAIN WITHOUT ENTERING SPECIAL CHARACTER");
			emailIDCheck = "Incorrect Input: TRY AGAIN WITHOUT ENTERING SPECIAL CHARACTER";
		}
		else{
			SendEmail.logger.debug("correct username");
			emailIDCheck = "Subscribe successful! Confirmation mail sent....";

			// Sender's email ID needs to be mentioned
//			String from = "Fortiss.Coffee@fortiss.org";
			String from = "Fortiss.Coffee@fortiss.org";

			String host = "mail.fortiss.org";

			// Get system properties
			Properties properties = System.getProperties();

			// Setup mail server
			properties.setProperty("mail.smtp.host", host);

			// Get the default Session object.
			Session session = Session.getDefaultInstance(properties);

			try{
				// Create a default MimeMessage object.
				MimeMessage message = new MimeMessage(session);

				//				 Set From: header field of the header.
				message.setFrom(new InternetAddress(from));

				// Set Subject: header field
				message.setSubject("Notification mail");

				// Now set the actual message
				message.setText("Please don not plug in heavy devices.");

				String to = s1 + "@fortiss.org";//bufferRead.readLine();

				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				Transport.send(message);
				SendEmail.logger.debug("Subscription mail sent to: " +to);
			}
			catch (MessagingException mex) {
				mex.printStackTrace();
			}
		}
		return emailIDCheck;
	}	

	private static boolean checkInput(String s) {
		Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
		return p.matcher(s).matches();
	}

	public void sendMail(){
		recipients= readFile ();
		SendEmail.logger.debug("recipients: " + recipients);
		String to = null;
		// Sender's email ID needs to be mentioned
		String from = "Fortiss.SMG@fortiss.org";
		String host = "mail.fortiss.org";
		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		try{			
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));
			// Set Subject: header field
			message.setSubject("Please do not plug in heavy devices.");
			// Now set the actual message
			message.setText(" ");
			for(Iterator<String> iter = recipients.iterator(); iter.hasNext();) {
				to = iter.next().concat("@fortiss.org");//bufferRead.readLine();
				// Set To: header field of the header.
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				// Send message

			}
			SendEmail.logger.debug("++++++++++++++++++++++++++++++++++++++Coffee mail sent successfully to ...." +recipients);
			Transport.send(message);

		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
//		System.out.println("++++++++++++++++++++++++++++++++++++++Coffee mail sent successfully to ...." +recipients);
	}

	private List<String> readFile()  {
		String line = null;  
		List<String> fileData = new ArrayList<String>();
		try{
			if (!coffeeFile.exists()) {
				coffeeFile.createNewFile();
			}
			if (coffeeFile.exists()){		
				SendEmail.logger.debug("Yes! file exists " +coffeeFile.getAbsolutePath()); }
			BufferedReader br = new BufferedReader(new FileReader (coffeeFile));
			while ((line=br.readLine())!= null){
				boolean checked = checkInput(line);
				if (checked == false){
					SendEmail.logger.debug("incorrect username: MOVING ON TO NEXT");
					br.skip(0);
				}
				else{
					SendEmail.logger.debug("correct username");
					fileData.add(line);
				}
			}
			br.close();
			SendEmail.logger.debug("fileData:  "+fileData);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return fileData;
	}

	public  void writeFile(List<String> tempList){
		String temp=null;
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(coffeeFile, false) );

			for(Iterator<String> tempIter = tempList.iterator(); tempIter.hasNext();){
				temp = tempIter.next();
				bw.write(temp);
				bw.newLine();
			}
			bw.close();


		} catch (Exception e) {
			e.printStackTrace();
		}
		recipients= tempList;
	}
}