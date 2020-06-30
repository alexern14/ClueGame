/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * Player class
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Player {
	private String playerName;
	private int row;
	private int column;
	private Color color;
	private Set<Card> hand;
	private Set<Card> notSeenCards;
	private int width = 30; // width of the player's piece
	private int height = 30;  // height of the player's piece
	private String dieRoll;
	Board board = Board.getInstance();
	private boolean finished = true;  // flag whether the player's turn is finished or not
	private boolean hasAccused = false;  // flag whether the player has made an accusation on this turn or not
	private boolean canAccuse = true;  // flag whether the player can make an accusation or not
	private boolean suggested = false;  // flag whether the player has made a suggestion or not
	private String roomGuess, personGuess, weaponGuess; 
	
	/*
	 * Constructor
	 */
	public Player(String playerName, int row, int column, Color color) {
		super();
		this.playerName = playerName;
		this.row = row;
		this.column = column;
		this.color = color;
	}
	
	/*
	 * Draws this player
	 */
	public void drawPlayer(Graphics g) {
		g.setColor(Color.black);
		g.drawOval(getX(), getY(), width, height);
		g.setColor(this.color);
		g.fillOval(getX(), getY(), width, height);
	}
	
	/*
	 * Moves this player to selected cell
	 */
	public void move(BoardCell cell) {
		int newRow = cell.getRow();
		int newColumn = cell.getColumn();
		BoardCell oldCell = board.getCellAt(row, column);
		oldCell.removePlayerOnCell(this);
		this.setRow(newRow);
		this.setColumn(newColumn);
		BoardCell newCell = board.getCellAt(row, column);
		newCell.addPlayerOnCell(this);
		board.repaint();
		canAccuse = false;
	}

	/*
	 * Checks player's hand for a card that matches one of the solution parameters (thus disproving the suggestion)
	 * and returns the card that can disprove it or null if player cannot disprove
	 */
	public Card disproveSuggestion(Suggestion suggestion) {
		// create a new array list to keep track of the player's card that can disprove the suggestion
		ArrayList<Card> matching = new ArrayList<Card>();
		for (Card card : this.getHand()) {
			// if the card matches one of the suggestions, add it to the matching array list
			String cardName = card.getCardName();
			String personSug = suggestion.getPerson();
			String weaponSug = suggestion.getWeapon();
			String roomSug = suggestion.getRoom();
			if (cardName.equals(personSug)|| cardName.equals(weaponSug) || cardName.equals(roomSug)) 
				matching.add(card);
		}
		Card matchingCard = null;
		if (matching.size() != 0) {
			// if there's more than 1 card that matches, choose one randomly
			if (matching.size() > 1) 
				matchingCard = getRandomElement(matching);
			else if (matching.size() == 1) {
				// if only one card matched, return that card
				matchingCard = matching.get(0);
			}
			return matchingCard;
		} // if no cards in hand matched, return null (cannot disprove)
		return null;
	}
	
	/*
	 * Helper function for createSuggestion
	 * Selects a random card from the ArrayList of cards passed in
	 */
	public Card getRandomElement(ArrayList<Card> cards){ 
		Random rand = new Random(); 
		return cards.get(rand.nextInt(cards.size())); 
	} 
	
	/*
	 * Rolls the die (random selection 1 - 6)
	 */
	public void rollDie() {
		int roll = (int) (Math.random() * 6) + 1;
		String rollString = Integer.toString(roll);
		this.dieRoll = rollString;
	}
	
	/*
	 * Getters and setters:
	 */
	
	/*
	 * Returns whether or not this player is human
	 */
	public boolean isHuman(Player player) {
		if (player instanceof HumanPlayer)
			return true;
		else return false;
	}
	
	private int getY() {
		return (getRow() * height);
	}

	private int getX() {
		return (getColumn() * width);
	}

	public String getName() {
		return playerName;
	}

	public Color getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public void setHand(Set<Card> hand) {
		this.hand = hand;
	}
	
	public Set<Card> getNotSeenCards() {
		return notSeenCards;
	}
	
	public void setNotSeenCards(Set<Card> notSeenCards) {
		this.notSeenCards = notSeenCards;
	}

	public void removeNotSeenCards(Card seenCard) {
		if (notSeenCards.contains(seenCard))
			this.notSeenCards.remove(seenCard);
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	public String getDieRoll() {
		return dieRoll;
	}
	
	public void setAccused(boolean accused) {
		this.hasAccused = accused;
	}
	
	public boolean hasAccused() {
		return hasAccused;
	}
	
	public boolean canAccuse() {
		return canAccuse;
	}

	public void setCanAccuse(boolean canAccuse) {
		this.canAccuse = canAccuse;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	public String getRoomGuess() {
		return roomGuess;
	}

	public void setRoomGuess(String roomGuess) {
		this.roomGuess = roomGuess;
	}

	public String getPersonGuess() {
		return personGuess;
	}

	public void setPersonGuess(String personGuess) {
		this.personGuess = personGuess;
	}

	public String getWeaponGuess() {
		return weaponGuess;
	}

	public void setWeaponGuess(String weaponGuess) {
		this.weaponGuess = weaponGuess;
	}

	public boolean isSuggested() {
		return suggested;
	}

	public void setSuggested(boolean suggested) {
		this.suggested = suggested;
	}
}
