/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * Tests:
 * -loading the people
 * -loading and creating the deck of cards
 * -dealing the cards
 */

package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.Player;

public class gameSetupTests {
	
	static final int DECK_SIZE = 21; //6 people, 6 weapons and the 9 rooms
	static final int CARDS_TO_BE_DEALT = 18; //6 people, 6 weapons and the 9 rooms - the solution(1 of each)
	static final int NUM_PLAYERS = 6; //1 human and 5 computer players
	
	private static Board board;
	
	// Sets up the board and cards needed for testing
	@BeforeClass
	public static void setUp() {
		board = Board.getInstance();
		board.loadConfigFiles("Players.txt", "Weapons.txt", "ClueRooms.txt");
	}
	
	/*
	 * Test loading people:
	 */
	
	//test total number of players
	@Test
	public void testPlayers() {
		Map<String, Player>playerList = board.getPlayers();
		int numPlayers = playerList.size();
		assertEquals(NUM_PLAYERS, numPlayers);
	}

	//test a human player for correct name, color, human/computer, and starting location
	@Test
	public void testMelancholyMay() {
		Player player = board.getPlayer("Melancholy May");

		assertEquals("Melancholy May", player.getName());
		assertEquals(Color.blue, player.getColor());
		assertTrue(player.isHuman(player));
		assertEquals(9, player.getRow());
		assertEquals(10, player.getColumn());
	}

	//test a computer player for correct name, color, human/computer, and starting location
	@Test
	public void testMrGreen() {
		Player player = board.getPlayer("Mr. Green");

		assertEquals("Mr. Green", player.getName());
		assertEquals(Color.green, player.getColor());
		assertFalse(player.isHuman(player)); // assertFALSE: player is computer
		assertEquals(11, player.getRow());
		assertEquals(16, player.getColumn());
	}

	//test the last player in the config file for correct name, color, human/computer, and starting location
	@Test
	public void testClementine() {
		Player player = board.getPlayer("Clementine");

		assertEquals("Clementine", player.getName());
		assertEquals(Color.orange, player.getColor());
		assertFalse(player.isHuman(player)); // assertFALSE: player is computer
		assertEquals(13, player.getRow());
		assertEquals(16, player.getColumn());
	}
	
	/*
	 * Test loading deck of cards:
	 */

	//Test that the deck contains the correct total number of cards
	@Test
	public void testDeckSize() {
		Set<Card> deck = board.getDeck();
		int numCards = deck.size();
		assertEquals(DECK_SIZE, numCards);
	}
	
	//Test that the deck contains the correct number of each type of card (room/weapon/person)
	@Test
	public void testDeckTypes() {
		Set<Card> deck = board.getDeck();
		int numPersons = 0;
		int numWeapons = 0;
		int numRooms = 0;
		// Iterate through deck and keep track of each type of card
		for (Card card : deck) {
			if (card.isPerson())
				numPersons++;
			else if (card.isRoom())
				numRooms++;
			else if (card.isWeapon())
				numWeapons++;
		}
		assertEquals(6, numPersons); // 6 people
		assertEquals(6, numWeapons); // 6 weapons
		assertEquals(9, numRooms); // 9 rooms
	}
	
	// choose one room,one weapon,and one person,and test that the deck contains each of those (to test loading the names).
	@Test
	public void testEachType() {
		assertTrue(board.checkContainment("Bathroom")); // room
		assertTrue(board.checkContainment("Bat")); // weapon
		assertTrue(board.checkContainment("Miss Scarlet")); // person
	}
	
	/*
	 * Test dealing cards
	 */

	//Test that all the cards were dealt
	@Test
	public void testAllCardsDealt() {
		Map<String, Set<Card>> dealtCards = Board.getDealtCards();
		int totalDealtCards = 0;
		// Iterate through the dealt cards map
		for (Set<Card> cards : dealtCards.values()) {
			// Iterate through each set of cards in the map
			for (Card card : cards) {
				totalDealtCards++; // keep track of each card
			}
		}
		assertEquals(CARDS_TO_BE_DEALT, totalDealtCards);
	}

	// Test that all players have roughly the same amount of cards
	@Test
	public void testCardsPerPlayer() {
		Map<String, Set<Card>> dealtCards = Board.getDealtCards();
		ArrayList<Integer> cardsPerPlayer = new ArrayList<Integer>();
		// iterate through the dealtCards map and create a list of numbers representing the number of cards each player has
		for (Set<Card> cards : dealtCards.values()) {
			int temp = 0;
			for (Card card : cards) {
				temp++;
			}
			cardsPerPlayer.add(temp);
		}
		// find the largest difference between all the numbers of cards each person has
		int largestDiff = 0;
		for (int i = 0; i < cardsPerPlayer.size(); i++) {
			for (int j = 0; j < cardsPerPlayer.size(); j++) {
				largestDiff = Math.max(largestDiff, (cardsPerPlayer.get(i)-cardsPerPlayer.get(j)));
			}
		}
		// the difference should be no larger than 1 
		//if not enough cards for each player to have same amount, a player should only have 1 more/less than some other player
		assertTrue(largestDiff <= 1);
	}

	//Test that the same card was not given to more than one player
	@Test
	public void testNoDuplicateDeals() {
		Map<String, Set<Card>> dealtCards = Board.getDealtCards();
		// create new set to keep track of all the cards dealt
		Set<Card> cardsDealt = new HashSet<Card>();
		int duplicates = 0;
		// iterate through the dealtCards map
		for (Set<Card> cards : dealtCards.values()) {
			for (Card card : cards) {
				// if the new set we created already has the card, it means it's a duplicate
				if (cardsDealt.contains(card)) {
					duplicates++;
				} else { // if the new set doesn't contain the card, add the card to the new set
					cardsDealt.add(card);
				}
			}
		}
		// there should be no duplicates
		assertEquals(0, duplicates);
	}
}
