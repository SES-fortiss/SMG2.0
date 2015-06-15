/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

public class Message {

		private String message;
     private int status;

	 public static final int HELLO = 0;
     public static final int GOODBYE = 1;

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

