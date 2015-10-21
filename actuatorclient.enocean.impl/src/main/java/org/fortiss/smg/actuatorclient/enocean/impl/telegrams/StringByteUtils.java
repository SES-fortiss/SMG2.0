package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;


public class StringByteUtils {
	/**
	 * Converts a hex-encoded string into a byte in char type.
	 * 
	 * @param orgString
	 *            a string of an hex-value, e.g. "F0" (0x is not part of this
	 *            string)
	 * @return the value of the string in byte (as char)
	 */
	public static char hexStringToByteChar(String orgString) {
		if (orgString.length()>2) {
			throw new IllegalArgumentException("Too long, has to be 2 characters");
		}
		Integer orgInt = Integer.valueOf(orgString, 16);
		char val = (char) orgInt.intValue();
		return val;
	}

	
	/**
	 * Reads a number of bytes from a hex-encoded string
	 * 
	 * @param byteString
	 *            a string of hex-encoded bytes, e.g. AFF3F00
	 * @param offset2
	 *            where in the string to start (in characters)
	 * @param length2
	 *            number of bytes to read
	 * @return
	 */
	public static char[] readBytes(String byteString) {
		if (Math.abs(byteString.length())%2==1) {
			throw new IllegalArgumentException("Must be of even length");
		}
		int length = byteString.length()/2;
		char[] result = new char[length];
		for (int i = 0; i < length; i++) {
			int pos =  2 * i;
			String dat = byteString.substring(pos, pos + 2);
			result[i] = hexStringToByteChar(dat);
		}
		return result;
	}
	
	public static String byteCharsToHexString(char[] arr) {
		StringBuilder strb = new StringBuilder();

		for (char ch : arr) {
			String hex = Integer.toHexString(ch);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			strb.append(hex);
		}
		return strb.toString().toUpperCase();
	}

	public static String byteCharToHexString(char byt) {
		StringBuilder strb = new StringBuilder();
		
		String hex = Integer.toHexString(byt);
		if (hex.length() == 1) {
			hex = "0" + hex;
		}
			strb.append(hex);
		return strb.toString().toUpperCase();
	}
	
	public static String byteCharsToHexString(char[] arr, int offset, int length) {
		char[] tmp = new char[length];
		for (int i = offset; i < offset+length; i++) {
			tmp[i-offset] = arr[i];
		}
		return byteCharsToHexString(tmp);

	}


	public static char byteToChar(byte b) {
		return (char) ((b+256)%256); //MODULO OK??
	}
	
	
	
}
