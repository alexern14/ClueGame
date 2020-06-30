/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * Class for the make a guess dialog
 */

package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MakeAGuess extends JDialog{
	private Board board = Board.getInstance();
	JTextArea room;
	JComboBox<String> weaponGuess;
	JComboBox<String> personGuess;
	String weaponGuessString;
	String personGuessString;
	String roomName;
	Player currentPlayer = board.getCurrentPlayer();
	
	public MakeAGuess(String room) {
		this.roomName = room;
		setModal(true);
		setSize(400, 200);
		setTitle("Suggestion");
		setLayout(new GridLayout(4, 1));
		JPanel roomPanel = roomPanel(room);
		add(roomPanel);
		JPanel personPanel = personPanel();
		add(personPanel);
		JPanel weaponPanel = weaponPanel();
		add(weaponPanel);
		JPanel buttonPanel = buttonPanel();
		add(buttonPanel);
	}

	/*
	 * sets up the panel with the submit and cancel buttons
	 */
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new SubmitButtonListener());
		panel.add(submitButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new CancelButtonListener());
		panel.add(cancelButton);
		return panel;
	}

	/*
	 * sets up the panel for the weapon selection
	 */
	private JPanel weaponPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel weaponLabel = new JLabel("Weapon");
		panel.add(weaponLabel);
		weaponGuess = new JComboBox<String>();
		weaponGuess.addItem(null);
		weaponGuess.addItem("Knife");
		weaponGuess.addItem("Pistol");
		weaponGuess.addItem("Axe");
		weaponGuess.addItem("Bat");
		weaponGuess.addItem("Rope");
		weaponGuess.addItem("Candlestick");
		panel.add(weaponGuess);
		weaponGuess.addActionListener(new ComboListener());
		return panel;
	}

	/*
	 * sets up the panel for the person selection 
	 */
	private JPanel personPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel personLabel = new JLabel("Person");
		panel.add(personLabel);
		personGuess = new JComboBox<String>();
		personGuess.addItem(null);
		personGuess.addItem("Melancholy May");
		personGuess.addItem("Miss Scarlet");
		personGuess.addItem("Mr. Green");
		personGuess.addItem("Colonel Mustard");
		personGuess.addItem("Professor Plum");
		personGuess.addItem("Clementine");
		panel.add(personGuess);
		personGuess.addActionListener(new ComboListener());
		return panel;
	}

	/*
	 * sets up the panel that shows the room 
	 */
	private JPanel roomPanel(String roomName) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel roomLabel = new JLabel("Your room");
		panel.add(roomLabel);
		room = new JTextArea();
		room.setText(roomName); 
		panel.add(room);
		return panel;
	}
	
	/*
	 * Action for when the submit button is pressed
	 */
	private class SubmitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			currentPlayer.setSuggested(true);
			Suggestion suggestion = new Suggestion(personGuessString, roomName, weaponGuessString);
			String guess = suggestion.getPerson() + ", " + suggestion.getRoom() + ", " + suggestion.getWeapon();
			ControlGUI.setGuess(guess);
			Card disprovingCard = board.handleSuggestion(suggestion, board.getPeople(), currentPlayer);
			if (disprovingCard == null) {
				ControlGUI.setGuessResult("no new clue");
			}else {
				String disprove = disprovingCard.getCardName();
				ControlGUI.setGuessResult(disprove);
				for (Player player : board.getPeople()) {
					player.removeNotSeenCards(disprovingCard);
				}
			}
			setVisible(false);
			dispose();
		}
	}
	
	/*
	 * Action for when the cancel button is pressed
	 */
	private class CancelButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			setVisible(false);
			dispose();
		}
	}
	
	/*
	 * Action for when either the person or weapon are selected
	 */
	private class ComboListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == weaponGuess) {
				weaponGuessString = weaponGuess.getSelectedItem().toString();
			}else 
				personGuessString = personGuess.getSelectedItem().toString();
		}
	}
	
	/*
	 * Main for testing purposes
	 */
	public static void main(String[] args) {
		MakeAGuess gui = new MakeAGuess("House");
		gui.setVisible(true);
	}
}
