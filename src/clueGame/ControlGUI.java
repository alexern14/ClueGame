/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * Sets up the control GUI
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ControlGUI extends JPanel{
	private String currentPlayerString;
	private Integer die;
	private static String guess;
	private static String result;
	private JTextArea curPlayer;
	private static JTextArea guessDisplay;
	private static JTextArea resultDisplay;
	private JTextArea rollDisplay;
	private JButton nextPlayerButton;
	private JButton accusationButton;
	
	private Player currentPlayer;
	private Board board = Board.getInstance();
	private boolean firstPush = true;
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	
	private int width = 30;
	private int height = 30;
	
	/*
	 * Constructor
	 */
	public ControlGUI() {
		setPreferredSize(new Dimension(1000, 150));
		currentPlayerString = "Current player";
		guess = "Guess";
		result = "Result";
		setLayout(new GridLayout(2, 0));
		JPanel topPanel = topPanel();
		add(topPanel);
		JPanel bottomPanel = bottomPanel();
		add(bottomPanel);
	}
	
	private JPanel topPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));    
		JPanel whoseTurnPanel = whoseTurnPanel();
		JPanel buttonPanel = buttonPanel();
		panel.add(whoseTurnPanel, BorderLayout.WEST);
		panel.add(buttonPanel);
		
		return panel;
	}

	private JPanel whoseTurnPanel() {
		JPanel panel = new JPanel();
		JPanel playerColor = new playerColorPanel();
		JLabel whoseTurnLabel = new JLabel("Whose turn?");
		curPlayer = new JTextArea(1, 35);
		curPlayer.setText("Current Player");
		panel.add(whoseTurnLabel);
		panel.add(curPlayer);
		panel.add(playerColor);
		
		return panel;
	}
	
	private JPanel buttonPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));    
		nextPlayerButton = new JButton("Next player");
		accusationButton = new JButton("Make an accusation");
		nextPlayerButton.addActionListener(new NextPlayerButtonListener());
		accusationButton.addActionListener(new AccusationButtonListener());
		panel.add(nextPlayerButton);
		panel.add(accusationButton);
		
		return panel;
	}
	
	private JPanel bottomPanel() {
		JPanel panel = new JPanel();   
		JPanel diePanel = dicePanel();
		JPanel guessPanel = guessPanel();
		JPanel resultPanel = resultPanel();
		panel.add(diePanel);
		panel.add(guessPanel);
		panel.add(resultPanel);
		
		return panel;
	}
	
	private JPanel dicePanel() {
		JPanel panel = new JPanel();
		JLabel dieLabel = new JLabel("Roll");
		rollDisplay = new JTextArea(1, 5);
		panel.add(dieLabel);
		panel.add(rollDisplay);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		
		return panel;
	}
	
	private JPanel guessPanel() {
		JPanel panel = new JPanel();
		JLabel guessLabel = new JLabel("Guess");
		guessDisplay = new JTextArea(1, 33);
		updateGuess();
		panel.add(guessLabel);
		panel.add(guessDisplay);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		
		return panel;
	}
	
	private JPanel resultPanel() {
		JPanel panel = new JPanel();
		JLabel resultLabel = new JLabel("Response");
		resultDisplay = new JTextArea(1, 15);
		updateResult();
		panel.add(resultLabel);
		panel.add(resultDisplay);
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));

		return panel;
	}
	
	/*
	 * Sets up the panel to show the color of the current player
	 */
	class playerColorPanel extends JPanel {
        public playerColorPanel() {
            // set a preferred size for the custom panel.
            setPreferredSize(new Dimension(35,35));
        }

        @Override
        public void paintComponent(Graphics g) {
    		super.paintComponent(g);
    		Player player = board.getCurrentPlayer();
    		g.setColor(Color.black);
    		g.drawOval(0, 0, 30, 30);
    		if (player == null) {
    			g.setColor(Color.white);
    		}else 
    			g.setColor(player.getColor());
    		g.fillOval(0, 0, width, height);
    	}
    }

	/*
	 * Action for when the next player button is pressed
	 */
	private class NextPlayerButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (ClueGame.isGameOver()) { 
				JOptionPane.showMessageDialog(null, "Game is over");
			}else {
				setGuess(" ");
				setGuessResult(" ");
				board.setSelected(false);
				if (firstPush) { // if this is the first time being pushed, start the game
					board.setStartingPlayer(board.getHumanPlayer()); //set the starting player to the human player
					firstPush = false;
				}else if (!board.getCurrentPlayer().isFinished()) {  // if the current player is not finished give an error message
					JOptionPane.showMessageDialog(null, "Human player is not finished with their turn");
				}else {
					board.getCurrentPlayer().setAccused(false);  //reset
					board.getCurrentPlayer().setCanAccuse(true);  //reset
					board.getCurrentPlayer().setSuggested(false);  //reset
					for (BoardCell target : targets) {  // reset so there are no targets
						target.setIsTarget(false);
					}
					board.setCurrentPlayer(); // move to the next player
				}
				if (board.getCurrentPlayer().isFinished()) {
					currentPlayer = board.getCurrentPlayer(); // get the current player
					currentPlayer.rollDie(); // roll the die
					int roll = Integer.parseInt((currentPlayer.getDieRoll()));
					board.calcTargets(currentPlayer.getRow(), currentPlayer.getColumn(), roll);  //calc the targets
					targets = board.getTargets();
					update();
					if (currentPlayer.isHuman(currentPlayer)) { // if it's the human players turn
						if (targets.isEmpty()) // if there are not targets (blocked in by other players) their turn is finished
							currentPlayer.setFinished(true);
						else {
							BoardCell currentRoom = board.getCellAt(currentPlayer.getRow(), currentPlayer.getColumn());
							String currentRoomName = currentRoom.getRoomName(); 
							for (BoardCell target : targets) { // set flag for all targets
								if (currentRoom.isDoorway()) { // if currently in a room
									if (!target.getRoomName().equals(currentRoomName)) // and a target is in the same room, set that flag of target to false
										target.setIsTarget(true);
								}else 
									target.setIsTarget(true);
							}
							board.repaint();
							currentPlayer.setFinished(false);
						}
					}else if (currentPlayer instanceof ComputerPlayer) {  // if it's a computer players turn
						ComputerPlayer computerPlayer = (ComputerPlayer) currentPlayer;
						if (computerPlayer.isReadyToAccuse()) { // if the computer player is ready to make an accusation
							Suggestion accusation = computerPlayer.getAccusation(); 
							boolean correct = board.checkAccusation(accusation); //check the accusation
							String person = accusation.getPerson();
							String room = accusation.getRoom();
							String weapon = accusation.getWeapon();
							String accuser = computerPlayer.getName();
							String message = accuser + " made the following accusation: " + person + ", " + room + ", " + weapon + ".";
							if (correct) { // if the accusation is correct
								JOptionPane.showMessageDialog(null, message + "\n" + "The accusation was correct, " + accuser + " wins the game!");
								ClueGame.setGameOver(true);
							} else { // if the accusation is incorrect
								Card disprovingCard = null;
								for (Card card : board.getDeck()) {
									String cardName = card.getCardName();
									if (cardName.equals(person) || cardName.equals(room) || cardName.equals(weapon)) 
										disprovingCard = card;
								}
								for (Player player : board.getPeople()) {
									player.removeNotSeenCards(disprovingCard);
								}
								computerPlayer.setReadyToAccuse(false);
								JOptionPane.showMessageDialog(null, message + "\n" + "The accusation was incorrect, click OK to continue playing.");
							}
						}else if (!targets.isEmpty()) // if there are targets, move the player
							computerPlayer.makeMove(targets);
					}
					board.setDisprovingCard(null);
				}
			}
		}
	}

	/*
	 * Action for when the accusation button is pressed
	 */
	private class AccusationButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (board.getCurrentPlayer() == null) {
				JOptionPane.showMessageDialog(null, "The game hasn't started yet. Press Next Player to start.");
			} else{
				currentPlayer = board.getCurrentPlayer();
				if (currentPlayer.isHuman(currentPlayer)) {
					if (currentPlayer.canAccuse()) {
						MakeAnAccusation accusationPanel = new MakeAnAccusation();
						accusationPanel.setVisible(true);
					}else if (!currentPlayer.isFinished()) {
						JOptionPane.showMessageDialog(null, "You've already made an accusation.");
					}else  
						JOptionPane.showMessageDialog(null, "You've already moved. You can only make an accusation at the beginning of your turn.");
				}else 
					JOptionPane.showMessageDialog(null, "It's not your turn. Please retry when it's your turn.");
			}
		}
	}

	/*
	 * Updates the display of the current player and their die roll
	 */
	private void update() {
		currentPlayer = board.getCurrentPlayer();
		currentPlayerString = currentPlayer.getName();
		String dieRoll = currentPlayer.getDieRoll();
		curPlayer.setText(currentPlayerString);
		rollDisplay.setText(dieRoll);
		repaint();
	}

	/*
	 * Getters and setters:
	 */

	public static void setGuessResult(String resultString) {
		result = resultString;
		updateResult();
	}

	public static void setGuess(String guessString) {
		guess = guessString;
		updateGuess();
	}
	
	public void setRoll(Integer roll) {
		this.die = roll;
		updateRoll();
	}

	private void showTurn(String turn) {
		this.currentPlayerString = turn;
		updateTurn();
	}
	
	private static void updateResult() {
		resultDisplay.setText(result);
	}
	
	private static void updateGuess() {
		guessDisplay.setText(guess);
	}
	
	private void updateTurn() {
		curPlayer.setText(currentPlayerString);
	}
	
	private void updateRoll() {
		rollDisplay.setText(die.toString());
	}

	/*
	 * Main for testing purposes
	 */
	public static void main(String[] args) {
		ControlGUI gui = new ControlGUI();
		JFrame frame = new JFrame();
		frame.setContentPane(gui);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 200);
		gui.showTurn("Miss Scarlet");
		gui.setGuess("My guess");
		gui.setGuessResult("I guessed it!");
		gui.setRoll(4);
		frame.setVisible(true);
	}
}
