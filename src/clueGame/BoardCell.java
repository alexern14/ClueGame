/*
 * Authors: 
 * 		Alexandra Ernst 
 * 		Mia Belliveau
 * 		Alexander Langfield
 * 
 * This class creates a single board piece
 */

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private String roomName;
	private DoorDirection door;
	private int doorX;  // where the cell should start drawing itself
	private int doorY;  // where the cell should start drawing itself
	private int width = 30; // width of the cell (for drawing purposes)
	private int height = 30;  // height of the cell (for drawing purposes)
	private int doorWidth = 30;  // width of the door (for drawing purposes)
	private int doorHieght = 30;  // height of the door (for drawing purposes)
	private boolean displaysRoomName = false; // flag for whether or not this cell will draw the room name
	private Set<Player> playerOnCell = new HashSet<Player>();  // set of players that are on this cell
	private boolean isTarget;  // flag for whether or not this cell is a target
	
	/*
	 * Constructor
	 */
	public BoardCell(int row, int column, char initial, DoorDirection door) {
		this.row = row;
		this.column = column;
		this.initial = initial;
		this.door = door;
	}
	
	/*
	 * This method draws this cell
	 */
	public void drawCell(Graphics g) {
		int x = getX();
		int y = getY();
		// if this cell is a room, set the color and draw the cell
		if (this.isRoom()) {
			g.setColor(new Color(11, 148, 232));
			g.fillRect(x, y, width, height);
		}
		// if this cell is a doorway, draw door in correct position
		// based on the door direction
		if (this.isDoorway()) {
			drawDoor(g);
		}
		// if this cell is a walkway, set the color and draw the cell
		else if (this.isWalkway()) {
			//outline the cell
			g.setColor(Color.white);
			g.fillRect(x, y, width, height);
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
		}
		if (isTarget) {
			g.setColor(Color.cyan);
			g.fillRect(x, y, width, height);
			g.setColor(Color.black);
			g.drawRect(x, y, width, height);
			if (this.isDoorway()) {
				drawDoor(g);
				g.setColor(Color.black);
				g.drawRect(x, y, width, height);
			}
		}
		// if this room is supposed to display the room name, then draw the room name
		if (displaysRoomName == true) {
			g.setColor(Color.black);
			g.drawString(roomName, x, y);
		}
		// if this cell has a player on it, call that player to draw themselves
		if (this.getPlayerOnCell() != null) {
			for (Player player : playerOnCell) {
				player.drawPlayer(g);
			}
		}
	}

	/*
	 * Draws the door
	 */
	private void drawDoor(Graphics g) {
		if (door == DoorDirection.DOWN) {
			doorX = getColumn() * 30;
			doorY = (getRow() * 30) + 25;
			doorHieght = 5;
		}else if (door == DoorDirection.UP) {
			doorX = getColumn() * 30;
			doorY = getRow() * 30;
			doorHieght = 5;
		}else if (door == DoorDirection.LEFT) {
			doorX = getColumn() * 30;
			doorY = getRow() * 30;
			doorWidth = 5;
		}else if (door == DoorDirection .RIGHT) {
			doorX = getColumn() * 30 + 25;
			doorY = getRow() * 30;
			doorWidth = 5;
		}
		g.setColor(Color.blue);
		g.fillRect(doorX, doorY, doorWidth, doorHieght);
	}
	
	/*
	 * Adds the player to the list of players on this cell 
	 */
	public void addPlayerOnCell(Player playerOnCell) {
		Set<Player> playersOnCell = this.playerOnCell;
		playersOnCell.add(playerOnCell);
		this.playerOnCell = playersOnCell;
	}
	
	/*
	 * Removes the player from the list of players on this cell
	 */
	public void removePlayerOnCell(Player player) {
		Set<Player> playersOnCell = this.playerOnCell;
		playersOnCell.remove(player);
		this.playerOnCell = playersOnCell;
	}
	
	/*
	 * Getters and setters:
	 */
	
	/*
	 * returns whether or not this board cell is a walkway
	 */
	public boolean isWalkway() {
		if( initial == 'W')
			return true;
		else return false;
	}
	
	/*
	 *  returns whether or not this board cell is a room
	 */
	public boolean isRoom() {
		if (initial == 'W')
			return false;
		else return true;
	}

	/*
	 * returns whether or not this board cell is a door
	 */
	public boolean isDoorway() {
		if(door == DoorDirection.NONE)
			return false;
		else return true;
	}
	
	public DoorDirection getDoorDirection() {
		return door;
	}
	
	private int getY() {
		return (getRow() * height);
	}

	private int getX() {
		return (getColumn() * width);
	}

	// returns the 1 character initial for this board cell
	public char getInitial() {
		return initial;
	}

	// returns the row this cell is in
	public int getRow() {
		return this.row;
	}

	// returns the column this cell is in
	public int getColumn() {
		return this.column;
	}

	public Set<Player> getPlayerOnCell() {
		return playerOnCell;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setDisplaysRoomName(boolean displaysRoomName) {
		this.displaysRoomName = displaysRoomName;
	}
	
	public void setIsTarget(boolean istarget) {
		this.isTarget = istarget;
	}
	
	public boolean isTarget() {
		return isTarget;
	}
}
