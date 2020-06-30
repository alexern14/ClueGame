/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * For the computer players
 */

package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

public class ComputerPlayer extends Player{
	Board board = Board.getInstance();
	private boolean accuse = false;
	private Suggestion accusation;
	
	/*
	 * Constructor
	 */
	public ComputerPlayer(String playerName, int row, int column, Color color) {
		super(playerName, row, column, color);
	}
	
	/*
	 * Makes the move for the computer player
	 *  - Picks the target location
	 *  - moves the player
	 *  	- if player moves to a room, it creates a suggestion
	 */
	public void makeMove(Set<BoardCell> Targets) {
		move(pickLocation(Targets));
		// if the cell moved to is a room create a suggestion
		if (board.getCellAt(getRow(), getColumn()).isRoom()) {
			Suggestion suggestion = createSuggestion();
			String guess = suggestion.getPerson() + ", " + suggestion.getRoom() + ", " + suggestion.getWeapon();
			ControlGUI.setGuess(guess);
			Card disprovingCard = board.handleSuggestion(suggestion, board.getPeople(), this);
			if (disprovingCard == null) {
				this.accuse = true;
				this.accusation = suggestion;
				ControlGUI.setGuessResult("no new clue");
			}else {
				String disprove = disprovingCard.getCardName();
				ControlGUI.setGuessResult(disprove);
				for (Player player : board.getPeople()) {
					player.removeNotSeenCards(disprovingCard);
				}
			}
		}
	}

	/*
	 * Picks a location to move to from given targets
	 */
	public BoardCell pickLocation(Set<BoardCell> targets) {
		for (BoardCell cell : targets) {
			// if target is a room
			if (cell.isDoorway()){
				BoardCell curBoardCell = board.getCellAt(this.getRow(), this.getColumn());
				char initial = curBoardCell.getInitial();
				String roomName = cell.getRoomName();
				Card roomCard = null;
				for (Card card : board.getDeck()) {
					if (card.getCardName().equals(roomName)) 
						roomCard = card;
				}
				Set<Card> hand = this.getHand();
				if (hand != null) {
					if (!hand.contains(roomCard)) {
						// if target is not room player was just currently in
						if (initial != cell.getInitial())
							return cell;
					}
				}else if (initial != cell.getInitial())
					return cell;
			}
		}
		// set the current boardcell player to null
		BoardCell currCell = board.getCellAt(this.getRow(), this.getColumn());
		currCell.removePlayerOnCell(this);
		// randomly select a target
		BoardCell cell = getRandomBoardCell(targets);
		// set the target boardcell to this player
		cell.addPlayerOnCell(this);
		return cell;
	}

	/*
	 * Creates a suggestion based on the notSeenCards hand
	 */
	public Suggestion createSuggestion() {
		// get current cell in order to know which room player is currently in
		BoardCell curCell = board.getCellAt(this.getRow(), this.getColumn());
		// set room suggestion to player's current location
		String roomSuggestion = curCell.getRoomName();
		// create 2 array (1 for people not seen and 1 for weapons not seen)
		ArrayList<Card> notSeenPersons = new ArrayList<Card>();
		ArrayList<Card> notSeenWeapons = new ArrayList<Card>();
		// populate those arrays with the player's not seen cards hand
		for (Card card : this.getNotSeenCards()) {
			if (card.getType() == CardType.PERSON)
				notSeenPersons.add(card);
			else if (card.getType() == CardType.WEAPON)
				notSeenWeapons.add(card);
		}
		// pick a random person and a random weapon
		Card personCard = getRandomCard(notSeenPersons); 
		Card weaponCard = getRandomCard(notSeenWeapons);
		// create suggestion
		return new Suggestion(personCard.getCardName(), roomSuggestion, weaponCard.getCardName());
	}

	/*
	 * Helper method for pickLocation
	 * Returns a random BoardCell from the set of targets given
	 */
	private BoardCell getRandomBoardCell(Set<BoardCell> targets){
		Random random = new Random();
		int randomNumber = random.nextInt(targets.size());
		Iterator<BoardCell> iterator = targets.iterator();
		int currentIndex = 0;
		BoardCell randomElement = null;
		while(iterator.hasNext()){     
			randomElement = iterator.next();
			if(currentIndex == randomNumber)
				return randomElement;     
			currentIndex++;
		}
		return randomElement;
	}    
	
	/*
	 * Helper function for createSuggestion
	 * Selects a random card from the ArrayList of cards passed in
	 */
	public Card getRandomCard(ArrayList<Card> cards) { 
		Random rand = new Random(); 
		return cards.get(rand.nextInt(cards.size())); 
	} 
	
	public boolean isReadyToAccuse() {
		return accuse;
	}
	
	public void setReadyToAccuse(boolean accuse) {
		this.accuse = accuse;
	}
	
	public Suggestion getAccusation() {
		return accusation;
	}
}
