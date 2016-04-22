package org.fortiss.smg.rulesystem.impl.executor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RuleSystemDroolsDrl {

	public static boolean writeRule(String rule ) throws Exception {
		boolean executed  = false;

		String drlPath = "/opt/felix/drools.drl";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			drlPath = "c:/opt/felix/drools.drl";
		}
		File file = new File(new File(drlPath).getAbsolutePath());
		
		
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
	
	
	public static boolean deleteRule(String ruleName) throws Exception {
		boolean executed = true; 
		String drlPath = "/opt/felix/drools.drl";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			drlPath = "c:/opt/felix/drools.drl";
		}
		
		try {
			File inFile = new File(drlPath);
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
		      int lineCounter = 0;
		      //Read from the original file and write to the new 
		      //unless content matches data to be removed.
		      while ((line = br.readLine()) != null) {
		        
		        if (!line.trim().equals("rule " + "\"" + ruleName + "\"") && lineCounter==0) {
		          pw.println(line);
		          pw.flush();
		        }else{
		        if(lineCounter < 4)
		        	lineCounter++;
		        else 
		        	lineCounter = 0 ;
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
		      if (!tempFile.renameTo(inFile)){
		        System.out.println("Could not rename file");
		        executed = false;
		      }
		    }
		    catch (FileNotFoundException ex) {
		      ex.printStackTrace();
		    }
		    catch (IOException ex) {
		      ex.printStackTrace();
		   }
		return executed;
	 }
	
}
