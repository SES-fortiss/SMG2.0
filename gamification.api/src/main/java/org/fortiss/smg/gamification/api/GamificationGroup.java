package org.fortiss.smg.gamification.api;

import java.util.HashMap;

public class GamificationGroup extends GamificationParticipant {
	
	private int id;
	
	public GamificationGroup(int id, int level, int score, String name) {
		super(level, score, name);
		this.id = id;
	}
	
	public GamificationGroup(GamificationGroup original) {
		super(original.getLevel(),original.getScore(), original.getName());
		id = original.getId();
	}

	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}




	public HashMap<String, Object> serialize(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", this.id);
		map.put("level", this.level);
		map.put("score", this.score);
		map.put("name", this.name);
		return map;
	}

	public void deserialize(HashMap<String, Object> groups){
		this.id = Integer.parseInt(""+groups.get("id"));
		this.level = Integer.parseInt("" +groups.get("level"));
		this.score = Integer.parseInt("" +groups.get("score"));
		this.name = ""+groups.get("name");
		
	}
	
}
