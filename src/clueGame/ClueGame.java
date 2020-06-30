/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * This class holds the main functionality of the game
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGame extends JFrame{
	private JFrame frame;
	private DetectiveNotes notes = new DetectiveNotes();
	private static Board board;
	private ControlGUI controlGUI;
	private static boolean isGameOver = false; // flag for whether or not the game is over (someone has won)
	
	/*
	 * Constructor
	 */
	public ClueGame() {
		setUp();
		setTitle("Clue Game");
		setSize(new Dimension(1000, 1000));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(board, BorderLayout.CENTER);
		CardsPanel cardsPanel = new CardsPanel();
		setCards();
		cardsPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));
		add(cardsPanel, BorderLayout.EAST);
		controlGUI = new ControlGUI();
		add(controlGUI, BorderLayout.SOUTH);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
	}
	
	/*
	 * Displays the splash screen welcoming the user to the new game
	 */
	public void splashScreen() {
		String message = "You are " + board.getHumanPlayerString() + ", press Next Player to begin playing";
		JOptionPane.showMessageDialog(frame, message, "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/*
	 * Sets the Array lists of each type of card for the human player's hand
	 */
	private void setCards() {
		Player humanPlayer = board.getHumanPlayer();
		ArrayList<String> people = new ArrayList<String>();
		ArrayList<String> rooms = new ArrayList<String>();
		ArrayList<String> weapons = new ArrayList<String>();
		for (Card card : humanPlayer.getHand()) {
			if (card.isPerson())
				people.add(card.getCardName());
			else if (card.isRoom())
				rooms.add(card.getCardName());
			else if (card.isWeapon())
				weapons.add(card.getCardName());
		}
		CardsPanel.setPeople(people);
		CardsPanel.setRooms(rooms);
		CardsPanel.setWeapons(weapons);
	}

	/*
	 * Sets up the board game with the proper config files
	 */
	public static void setUp() {
		board = Board.getInstance();
		board.setTest(false);
		board.setConfigurationFiles("BoardLayout.csv", "ClueRooms.txt", "Players.txt", "Weapons.txt");
		board.initializeClueGame();
	}
	
	/*
	 * Creates the file menu 
	 */
	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.add(createFileNotesItem());
		menu.add(createFileExitItem());
		return menu;
	}
	
	/*
	 * Creates the exit item in the file menu
	 */
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}
	
	/*
	 * Creates the detective notes item in the file menu
	 */
	private JMenuItem createFileNotesItem() {
		JMenuItem item = new JMenuItem("Detective notes");
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				notes.setSize(900, 700);
				notes.setVisible(true);
			}
		}
		item.addActionListener(new MenuItemListener());
		return item;
	}

	public static boolean isGameOver() {
		return isGameOver;
	}

	public static void setGameOver(boolean gameOver) {
		isGameOver = gameOver;
	}

	/*
	 * Main for the game
	 */
	public static void main(String[] args) {
		ClueGame gui = new ClueGame();
		gui.setVisible(true);
		gui.splashScreen();
	}
}
