/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * This class gets the game board set up
 */

package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseListener{
	private int numRows;
	private int numColumns;

	private static final int MAX_BOARD_SIZE = 50;  // Max board dimensions

	private BoardCell[][] board;
	private Map<Character, String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private Map<String, Player> players = new LinkedHashMap<String, Player>();
	private ArrayList<Player> people = new ArrayList<Player>();
	private Set<Card> deck = new HashSet<Card>();
	private static Map<String, Set<Card>> dealtCards = new HashMap<String, Set<Card>>();
	private Solution solution;
	private Player currentPlayer;
	private int playerCurrentlyOn = 0;
	private Card disprovingCard = null;
	private boolean selected = false;
	private boolean test = true;  // flag for whether or not this run is for tests

	private String boardConfigFile;  // Grid of board game(csv file)
	private String roomConfigFile; // Key/legend for rooms(text file)
	private String playerConfigFile; // file with all players and their attributes
	private String weaponConfigFile; // file with all weapons

	private static Board theInstance = new Board(); // variable used for singleton pattern

	/*
	 *  constructor is private to ensure only one can be created
	 */
	private Board() {
		setPreferredSize(new Dimension(800, 800));
		addMouseListener(this);
	}

	/*
	 *  Gets the game set up 
	 */
	public void initializeClueGame(){	
		// catch exceptions for any of the three methods being called
		try {
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();
			loadPlayerConfig();
			loadWeaponConfig();
			loadRoomCards();
			dealDeck();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.getLocalizedMessage();
		}
	}

	/*
	 * This method has each board cell in the board draw themselves
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (BoardCell[] row : board) {
			for (BoardCell cell : row) {
				cell.drawCell(g);
			}
		}
	}

	/*
	 * Mouse Listeners
	 */
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if (currentPlayer.isHuman(currentPlayer)) { // if ts's the human players turn
			if (currentPlayer.hasAccused()) { // check whether or not the player has already made an accusation or not
				JOptionPane.showMessageDialog(null, "Your turn is over, you've made an accusation.");
			}else if (currentPlayer.isSuggested()) {  // check whether or not the player has already made a suggestion or not
				JOptionPane.showMessageDialog(null, "You've already moved and made a suggestion. You're turn is over.");
			}else {
				BoardCell cellClicked = null;
				int row = e.getY() / 30;
				int column = e.getX() / 30;
				BoardCell cell = getCellAt(row, column);
				if(cell.isTarget()) { // if the cell clicked is a target, move there
					cellClicked = cell;
					currentPlayer.move(cellClicked);
					currentPlayer.setFinished(true);
					if(cell.isRoom()) { // if the target clicked is a room, bring up the make a guess panel
						String room = cell.getRoomName();
						MakeAGuess guessPanel = new MakeAGuess(room);
						guessPanel.setVisible(true);
					}
					repaint();
				}else 
					JOptionPane.showMessageDialog(null, "Not a valid target. Please select a highlighted target.");
			}
		}else  // if it's not the human players turn, display an error message
			JOptionPane.showMessageDialog(null, "Please wait for your turn.");
	}

	/*
	 * this method loads in the room configurations from the cluerooms.txt file
	 */
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException  {
		legend = new HashMap<Character, String>();
		FileReader reader = new FileReader(roomConfigFile);
		Scanner in =  new Scanner(reader);

		while (in.hasNextLine()) {
			String line = in.nextLine(); // grab whole line
			String[] lineArray = line.split(", "); // split line by ,
			String roomChar = lineArray[0]; // roomChar is the first element
			String roomName = lineArray[1]; // roomName is the second
			String cardType = lineArray[2]; // roomType is the third
			char roomInitial = ' ';
			if (!(roomChar.length()>1))  //check for correct format of roomChar
				roomInitial = roomChar.charAt(0); 
			else throw new BadConfigFormatException(); 
			if (cardType.equalsIgnoreCase("Card") || cardType.equalsIgnoreCase("Other")) //check correct format for third field
				legend.put(roomInitial, roomName);
			else throw new BadConfigFormatException();
		}	
		in.close();
	}

	/*
	 * This method loads in the board configurations from the BoardLayout.csv file
	 */
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException {
		FileReader reader = new FileReader(boardConfigFile);
		Scanner in = new Scanner(reader);

		numRows = 0;
		numColumns = 0;	
		DoorDirection door;

		ArrayList<ArrayList<BoardCell>> tempBoard = new ArrayList<ArrayList<BoardCell>>();  //container to hold board while counting numRows/Columns
		ArrayList<BoardCell> tempRow; //container to hold cells while counting numRows/Columns
		int tempColumnCount[] = new int[MAX_BOARD_SIZE] ;
		while (in.hasNextLine()) {		//This loop dynamically allocates the cells to a 2D container and count Rows/ Columns
			int tempCount = 0;
			char initial;
			String line = in.nextLine();
			String[] cells = line.split(",");	// everything between commas is a new element
			tempRow = new ArrayList<BoardCell>();
			for (int i = 0; i< cells.length; i++) { // i represents the column number
				boolean displaysRoomName = false;
				if (cells[i].length() > 1) {		//check for cells with doors (cells with 2 chars)
					char temp = cells[i].charAt(1);
					if (temp == 'U')
						door = DoorDirection.UP;
					else if (temp == 'D') 
						door = DoorDirection.DOWN;
					else if (temp == 'L')
						door = DoorDirection.LEFT;
					else if (temp =='R')
						door = DoorDirection.RIGHT;
					// if it does have 2 chars, but is not a door
					else {
						door = DoorDirection.NONE;
						displaysRoomName = true;
					}
				} else { // if it doesn't have 2 characters, it is not a door
					door = DoorDirection.NONE;
				}
				initial = cells[i].charAt(0);
				if (!legend.containsKey(initial)) // if legend doesn't have the room initial, throw exception
					throw new BadConfigFormatException();
				BoardCell cell = new BoardCell (numRows, i, initial, door);
				cell.setRoomName(legend.get(initial));
				if (displaysRoomName == true)
					cell.setDisplaysRoomName(displaysRoomName);
				tempRow.add(cell); //add to temp Row container
				tempCount++;
			}
			tempColumnCount[numRows] = tempCount;
			tempBoard.add(tempRow); // add temp row to temp board container

			numColumns = cells.length; //set columns count
			numRows ++; //increase rows count
		}
		for(int j = 0; j < numRows - 1; j++) { // for each row check that the number of columns is the same
			if(tempColumnCount[j] != tempColumnCount[j + 1]) // if not, throw an exception
				throw new BadConfigFormatException("Each row doesn't have the same number of columns.");
		}
		in.close();
		board = new BoardCell[numRows][numColumns];		//declare real dimensions to board
		for (int i = 0; i< numRows; i++) {		// populate the actual board with the temp board container contents
			for (int j = 0; j< numColumns; j++) {
				BoardCell cell = tempBoard.get(i).get(j);
				board[i][j] = cell;
			}
		}
	}
	
	/*
	 * Loads each room in as a card and adds to the deck
	 */
	public void loadRoomCards() throws BadConfigFormatException, FileNotFoundException  {
		legend = new HashMap<Character, String>();
		FileReader reader = new FileReader(roomConfigFile);
		Scanner in =  new Scanner(reader);

		while (in.hasNextLine()) {
			String line = in.nextLine(); // grab whole line
			String[] lineArray = line.split(", "); // split line by ,
			String roomName = lineArray[1]; // roomName is the second
			String cardType = lineArray[2]; // roomType is the third
			if (cardType.equalsIgnoreCase("Card")){
				Card card = new Card(roomName, CardType.ROOM);
				deck.add(card);
			} else if (cardType.equalsIgnoreCase("other")) {
				continue;
			} else throw new BadConfigFormatException();
		}	
		in.close();
	}
	
	/*
	 * Loads in each player from player config file
	 */
	private void loadPlayerConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(playerConfigFile);
		Scanner in =  new Scanner(reader);
		int numPlayers = 0;
		int numHumanPlayers = 0;
		while (in.hasNextLine()) {
			String line = in.nextLine(); // grab whole line
			String[] lineArray = line.split(", "); // split line by ,
			String playerName = lineArray[0]; // player's name is the first element
			String color = lineArray[1]; // player's color is the second
			Color playerColor = convertColor(color); // convert the string "color" into a java color
			String playerType = lineArray[2]; //playerType says whether the player is a human or a computer
			String row = lineArray[3]; // player's starting position (row)
			int startRow = Integer.parseInt(row); // convert to an int
			String column = lineArray[4]; // player's starting position (column)
			int startColumn = Integer.parseInt(column); // convert to an int
			if (playerType.equalsIgnoreCase("Human")) { // if human player, create human player
				Player humanPlayer = new HumanPlayer(playerName, startRow, startColumn, playerColor);
				players.put(playerName, humanPlayer);	
				people.add(humanPlayer);
				numHumanPlayers += 1;
				BoardCell cell = getCellAt(startRow, startColumn);
				cell.addPlayerOnCell(humanPlayer);
			} else if (playerType.equalsIgnoreCase("Computer")) { // if computer player, create computer player
				Player computerPlayer = new ComputerPlayer(playerName, startRow, startColumn, playerColor);
				players.put(playerName, computerPlayer);
				people.add(computerPlayer);
				BoardCell cell = getCellAt(startRow, startColumn);
				cell.addPlayerOnCell(computerPlayer);
			} else throw new BadConfigFormatException();
			// create a card for the person
			Card card = new Card(playerName, CardType.PERSON);
			// add card to the deck
			deck.add(card);
			numPlayers++;
		}	
		if (numPlayers > 6) // total number of players should not exceed 6
			throw new BadConfigFormatException();
		if (numHumanPlayers > 1) // there should only be 1 human player
			throw new BadConfigFormatException();
		in.close();
	}
	
	/*
	 * Loads in each weapon from weapon config file
	 */
	private void loadWeaponConfig() throws FileNotFoundException, BadConfigFormatException {
		FileReader reader = new FileReader(weaponConfigFile);
		Scanner in =  new Scanner(reader);
		int numWeapons = 0;
		while (in.hasNextLine()) {
			String weapon = in.nextLine();
			// create card for the weapon
			Card card = new Card(weapon, CardType.WEAPON);
			// add the card to the deck
			deck.add(card);
			numWeapons++;
		}
		if (numWeapons > 6) // there should be no more than 6 weapons total
			throw new BadConfigFormatException();
	}

	/*
	 *  this method calculates all adjacencies for each cell
	 */
	public void calcAdjacencies() {
		//for each cell
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numColumns; column++) {
				BoardCell currCell = board[row][column];
				Set<BoardCell> adjList = new HashSet<BoardCell>();
				Set<BoardCell> otherDoorsInRoom = new HashSet<BoardCell>();
				// check current cell
				if (currCell.isDoorway()) { // if a doorway
					if (!test) {
						//find other doorways in room
						for (BoardCell[] cellRow : board) {
							for (BoardCell cell : cellRow) 
								if (cell.getRoomName().equals(currCell.getRoomName())) 
									otherDoorsInRoom.add(cell);
							if (!otherDoorsInRoom.isEmpty()) 
								adjList = checkOtherDoorsInRoom(adjList, otherDoorsInRoom);
						}
					}
					// add the cell(if its a walkway) in the correct door direction to adjList
					DoorDirection direction = currCell.getDoorDirection();
					checkForDoorDirection(row, column, adjList, direction);
				} else if (currCell.isWalkway()) { // if a walkway
					// look at each neighbor
					// if valid, add to adjList
					if (checkNeighborOfWalkway(row - 1, column, DoorDirection.DOWN)) //check cell above
						adjList.add(board[row - 1][column]);
					if (checkNeighborOfWalkway(row + 1, column, DoorDirection.UP)) //check cell below
						adjList.add(board[row + 1][column]);
					if (checkNeighborOfWalkway(row, column - 1, DoorDirection.RIGHT)) //check cell to the left
						adjList.add(board[row][column - 1]);
					if (checkNeighborOfWalkway(row, column + 1, DoorDirection.LEFT)) //check cell to the right
						adjList.add(board[row][column + 1]);
				} 
				adjMatrix.put(currCell, adjList);
			}
		}
	}
	
	/*
	 * Checks for adjacencies from other doorways in the same room
	 */
	public Set<BoardCell> checkOtherDoorsInRoom(Set<BoardCell> adjList, Set<BoardCell> otherDoorsInRoom) {
		for (BoardCell door : otherDoorsInRoom) {
			DoorDirection direction = door.getDoorDirection();
			int row = door.getRow();
			int column = door.getColumn();
			checkForDoorDirection(row, column, adjList, direction);
			if (adjList.contains(door)) 
				adjList.remove(door);
		}
		return adjList;
	}

	/*
	 * Helper function for calc adjacences and check other doors in room
	 * checks for the door direction and adds the correct adj cell to the adj list
	 */
	private void checkForDoorDirection(int row, int column, Set<BoardCell> adjList, DoorDirection direction) {
		// if door direction is up, look at the cell above it
		if (direction == DoorDirection.UP) {
			if (checkNeighborOfDoor(row - 1, column))
				adjList.add(board[row - 1][column]);
		} // if door direction is down, look at the cell below it
		else if (direction == DoorDirection.DOWN) {
			if (checkNeighborOfDoor(row + 1, column))
				adjList.add(board[row + 1][column]);
		} // if door direction is left, look at the cell to the left of it
		else if (direction == DoorDirection.LEFT) {
			if (checkNeighborOfDoor(row , column - 1))
				adjList.add(board[row][column - 1]);
		} // if door direction is right, look at the cell to the right of it
		else if (direction == DoorDirection.RIGHT) {
			if (checkNeighborOfDoor(row, column + 1))
				adjList.add(board[row][column + 1]);
		}
	}

	/*
	 *  helper function for calcAdjacencies
	 *  add valid neighbors to walkway's adj list
	 */
	private boolean checkNeighborOfWalkway(int row, int column, DoorDirection direction) {
		if ((row) < numRows && (row) >= 0 && (column) < numColumns && (column) >= 0) { // confirm that cell is in board constraints
			BoardCell neighbor = board[row][column];
			if (neighbor.isWalkway()) // if it's a walkway, add to adj list
				return true;
			else if (neighbor.isDoorway()) {
				if (neighbor.getDoorDirection() == direction) // if it's a door check that it's in the right direction
					return true;
			}
		}
		return false;
	}

	/*
	 *  helper function for calcAdjacencies
	 *  confirm neighbor of door is walkway 
	 */
	private boolean checkNeighborOfDoor(int row, int column) {
		if ((row) < numRows && (row) >= 0 && (column) < numColumns && (column) >= 0) {  // confirm that cell is in board constraints
			BoardCell neighbor = board[row][column];
			if (neighbor.isWalkway()) // confirm that it is a walkway
				return true;
		}
		return false;
	}

	/*
	 *  this method calculates all valid targets for the cell currently on and the amount moving(roll of die)
	 */
	public void calcTargets(int row, int column, int pathLength) {
		targets.clear();
		visited.clear();
		calcTargetsHelper(row, column, pathLength);
	}

	/*
	 *  helper function for calcTargets
	 */
	private void calcTargetsHelper(int row, int column, int pathLength) {
		BoardCell cell = getCellAt(row, column);
		visited.add(cell);
		Set<BoardCell> adjList = getAdjList(row, column);
		// for each cell in adjacent cells
		for (BoardCell adjCell : adjList) {
			// if already in visited list, skip rest
			if (!visited.contains(adjCell)) {
				//add adjcell to visited list
				visited.add(adjCell);
				// if the current cell is a doorway(meaning you are in a room)
				if (cell.isDoorway()) {
					// check that the adjcell is a walkway
					if (adjCell.isWalkway())
						checkPathLength(pathLength, adjCell);
				}
				// if adjcell is a doorway, add it to targets
				else if (adjCell.isDoorway()) 
					targets.add(adjCell);
				else
					checkPathLength(pathLength, adjCell);
				//remove adjcell from visited list
				visited.remove(adjCell);
			}
		}		
	}

	/*
	 * Helper function for calcTargets
	 * if path length is not 1, call calcTargets with adjcell row and column and pathlenth - 1
	 */
	private void checkPathLength(int pathLength, BoardCell adjCell) {
		if (pathLength ==1) {
			if (adjCell.getPlayerOnCell().isEmpty()) 
				targets.add(adjCell);
		}else if (adjCell.getPlayerOnCell().isEmpty()) {
			int adjCellRow = adjCell.getRow();
			int adjCellColumn = adjCell.getColumn();
			calcTargetsHelper(adjCellRow, adjCellColumn, pathLength - 1);
		}
	}

	/*
	 * Creates solution and then
	 * deals out the rest of the cards to the players
	 */
	public void dealDeck() {
		int deckSize = deck.size() - 3;
		int playerNum = 0;
		// Make a list to keep track of cards that have not been dealt yet
		ArrayList<Card> toBeDealt = new ArrayList<Card>();
		for (Card card : deck) {
			toBeDealt.add(card);
		}
		// generates a solution and updates the toBeDealt deck with no solution cards
		ArrayList<Card >updatedToBeDealt = createSolution(toBeDealt);
		// start to deal the cards
		int initialize = 0;
		for (int i = 0; i < deckSize; i++) {
			// get a random card from the list of cards that still need to be dealt
			Card dealCard = getRandomElement(updatedToBeDealt);
			// select the player to deal the card to 
			Player player = (Player) players.values().toArray()[playerNum];
			// if you get to the last player, reset to the first player
			playerNum = (playerNum + 1) % players.size();
			// if NOT all players have been dealt at least 1 card, initialize a set to hold their cards
			if (initialize < players.size()) {
				Set<Card> cards = new HashSet<Card>();
				cards.add(dealCard);
				dealtCards.put(player.getName(), cards);
			}else { // if all players have been dealt at least 1 card, just add to their set of cards
				Set<Card> cards = dealtCards.get(player.getName());
				cards.add(dealCard);
				dealtCards.put(player.getName(), cards);
			}
			// remove the card just dealt from the list of cards that still need to be dealt
			updatedToBeDealt.remove(dealCard);
			initialize++;
		}
		// set each player's hand
		for (Map.Entry<String, Set<Card>> hand : dealtCards.entrySet()) {
			String playerName = hand.getKey();
			Set<Card> playerHand = hand.getValue();
			Player player = players.get(playerName);
			player.setHand(playerHand);
		}
		// set each player's "not seen" hand
		for (Map.Entry<String, Player> curPlayer : players.entrySet()) {
			Player player = curPlayer.getValue();
			Set<Card> notSeenHand = removeSeenCard(player.getHand());
			player.setNotSeenCards(notSeenHand);
		}
	}

	/*
	 * Helper method for dealDeck
	 * Removes the cards the player has in their from their notSeenCards hand.
	 */
	private Set<Card> removeSeenCard(Set<Card> playerHand) {
		Set<Card> notSeenCards = new HashSet<Card>();
		for (Card card : deck) {
			notSeenCards.add(card);
		}
		for (Card card : playerHand) {
			notSeenCards.remove(card);
		}
		return notSeenCards;
	}
	
	/*
	 * randomly sets aside 1 person, 1 weapon, and 1 room for the solution
	 * and remove those cards from the toBeDealt deck (they should not be dealt)
	 */
	private ArrayList<Card> createSolution(ArrayList<Card> toBeDealt) {
		// create Array lists to hold each type of card
		ArrayList<Card> playerCards = new ArrayList<Card>();
		ArrayList<Card> weaponCards = new ArrayList<Card>();
		ArrayList<Card> roomCards = new ArrayList<Card>();
		// generate those lists with the deck
		for (Card card : deck) {
			if (card.getType() == CardType.PERSON) 
				playerCards.add(card);
			else if (card.getType() == CardType.WEAPON)
				weaponCards.add(card);
			else if (card.getType() == CardType.ROOM)
				roomCards.add(card);
		}
		// randomly choose a card from each type
		Card playerCard = getRandomElement(playerCards);
		Card weaponCard = getRandomElement(weaponCards);
		Card roomCard = getRandomElement(roomCards);
		// create the solution
		solution = new Solution(playerCard.getCardName(), roomCard.getCardName(), weaponCard.getCardName());
		// remove the cards chosen for the solution from the toBeDealt deck
		toBeDealt.remove(playerCard);
		toBeDealt.remove(weaponCard);
		toBeDealt.remove(roomCard);
		return toBeDealt;
	}

	/*
	 * When a suggestion is made, this method goes through each player and sees if they can disprove or not
	 */
	public Card handleSuggestion(Suggestion suggestion, ArrayList<Player> players, Player suggestingPlayer) {
		int suggestingPlayerIndex = 0;
		boolean found = false;
		// find the index of the player who made the suggestion
		for (int index = 0; index < players.size(); index++) {
			if (found == false) {
				if (players.get(index).getName().equals(suggestingPlayer.getName())) {
					suggestingPlayerIndex = index;
					found = true;
				}
			}
		}
		// go through each player, in order and see if someone can disprove
		for(int j = suggestingPlayerIndex +1; j < players.size(); j ++) {
			Player disprovingPlayer = players.get(j);
			Card disprovingCard = disprovingPlayer.disproveSuggestion(suggestion);
			if (disprovingCard != null)
				return disprovingCard;
		} // if at the end of the list of players, and the suggesting player wasn't first on the list, 
		//then continue at the top of the list of people
		if (suggestingPlayerIndex != 0) {
			for (int i = 0; i < suggestingPlayerIndex; i++) {
				Player disprovingPlayer = players.get(i);
				Card disprovingCard = disprovingPlayer.disproveSuggestion(suggestion);
				if (disprovingCard != null)
					return disprovingCard;
			}
		}
		return null;
	}

	/*
	 * Compares the accusation to the solution and if everything (person, weapon, and room) 
	 * matches, then it returns true. Otherwise, if one thing doesn't match return false.
	 */
	public boolean checkAccusation(Suggestion accusation) {
		// compare accusation to solution
		if (solution.getPerson().equals(accusation.getPerson())) {
			if (solution.getWeapon().equals(accusation.getWeapon())) {
				if (solution.getRoom().equals(accusation.getRoom())) {
					// if all three match, return true
					return true;
				}
			}
		} 
		// else, return false
		return false;
	}

	/*
	 * Helper function for loadPlayerConfig to load in colors for each player (given to us)
	 */
	public Color convertColor(String strColor) {
		Color color; try {
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim()); 
			color = (Color)field.get(null);
		} catch (Exception e) {
			color = null; // Not defined
		}
		return color; 
	}

	/*
	 * Helper function for dealDeck and createSolution
	 * Selects a random card from the ArrayList of cards passed in
	 */
	public Card getRandomElement(ArrayList<Card> cards) 
	{ 
		Random rand = new Random(); 
		return cards.get(rand.nextInt(cards.size())); 
	} 

	/*
	 * For testing purposes
	 * Checks if the deck contains a certain card
	 */
	public boolean checkContainment(String string) {
		for (Card card : deck) {
			String name = card.getCardName();
			if (name.equals(string))
				return true;
		}
		return false;
	}

	/*
	 * Getters and setters:
	 */

	/*
	 *  this method sets all the config files
	 */
	public void setConfigurationFiles(String boardLayout, String rooms, String players, String weapons) {
		boardConfigFile = boardLayout;
		roomConfigFile = rooms;
		playerConfigFile = players;
		weaponConfigFile = weapons;
	}

	/*
	 * this method returns the only Board
	 */
	public static Board getInstance() {
		return theInstance;
	}

	/*
	 * Sets the starting player (Human)
	 */
	public void setStartingPlayer(Player curPlayer) {
		currentPlayer = curPlayer;
	}

	/*
	 * Sets the current player
	 */
	public void setCurrentPlayer() {
		if (playerCurrentlyOn == players.size() - 1) {
			playerCurrentlyOn = 0;
		}else 
			playerCurrentlyOn++;
		currentPlayer = people.get(playerCurrentlyOn);
	}

	public Map<Character, String> getLegend() {
		return legend;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	// returns the cell for a given row/column
	public BoardCell getCellAt(int row, int column) {
		return board[row][column];
	}

	// returns adj list for a given cell
	public Set<BoardCell> getAdjList(int i, int j) {
		BoardCell cell = getCellAt(i, j);
		return adjMatrix.get(cell);
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public Map<String, Player> getPlayers() {
		return players;
	}
	
	// returns the human player
	public Player getHumanPlayer() {
		for (Player player : players.values()) {
			if (player.isHuman(player)) 
				return player;
		}
		return null;
	}
	
	// returns the human player's name
	public String getHumanPlayerString() {
		for (Player player : players.values()) {
			if (player.isHuman(player)) 
				return player.getName();
		}
		return null;
	}

	public Set<Card> getDeck() {
		return deck;
	}

	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	public static Map<String, Set<Card>> getDealtCards() {
		return dealtCards;
	}

	public Solution getSolution() {
		return solution;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public ArrayList<Player> getPeople() {
		return people;
	}
	
	public void setDisprovingCard(Card card) {
		this.disprovingCard = card;
	}
	
	public String getDisprovingCard() {
		if (disprovingCard != null) 
			return disprovingCard.toString();
		else return null;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public BoardCell[][] getBoard() {
		return board;
	}
	
	public void setTest(boolean test) {
		this.test = test;
	}

	/*
	 * Set up for testing purposes:
	 */
	
	// Gets the board game set up (for testing purposes)
	public void initialize(){	
		// catch exceptions for any of the three methods being called
		try {
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.getLocalizedMessage();
		}
	}

	// Gets the game setup (for testing purposes)
	public void loadConfigFiles(String players, String weapons, String rooms) {
		playerConfigFile = players;
		weaponConfigFile = weapons;
		roomConfigFile = rooms;
		try {
			loadPlayerConfig();
			loadWeaponConfig();
			loadRoomCards();
			dealDeck();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.getLocalizedMessage();
		}
	}
	
	/*
	 *  this method sets the config files (for testing purposes)
	 */
	public void setConfigFiles(String boardLayout, String rooms) {
		boardConfigFile = boardLayout;
		roomConfigFile = rooms;
	}
}