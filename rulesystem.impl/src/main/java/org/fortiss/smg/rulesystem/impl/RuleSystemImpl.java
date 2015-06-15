/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

import java.io.*;

import org.fortiss.smg.rulesystem.api.RuleSystemInterface;

public class RuleSystemImpl implements RuleSystemInterface{

	@Override
	public boolean writeRuleInFile(String rule ) throws Exception {
		boolean executed  = false;
		File file = new File("/Users/pragyagupta/Projects/SMG_GIT/smg2/rulesystem.impl/src/main/resources/rules.drl");
		try{
			if (!file.exists()){
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(rule);
			bw.close();
			executed= true;
		}
		catch(IOException e ){
			e.printStackTrace();
		}
		return executed;
	}

	@Override
	public boolean deleteRule(String ruleName) throws Exception {
		boolean executed = false; 
		String RuleToRemove= null;
		try {
		      File inFile = new File("/Users/pragyagupta/Projects/SMG_GIT/smg2/rulesystem.impl/src/main/resources/rules.drl");
		      if (!inFile.isFile()) {
		        System.out.println("Parameter is not an existing file");
		        executed = false;
		        return executed ;
		      }
		       
		      //Construct the new file that will later be renamed to the original filename. 
		      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");
		      
		      BufferedReader br = new BufferedReader(new FileReader(inFile));
		      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
		      
		      String line = null;
		 
		      //Read from the original file and write to the new 
		      //unless content matches data to be removed.
		      while ((line = br.readLine()) != null) {
		        
		        if (!line.trim().equals(RuleToRemove)) {
		 
		          pw.println(line);
		          pw.flush();
		        }
		      }
		      pw.close();
		      br.close();
		      
		      //Delete the original file
		      if (!inFile.delete()) {
		        System.out.println("Could not delete file");
		        executed = false;
		        return executed;
		      } 
		      
		      //Rename the new file to the filename the original file had.
		      if (!tempFile.renameTo(inFile))
		        System.out.println("Could not rename file");
		      
		    }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		   }
		return executed;
	 }
		

	@Override
	public boolean updateRule(String rule) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createRule(String rule) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
