package org.fortiss.smg.rulesystem.api;

public class Message {

		private String message;
     private int status;

	 public static final int HELLO = 0;
     public static final int GOODBYE = 1;
     public static int counter = 0;
     public Message(String message, int status) {
         this.message = message;
         this.status = status;
     }
     
   
     public String getMessage() {
         return this.message;
     }

     public void setMessage(String message) {
         this.message = message;
     }

     public int getStatus() {
         return this.status;
     }

     public void setStatus(int status) {
         this.status = status;
     }

 }

