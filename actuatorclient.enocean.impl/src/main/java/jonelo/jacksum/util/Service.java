/******************************************************************************
*
* Jacksum version 1.7.0 - checksum utility in Java
* Copyright (C) 2001-2006  Dipl.-Inf. (FH) Johann Nepomuk Loefflmann,
* All Rights Reserved, http://www.jonelo.de
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*
* E-mail: jonelo@jonelo.de
*
*****************************************************************************/

package jonelo.jacksum.util;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import jonelo.sugar.util.GeneralString;

public class Service {

   private final static char[] HEX = "0123456789abcdef".toCharArray();

   public static String right(long number, int blanks) {
       StringBuffer sb=new StringBuffer(number+"");
       while (sb.length() < blanks) sb.insert(0,' ');
       return sb.toString();
   }

   public static String decformat(long number, String mask) {
       DecimalFormat df = new DecimalFormat(mask);
       return df.format(number);
   }

   public static String hexformat(long value, int nibbles) {
       StringBuffer sb = new StringBuffer(Long.toHexString(value));
       while (sb.length() < nibbles) sb.insert(0,'0');
       return sb.toString();
   }

   public static String hexformat(long value, int nibbles, int group, char groupChar) {
       StringBuffer sb = new StringBuffer(Long.toHexString(value));
       while (sb.length() < nibbles) sb.insert(0,'0');

       if (group > 0) sb=insertBlanks(sb, group, groupChar);
       return sb.toString();
   }

   public static String format(byte[] bytes) {
       return format(bytes,false);
   }

   private static StringBuffer insertBlanks(StringBuffer sb, int group, char groupChar) {
       int bytecount = sb.length()/2; // we expect a hex string
       if (bytecount <= group) return sb; // avoid unnecessary action
       StringBuffer sb2=new StringBuffer(sb.length()+(bytecount/group - 1));
       int group2=group*2;

       for (int i=0; i < sb.length(); i++) {
          if ((i > 0) && (i % (group2))==0) {
              sb2.append(groupChar);
          }
          sb2.append(sb.charAt(i));
       }
       return sb2;

   }

   public static String format(byte[] bytes, boolean uppercase, int group, char groupChar) {
       if (bytes==null) return "";
       StringBuffer sb = new StringBuffer(bytes.length * 2);
       int b;
       for (int i=0; i < bytes.length; i++) {
           b=bytes[i] & 0xFF;
           sb.append(HEX[b >>> 4]);
           sb.append(HEX[b & 0x0F]);
       }
       if (group > 0) sb=insertBlanks(sb, group, groupChar);
       return (uppercase ? sb.toString().toUpperCase() : sb.toString());
   }
   
   public static String formatAsBits(byte[] bytes) {
       if (bytes==null) return "";
       StringBuffer sb = new StringBuffer(bytes.length);
       BigInteger big = new BigInteger(1, bytes);
       sb.append(big.toString(2)); // dual
       while (sb.length() < (bytes.length*8)) sb.insert(0,'0');
       return sb.toString();        
   }

   public static String format(byte[] bytes, boolean uppercase) {
       return format(bytes,false,0,' ');
   }


  public static boolean isSymbolicLink(File file) {
    // there are no symbolic links on Windows
    // on Windows a link is always a file
    if (System.getProperty("os.name").toLowerCase().startsWith("windows"))
      return false;

    try {
       String cnnpath = file.getCanonicalPath();
       String abspath = file.getAbsolutePath();
       abspath=GeneralString.replaceString(abspath,"/./","/");
       if (abspath.endsWith("/.")) return false;
       return !abspath.equals(cnnpath);
    } catch(IOException ex) {
     System.err.println(ex);
     return true;
   }
  }

 /**
   * Method transfers a long to 8 bytes
   * and fills the array given in argument
   * @param i a long value
   * @param b the byte array
   */
  public static void setLongInByteArray(long i, byte[] b) throws IndexOutOfBoundsException    {
     setLongInByteArray(i,b,0);
  }

 /**
   * Method transfers a long to 8 bytes
   * and fills the array given in argument
   * @param i a long value
   * @param b the byte array
   * @param index the index in the array
   */
  public static void setLongInByteArray(long i, byte[] b, int index) throws IndexOutOfBoundsException {
    byte[] b1 = new byte[8];
    long i1;
    for (int j=0; j<8; j++) {
    i1 = (i & 0xff);
      b1[j] = (byte) i1;
      i = i >> 8;
    }
    for (int j=0; j<8; j++) {
      b[j+index] = b1[7-j];
    }
  }

  public static void setIntInByteArray(int i, byte[] b) throws IndexOutOfBoundsException {
      setIntInByteArray(i, b, 0);
  }

 /**
  * Method transforms an int to 4 bytes
  * and fills the array given in argument
  * @param i a int value
  * @param b the byte array
  * @param index the index in the array
  */
  public static void setIntInByteArray(int i, byte[] b, int index) throws IndexOutOfBoundsException {

    byte[] b1 = new byte[4];
    int i1;
    for (int j=0; j<4; j++) {
      i1 = (i & 0xff) ;
      b1[j] = (byte) i1;
      i = i >> 8;
    }

    for (int j=0; j<4; j++) {
       b[j+index] = b1[3-j];
    }
  }

   /**
    * Return a human readable String which represents a time in ms
    * @param t a time in ms
    * @return a human readable representation of time as String
    */
   public static final String duration(long t) {
       long ms = 0, s = 0, m = 0, h = 0, d = 0;
       ms = t % 1000; t /= 1000;
       if (t > 0) {
           s  = t % 60; t /= 60; }
       if (t > 0) {
           m = t % 60; t /= 60; }
       if (t > 0) {
           h = t % 24; t /= 24; }
       d = t;
       
       return (d+" d, "+
               h+" h, "+
               m+" m, "+
               s+" s, "+
               ms+" ms");
   }

}
