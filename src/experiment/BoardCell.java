/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * This class creates a single board piece
 */

package experiment;

public class BoardCell {

	int row;
	int column;
	
	
	public BoardCell(int row, int column) {
		this.row = row;
		this.column = column;
	}


	public static BoardCell boardCell(int row, int column) { //reference to cell based on ints
		BoardCell boardCell = new BoardCell(row, column);
		return boardCell;
	}
 

	
}
