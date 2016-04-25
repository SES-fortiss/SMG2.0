package org.fortiss.smg.analyzer.api;

/**
 * exception which is caused, if no data was found in the database
 * 
 * @author Ann Katrin Gibtner (annkatrin.gibtner@tum.de)
 *
 */
public class NoDataFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6607594016300161957L;

	public NoDataFoundException(String string) {
		super(string);
	}
}
