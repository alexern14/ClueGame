/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * Tests:
 * -selecting a target location (computer player)
 * -checking an accusation (board)
 * -disproving a suggestion (player)
 * -handling a suggestion (board)
 * -creating a suggestion (computer player)
 */

package tests;

import static org.junit.Assert.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;
import clueGame.Suggestion;

public class gameActionTests {

	private static Board board;

	// Sets up the board and cards needed for testing
	@BeforeClass
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("BoardLayout.csv", "ClueRooms.txt");	
		board.initialize();
		board.loadConfigFiles("Players.txt", "Weapons.txt", "ClueRooms.txt");
	}

	/*
	 * Test selecting a target location (computer player):
	 */

	// if no rooms in list, select randomly
	@Test
	public void testTargetNoRooms() {
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 7, 11, Color.red);
		// location with no rooms in target
		board.calcTargets(7, 11, 2);
		boolean loc_7_13 = false;
		boolean loc_8_10 = false;
		boolean loc_7_9 = false;
		boolean loc_6_10 = false;
		boolean loc_8_12 = false;
		// run test a large amount of times
		for (int i = 0; i < 100; i ++) {
			BoardCell selected = player.pickLocation(board.getTargets()); 
			if (selected == board.getCellAt(7, 13))
				loc_7_13 = true;
			else if (selected == board.getCellAt(8, 10))
				loc_8_10 = true; 
			else if (selected == board.getCellAt(7, 9))
				loc_7_9 = true;
			else if (selected == board.getCellAt(6, 10))
				loc_6_10 = true;
			else if (selected == board.getCellAt(8, 12))
				loc_8_12 = true;
			else 
				fail("Invalid target selected");
		}
		// ensure each target was selected at least once
		assertTrue(loc_7_13); 
		assertTrue(loc_8_10); 
		assertTrue(loc_7_9);
		assertTrue(loc_6_10);
		assertTrue(loc_8_12);
	}
	
	// if room in list that was not just visited, must select it
	@Test
	public void testTargetRoomNotJustVisited() {
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 16, 22, Color.red);
		// location with room in target (that was not just visited)
		board.calcTargets(16, 22, 2);
		boolean roomChosen = false;
		BoardCell selected = player.pickLocation(board.getTargets()); 
		if (selected == board.getCellAt(18, 22))
			roomChosen = true;
		assertTrue(roomChosen);
	}
	
	// if room just visited is in list, each target (including room) selected randomly
	@Test
	public void testTargetRoomJustVisited() {
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 17, 5, Color.red);
		// location with room in list (that was just visited)
		board.calcTargets(17, 5, 3);
		boolean loc_17_4 = false;
		boolean loc_16_3 = false;
		boolean loc_15_4 = false;
		boolean loc_15_6 = false;
		boolean loc_16_7 = false;
		boolean loc_17_6 = false;
		// run test a large amount of times
		for (int i = 0; i < 100; i ++) {
			BoardCell selected = player.pickLocation(board.getTargets()); 
			if (selected == board.getCellAt(17, 4))
				loc_17_4 = true;
			else if (selected == board.getCellAt(16, 3))
				loc_16_3 = true; 
			else if (selected == board.getCellAt(15, 4))
				loc_15_4 = true;
			else if (selected == board.getCellAt(15, 6))
				loc_15_6 = true;
			else if (selected == board.getCellAt(16, 7))
				loc_16_7 = true;
			else if (selected == board.getCellAt(17, 6))
				loc_17_6 = true;
			else 
				fail("Invalid target selected");
		}
		// ensure each target was selected at least once
		assertTrue(loc_17_4); 
		assertTrue(loc_16_3); 
		assertTrue(loc_15_4);
		assertTrue(loc_15_6);
		assertTrue(loc_16_7);
		assertTrue(loc_17_6);
	}
	
	/*
	 * Test checking an accusation (board):
	 */
	
	// solution that is correct
	@Test
	public void testAccusationCorrect() {
		// get the solution
		Solution solution = board.getSolution();
		// create an accusation based on the solution
		Suggestion accusation = new Suggestion(solution.getPerson(), solution.getRoom(), solution.getWeapon());
		assertTrue(board.checkAccusation(accusation));
	}
	
	// solution with wrong person
	@Test
	public void testAccusationWrongPerson() {
		// get solution
		Solution solution = board.getSolution();
		String person = "Clementine";
		if (solution.getPerson() == person) // if solution has Clementine then change for accusation
			person = "Pofessor Plum";
		// create accusation with the wrong person
		Suggestion accusation = new Suggestion(person, solution.getRoom(), solution.getWeapon());
		assertFalse(board.checkAccusation(accusation));
	}
	
	// solution with wrong weapon
	@Test
	public void testAccusationWrongWeapon() {
		// get solution
		Solution solution = board.getSolution();
		String weapon = "Knife";
		if (solution.getPerson() == weapon) // if solution has knife, change for accusation
			weapon = "Bat";
		// create accusation with the wrong weapon
		Suggestion accusation = new Suggestion(solution.getPerson(), solution.getRoom(), weapon);
		assertFalse(board.checkAccusation(accusation));
	}
	
	// solution with wrong room
	@Test
	public void testAccusationWrongRoom() {
		// get solution
		Solution solution = board.getSolution();
		String room = "Office";
		if (solution.getPerson() == room) // if solution has office, change for accusation
			room = "Kitchen";
		// create accusation with wrong room
		Suggestion accusation = new Suggestion(solution.getPerson(), room, solution.getWeapon());
		assertFalse(board.checkAccusation(accusation));
	}
	
	/*
	 * Test disproving a suggestion (player):
	 */
	
	// If player has only one matching card it should be returned
	@Test
	public void testOneMatchingCard() {
		// create a player and their hand
		Player player = new Player("Mr. Green", 7, 25, Color.green);
		Card card1 = new Card("Axe", CardType.WEAPON);
		Card card2 = new Card("Bat", CardType.WEAPON);
		Card card3 = new Card("Family room", CardType.ROOM);
		Set<Card> hand = new HashSet<Card>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);
		player.setHand(hand);
		// make suggestion that matches 1 of the player's cards
		Suggestion suggestion = new Suggestion("Clementine", "Garage", "Bat");
		Card card = player.disproveSuggestion(suggestion);
		if (card == null)
			fail();
		assertEquals("Bat", card.getCardName());
	}
	
	// If players has >1 matching card, returned card should be chosen randomly
	@Test
	public void testMultipleMatchingCards() {
		// create a player and their hand
		Player player = new Player("Mr. Green", 7, 25, Color.green);
		Card card1 = new Card("Axe", CardType.WEAPON);
		Card card2 = new Card("Bat", CardType.WEAPON);
		Card card3 = new Card("Family room", CardType.ROOM);
		Set<Card> hand = new HashSet<Card>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);
		player.setHand(hand);
		// make suggestion that matches 2 of the player's cards
		Suggestion suggestion = new Suggestion("Clementine", "Family room", "Bat");
		boolean familyRoom = false;
		boolean bat = false;
		for (int i = 0; i < 10; i++) {
			Card card = player.disproveSuggestion(suggestion);
			if (card.getCardName() == "Family room")
				familyRoom = true;
			else if (card.getCardName() == "Bat")
				bat = true;
			else 
				fail();
		}
		assertTrue(familyRoom);
		assertTrue(bat);
	}
	
	// If player has no matching cards, null is returned
	@Test
	public void testNoMatchingCards() {
		// create a player and their hand
		Player player = new Player("Mr. Green", 7, 25, Color.green);
		Card card1 = new Card("Axe", CardType.WEAPON);
		Card card2 = new Card("Bat", CardType.WEAPON);
		Card card3 = new Card("Family room", CardType.ROOM);
		Set<Card> hand = new HashSet<Card>();
		hand.add(card1);
		hand.add(card2);
		hand.add(card3);
		player.setHand(hand);
		// create a suggestion that does not match any of the player's cards
		Suggestion suggestion = new Suggestion("Clementine", "Garage", "Pistol");
		Card card = player.disproveSuggestion(suggestion);
		assertTrue(card == null);
	}
	
	/*
	 * Test handling a suggestion (board):
	 */
	// set up for next set of tests
	private static Player plumPlayer;
	private static Player clementiinePlayer;
	private static Player scarletPlayer;
	private static Card mustardCard;
	private static Card axeCard;
	private static Card officeCard;
	private static Card garageCard;
	private static Card mrGreenCard;
	private static Card batCard;
	private static Map<Player, Set<Card>> dealtCards = new HashMap<Player, Set<Card>>();
	private static Set<Card> plumCards = new HashSet<Card>();
	private static Set<Card> clementineCards = new HashSet<Card>();
	private static Set<Card> scarletCards = new HashSet<Card>();
	private static ArrayList<Player> players = new ArrayList<Player>();

	@BeforeClass
	public static void setUpHandling() {
		board.initialize();
		plumPlayer = new HumanPlayer("Professor Plum", 6, 10, Color.magenta);
		clementiinePlayer = new ComputerPlayer("Clementine", 4, 6, Color.orange);
		scarletPlayer = new ComputerPlayer("Miss Scarlet", 13, 8, Color.red);
		mustardCard = new Card("Colonel Mustard", CardType.PERSON);
		axeCard = new Card("Axe", CardType.WEAPON);
		officeCard = new Card("Office", CardType.ROOM);
		garageCard = new Card("Garage", CardType.ROOM);
		mrGreenCard = new Card("Mr. Green", CardType.PERSON);
		batCard = new Card("Bat", CardType.WEAPON);
		plumCards.add(mrGreenCard);
		plumCards.add(garageCard);
		clementineCards.add(officeCard);
		clementineCards.add(axeCard);
		scarletCards.add(batCard);
		scarletCards.add(mustardCard);
		dealtCards.put(plumPlayer, plumCards);
		dealtCards.put(clementiinePlayer, clementineCards);
		dealtCards.put(scarletPlayer, scarletCards);
		players.add(plumPlayer);
		players.add(clementiinePlayer);
		players.add(scarletPlayer);
		plumPlayer.setHand(plumCards);
		clementiinePlayer.setHand(clementineCards);
		scarletPlayer.setHand(scarletCards);
	}
	
	// Suggestion no one can disprove returns null
	@Test
	public void testNoOneCanDisprove() {
		// create suggestion that matches none of the player's cards
		Suggestion suggestion = new Suggestion("Melancholy May", "Bathroom", "Rope");
		// plum is the suggesting player
		Card card = board.handleSuggestion(suggestion, players, plumPlayer);
		assertEquals(null, card);
	}
	
	// Suggestion only accusing player can disprove returns null
	@Test
	public void testOnlyAccusingPlayerDisproves() {
		// create suggestion that matches only 1 of the accusing player's cards
		Suggestion suggestion = new Suggestion("Melancholy May", "Office", "Rope");
		// clementine is the suggesting player
		Card card = board.handleSuggestion(suggestion, players, clementiinePlayer);
		assertEquals(null, card);
	}
	
	// Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
	@Test
	public void testOnlyHumanDisproves() {
		// create suggestion that matches only the human's cards
		Suggestion suggestion = new Suggestion("Melancholy May", "Garage", "Rope");
		// clementine is the suggesting player
		Card card = board.handleSuggestion(suggestion, players, clementiinePlayer);
		assertEquals("Garage", card.getCardName());
	}
	
	// Suggestion only human can disprove, but human is accuser, returns null
	@Test
	public void testOnlyHumanDisprovesButIsAccuser() {
		// create suggestion the matches only the human's cards, who is also the accuser
		Suggestion suggestion = new Suggestion("Mr. Green", "Bathroom", "Rope");
		// plum is the suggesting player
		Card card = board.handleSuggestion(suggestion, players, plumPlayer);
		assertEquals(null, card);
	}
	
	// Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
	@Test
	public void testTwoPlayersCanDisprove() {
		// create suggestion that matches 2 players cards
		Suggestion suggestion = new Suggestion("Colonel Mustard", "Bathroom", "Axe");
		// plum is the suggesting player
		Card card = board.handleSuggestion(suggestion, players, plumPlayer);
		assertEquals("Axe", card.getCardName());
	}
	
	// Suggestion that human and another player can disprove, other player is next in list, ensure other player returns answer
	@Test
	public void testHumanAndAnotherDisprove() {
		// create suggestion that matches 2 players cards, 1 being human, but other player disproves before human does
		Suggestion suggestion = new Suggestion("Melancholy May", "Garage", "Bat");
		// clementine is the suggesting player
		Card card = board.handleSuggestion(suggestion, players, clementiinePlayer);
		assertEquals("Bat", card.getCardName());
	}
	
	/*
	 * Test creating a suggestion (computer player):
	 */
	
	// Room matches current location
	@Test
	public void testSuggestionCurrentRoom() {
		Set<Card> deck = board.getDeck();
		Set<Card> notSeenCards = new HashSet<Card>();
		for (Card card : deck) {
			notSeenCards.add(card);
		}
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 3, 15, Color.red);
		player.setNotSeenCards(notSeenCards);
		Suggestion playerSuggestion = player.createSuggestion(); 
		BoardCell curCell = board.getCellAt(player.getRow(), player.getColumn());
		String room = curCell.getRoomName(); 
		if (room != playerSuggestion.getRoom()) {
			fail();
		}
	}
	
	// If only one weapon not seen, it's selected
	@Test
	public void testSuggestionOneWeaponNotSeen() {
		Set<Card> deck = board.getDeck();
		int numWeapons = 0;
		Card weapon = null;
		// create a hand of not seen cards with only 1 weapon
		Set<Card> notSeenCards = new HashSet<Card>();
		for (Card card : deck) {
			if (card.getType() == CardType.WEAPON && numWeapons == 0) {
				notSeenCards.add(card);
				weapon = card;
				numWeapons++;
			} else if (card.getType() == CardType.PERSON)
				notSeenCards.add(card);
			else if (card.getType() == CardType.ROOM)
				notSeenCards.add(card);
		}
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 3, 15, Color.red);
		player.setNotSeenCards(notSeenCards);
		Suggestion playerSuggestion = player.createSuggestion();
		String weaponString = weapon.getCardName();
		assertTrue(weaponString == playerSuggestion.getWeapon());
	}
	
	// If only one person not seen, it's selected (can be same test as weapon)
	@Test
	public void testSuggestionOnePersonNotSeen() {
		Set<Card> deck = board.getDeck();
		int numPersons = 0;
		Card person = null;
		// create a hand of not seen cards with only 1 person
		Set<Card> notSeenCards = new HashSet<Card>();
		for (Card card : deck) {
			if (card.getType() == CardType.PERSON && numPersons == 0) {
				notSeenCards.add(card);
				person = card;
				numPersons++;
			} else if (card.getType() == CardType.WEAPON)
				notSeenCards.add(card);
			else if (card.getType() == CardType.ROOM)
				notSeenCards.add(card);
		}
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 3, 15, Color.red);
		player.setNotSeenCards(notSeenCards);
		Suggestion playerSuggestion = player.createSuggestion();
		String personString = person.getCardName();
		assertTrue(personString == playerSuggestion.getPerson());
	}
	
	// If multiple weapons not seen, one of them is randomly selected
	@Test
	public void testSuggestionMultipleWeaponsNotSeen() {
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 3, 15, Color.red);
		Set<Card> deck = board.getDeck();
		int numWeapons = 0;
		Set<Card> weapon = new HashSet<Card>();
		// create a hand of not seen cards with multiple weapons
		Set<Card> notSeenCards = new HashSet<Card>();
		for (Card card : deck) {
			if (card.getType() == CardType.WEAPON && numWeapons < 3) {
				notSeenCards.add(card);
				weapon.add(card);
				numWeapons++;
			} else if (card.getType() == CardType.PERSON)
				notSeenCards.add(card);
			else if (card.getType() == CardType.ROOM)
				notSeenCards.add(card);
		}
		player.setNotSeenCards(notSeenCards);
		// keep track of all weapons in hand of not seen cards
		String[] weapons = new String[3];
		int i = 0;
		for (Card card : weapon) {
			weapons[i] = card.getCardName();
			i++;
		}
		boolean weapon1 = false;
		boolean weapon2 = false;
		boolean weapon3 = false;
		// run test a large amount of times
		for (int j = 0; j < 50; j ++) {
			Suggestion playerSuggestion = player.createSuggestion();
			if (playerSuggestion.getWeapon() == weapons[0])
				weapon1 = true;
			else if (playerSuggestion.getWeapon() == weapons[1])
				weapon2 = true;
			else if (playerSuggestion.getWeapon() == weapons[2])
				weapon3 = true;
			else 
				fail();
		}
		// ensure that each weapon was chosen randomly
		assertTrue(weapon1);
		assertTrue(weapon2);
		assertTrue(weapon3);
	}
	
	// If multiple persons not seen, one of them is randomly selected
	@Test
	public void testSuggestionMutiplePersonsNotSeen() {
		ComputerPlayer player = new ComputerPlayer("Miss Scarlet", 3, 15, Color.red);
		Set<Card> deck = board.getDeck();
		int numPersons = 0;
		Set<Card> players = new HashSet<Card>();
		// create a hand of not seen cards with multiple people
		Set<Card> notSeenCards = new HashSet<Card>();
		for (Card card : deck) {
			if (card.getType() == CardType.PERSON && numPersons < 3) {
				notSeenCards.add(card);
				players.add(card);
				numPersons++;
			} else if (card.getType() == CardType.WEAPON)
				notSeenCards.add(card);
			else if (card.getType() == CardType.ROOM)
				notSeenCards.add(card);
		}
		player.setNotSeenCards(notSeenCards);
		// keep track of all the people in hand of not seen cards
		String[] playerStrings = new String[3];
		int i = 0;
		for (Card card : players) {
			playerStrings[i] = card.getCardName();
			i++;
		}
		boolean person1 = false;
		boolean person2 = false;
		boolean person3 = false;
		// run test a large amount of times
		for (int j = 0; j < 50; j ++) {
			Suggestion playerSuggestion = player.createSuggestion();
			if (playerSuggestion.getPerson() == playerStrings[0])
				person1 = true;
			else if (playerSuggestion.getPerson() == playerStrings[1])
				person2 = true;
			else if (playerSuggestion.getPerson() == playerStrings[2])
				person3 = true;
			else 
				fail();
		}
		// ensure that each person was chosen randomly
		assertTrue(person1);
		assertTrue(person2);
		assertTrue(person3);
	}
}