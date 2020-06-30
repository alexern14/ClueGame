/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * Class for the suggestion outline
 */

package clueGame;

public class Suggestion {
	public String person;
	public String room;
	public String weapon;
	
	public Suggestion(String person, String room, String weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}

	public String getPerson() {
		return person;
	}

	public String getRoom() {
		return room;
	}

	public String getWeapon() {
		return weapon;
	}
}
