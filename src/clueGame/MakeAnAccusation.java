/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 *  Class for the Make an accusation dialog
 */

package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MakeAnAccusation extends JDialog{
	private Board board = Board.getInstance();
	JComboBox<String> roomGuess;
	JComboBox<String> weaponGuess;
	JComboBox<String> personGuess;
	String roomGuessString;
	String weaponGuessString;
	String personGuessString;
	Player currentPlayer = board.getCurrentPlayer();
	
	public MakeAnAccusation(){
		setModal(true);
		setSize(400, 200);
		setTitle("Accusation");
		setLayout(new GridLayout(4, 1));
		JPanel roomPanel = roomPanel();
		add(roomPanel);
		JPanel personPanel = personPanel();
		add(personPanel);
		JPanel weaponPanel = weaponPanel();
		add(weaponPanel);
		JPanel buttonPanel = buttonPanel();
		add(buttonPanel);
	}
	
	/*
	 *  Sets up the panel with the submit and cancel buttons
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
	 * sets up the panel for the room selection 
	 */
	private JPanel roomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));
		JLabel roomLabel = new JLabel("Room");
		panel.add(roomLabel);
		roomGuess = new JComboBox<String>();
		roomGuess.addItem(null);
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
		roomGuess.addActionListener(new ComboListener());
		return panel;
	}
	
	/*
	 * Action for when the submit button is pressed
	 */
	private class SubmitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			for (BoardCell[] cellRow : board.getBoard()) {
				for (BoardCell cell : cellRow) {
					if (cell.isTarget()) 
						cell.setIsTarget(false);
				}
			}
			board.repaint();
			currentPlayer.setAccused(true);
			currentPlayer.setCanAccuse(false);
			currentPlayer.setFinished(true);
			setVisible(false);
			Suggestion accusation = new Suggestion(personGuessString, roomGuessString, weaponGuessString);
			boolean correct = board.checkAccusation(accusation);
			if (correct) {
				JOptionPane.showMessageDialog(null, "Your accusation was correct!! You win!! Hit OK to close the game.");
				ClueGame.setGameOver(true);
			}else {
				Card disprovingCard = null;
				JOptionPane.showMessageDialog(null, "Your accusation was incorrect. Hit OK to continue the game.");
				for (Card card : board.getDeck()) {
					String cardName = card.getCardName();
					if (cardName.equals(personGuessString) || cardName.equals(roomGuessString) || cardName.equals(weaponGuessString)) 
						disprovingCard = card;
				}
				for (Player player : board.getPeople()) {
					player.removeNotSeenCards(disprovingCard);
				}
			}
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
	 * Action for when either the person, room, or weapon are selected
	 */
	private class ComboListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == weaponGuess) {
				weaponGuessString = weaponGuess.getSelectedItem().toString();
			}else if (e.getSource() == personGuess) {
				personGuessString = personGuess.getSelectedItem().toString();
			}else 
				roomGuessString = roomGuess.getSelectedItem().toString();
		}
	}
}
