/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * Class for all the cards
 */

package clueGame;

public class Card {
	private String cardName;
	private CardType type;

	public Card(String name, CardType cardType) {
		this.cardName = name;
		this.type = cardType;
	}

	public String getCardName() {
		return cardName;
	}
	
	public CardType getType() {
		return type;
	}

	public boolean equals() {
		return false;
	}
	
	public boolean isRoom() {
		if (type == CardType.ROOM)
			return true;
		else return false;
	}
	
	public boolean isPerson() {
		if (type == CardType.PERSON)
			return true;
		else return false;
	}
	
	public boolean isWeapon() {
		if (type == CardType.WEAPON)
			return true;
		else return false;
	}
}
