/**
 * 
 */
package org.fortiss.smg.gamification.api;

/**
 * @author Pahlke
 *
 */
public abstract class Property {
	final int id;
	final String title;
	final String description;
	
	public Property(int id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
}
