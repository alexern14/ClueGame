/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 *  Sets up the panel to show the user the cards in their hand
 */

package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardsPanel extends JPanel{
	private static ArrayList<String> peopleStrings;
	private static ArrayList<String> roomStrings;
	private static ArrayList<String> weaponStrings;
	private static JTextArea peopleCards;
	private static JTextArea roomCards;
	private static JTextArea weaponCards;

	/*
	 * Constructor for panel
	 */
	public CardsPanel() {
		setLayout(new GridLayout(3, 1));
		JPanel peopleCardsPanel = peopleCardsPanel();
		add(peopleCardsPanel);
		JPanel roomCardsPanel = roomCardsPanel();
		add(roomCardsPanel);
		JPanel weaponCardsPanel = weaponCardsPanel();
		add(weaponCardsPanel);
	}

	/*
	 * Sets up the panel for the people cards
	 */
	private JPanel peopleCardsPanel() {
		JPanel panel = new JPanel();
		peopleCards = new JTextArea(8, 10);
		panel.add(peopleCards);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		return panel;
	}
	
	/*
	 * Sets up the panel for the room cards
	 */
	private JPanel roomCardsPanel() {
		JPanel panel = new JPanel();
		roomCards = new JTextArea(8, 10);
		panel.add(roomCards);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		return panel;
	}
	
	/*
	 * Sets up the panel for the weapon cards
	 */
	private JPanel weaponCardsPanel() {
		JPanel panel = new JPanel();
		weaponCards = new JTextArea(8, 10);
		panel.add(weaponCards);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		return panel;
	}
	
	public static void setPeople(ArrayList<String> people) {
		peopleStrings = people;
		updatePeople();
	}
	
	public static void setRooms(ArrayList<String> rooms) {
		roomStrings = rooms;
		updateRooms();
	}
	
	public static void setWeapons(ArrayList<String> weapons) {
		weaponStrings = weapons;
		updateWeapons();
	}

	/*
	 * Updates the display of the people cards
	 */
	public static void updatePeople() {
		if (peopleStrings.size() != 0) {
			String people = "";
			for (int i = 0; i < peopleStrings.size(); i++) {
				people += peopleStrings.get(i) + "\n";
			}
			peopleCards.setText(people);
		}
	}

	/*
	 * Updates the display of the room cards
	 */
	public static void updateRooms() {
		if (roomStrings.size() != 0) {
			String rooms = "";
			for (int i = 0; i < roomStrings.size(); i++) {
				rooms += roomStrings.get(i) + "\n";
			}
			roomCards.setText(rooms);
		}
	}

	/*
	 * Updates the display of the weapon cards
	 */
	public static void updateWeapons() {
		if (weaponStrings.size() != 0) {
			String weapons = "";
			for (int i = 0; i < weaponStrings.size(); i++) {
				weapons += weaponStrings.get(i) + "\n";
			}
			weaponCards.setText(weapons);
		}
	}
}
