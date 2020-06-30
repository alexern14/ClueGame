/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * This class sets up the detective notes dialog
 */

package clueGame;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotes extends JDialog{
	private JRadioButton may, scarlet, green, mustard, plum, clementine;
	private JRadioButton knife, pistol, axe, bat, rope, candlestick;
	private JRadioButton bathroom, dining, family, garage, kitchen, bedroom, office, play, theatre;
	private JComboBox<String> personGuess;
	private JComboBox<String> roomGuess;
	private JComboBox<String> weaponGuess;
	
	/*
	 * Constructor
	 */
	public DetectiveNotes() {
		setLayout(new GridLayout(3, 1));
		JPanel people = peoplePanel();
		add(people);
		JPanel rooms = roomPanel();
		add(rooms);
		JPanel weapons = weaponPanel();
		add(weapons);
	}

	/*
	 * Sets up the panel for the people
	 */
	private JPanel peoplePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));  
		JPanel peopleButtonsPanel = peopleButtonsPanel();
		JPanel peopleGuessPanel = peopleGuessPanel();
		panel.add(peopleButtonsPanel);
		panel.add(peopleGuessPanel);
		
		return panel;
	}
	
	/*
	 * Sets up the buttons for all the people cards
	 */
	private JPanel peopleButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3));   
		may = new JRadioButton("Melancholy May");
		scarlet = new JRadioButton("Miss Scarlet");
		green = new JRadioButton("Mr. Green");
		mustard = new JRadioButton("Colonel Mustard");
		plum = new JRadioButton("Professor Plum");
		clementine = new JRadioButton("Clementine");
		panel.add(may);
		panel.add(scarlet);
		panel.add(green);
		panel.add(mustard);
		panel.add(plum);
		panel.add(clementine);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		
		return panel;
	}
	
	/* 
	 * Sets up the combo box for the suspected person
	 */
	private JPanel peopleGuessPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));   
		personGuess = new JComboBox<String>();
		personGuess.addItem("Unsure");
		personGuess.addItem("Melancholy May");
		personGuess.addItem("Miss Scarlet");
		personGuess.addItem("Mr. Green");
		personGuess.addItem("Colonel Mustard");
		personGuess.addItem("Professor Plum");
		personGuess.addItem("Clementine");
		panel.add(personGuess);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
		
		return panel;
	}
	
	/*
	 * Sets up the panel for the rooms
	 */
	private JPanel roomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));  
		JPanel roomButtonsPanel = roomButtonsPanel();
		JPanel roomGuessPanel = roomGuessPanel();
		panel.add(roomButtonsPanel);
		panel.add(roomGuessPanel);
		
		return panel;
	}
	
	/*
	 * Sets up the buttons for all the room cards
	 */
	private JPanel roomButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3)); 
		bathroom = new JRadioButton("Bathroom");
		dining = new JRadioButton("Dining Room");
		family = new JRadioButton("Family Room");
		garage = new JRadioButton("Gargae");
		kitchen = new JRadioButton("Kitchen");
		bedroom = new JRadioButton("Master Bedroom");
		office = new JRadioButton("Office");
		play = new JRadioButton("Play Room");
		theatre = new JRadioButton("Theatre");
		panel.add(bathroom);
		panel.add(dining);
		panel.add(family);
		panel.add(garage);
		panel.add(kitchen);
		panel.add(bedroom);
		panel.add(office);
		panel.add(play);
		panel.add(theatre);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		
		return panel;
	}
	
	/* 
	 * Sets up the combo box for the suspected room
	 */
	private JPanel roomGuessPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2)); 
		roomGuess = new JComboBox<String>();
		roomGuess.addItem("Unsure");
		roomGuess.addItem("Bathroom");
		roomGuess.addItem("Dining Room");
		roomGuess.addItem("Family Room");
		roomGuess.addItem("Garage");
		roomGuess.addItem("Kitchen");
		roomGuess.addItem("Master Bedroom");
		roomGuess.addItem("Office");
		roomGuess.addItem("Play Room");
		roomGuess.addItem("Theatre");
		panel.add(roomGuess);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
		
		return panel;
	}
	
	/*
	 * Sets up the panel for the weapons
	 */
	private JPanel weaponPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));  
		JPanel weaponButtonsPanel = weaponButtonsPanel();
		JPanel weaponGuessPanel = weaponGuessPanel();
		panel.add(weaponButtonsPanel);
		panel.add(weaponGuessPanel);
		
		return panel;
	}
	
	/*
	 * Sets up the buttons for all the weapon cards
	 */
	private JPanel weaponButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3));   
		knife = new JRadioButton("Knife");
		pistol = new JRadioButton("Pistol");
		axe = new JRadioButton("Axe");
		bat = new JRadioButton("Bat");
		rope = new JRadioButton("Rope");
		candlestick = new JRadioButton("Candlestick");
		panel.add(knife);
		panel.add(pistol);
		panel.add(axe);
		panel.add(bat);
		panel.add(rope);
		panel.add(candlestick);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		
		return panel;
	}
	
	/* 
	 * Sets up the combo box for the suspected weapon
	 */
	private JPanel weaponGuessPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));  
		weaponGuess = new JComboBox<String>();
		weaponGuess.addItem("Unsure");
		weaponGuess.addItem("Knife");
		weaponGuess.addItem("Pistol");
		weaponGuess.addItem("Axe");
		weaponGuess.addItem("Bat");
		weaponGuess.addItem("Rope");
		weaponGuess.addItem("Candlestick");
		panel.add(weaponGuess);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
		
		return panel;
	}
}
