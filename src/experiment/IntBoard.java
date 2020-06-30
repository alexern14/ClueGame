/*
 * Authors: Alexandra Ernst & Mia Belliveau
 */

package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	private Map<BoardCell, Set<BoardCell>> adjMtx = new HashMap<BoardCell, Set<BoardCell>>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private BoardCell[][] grid = new BoardCell[4][4];
	
	public IntBoard() {
		createGrid();
		calcAdjacencies();
	}
	
	/*
	 * Creates the board game grid
	 */
	public void createGrid() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j ++) {
				BoardCell cell = new BoardCell(i, j);  
				grid[i][j] = cell;
			}
		}
	}
	
	/*
	 * calculates the adjacencies for each cell
	 */
	public void calcAdjacencies() {
		//for each cell
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				BoardCell currCell = grid[i][j];
				Set<BoardCell> adjList = new HashSet<BoardCell>();
				//look at each neighbor
				//if it's a valid index, add to adjlist
				if ((i - 1) < 4 && (i - 1) >= 0) {
					BoardCell upNeighbor = grid[i - 1][j];
					adjList.add(upNeighbor);
				}
				if ((i + 1) < 4 && (i + 1) >= 0) {
					BoardCell downNeighbor = grid[i + 1][j];
					adjList.add(downNeighbor);
				}
				if ((j - 1) < 4 && (j - 1) >= 0) {
					BoardCell leftNeighbor = grid[i][j - 1];
					adjList.add(leftNeighbor);
				}
				if ((j + 1) < 4 && (j + 1) >= 0) {
					BoardCell rightNeighbor = grid[i][j + 1];
					adjList.add(rightNeighbor);
				}
				if (adjList != null) {
					adjMtx.put(currCell, adjList);
				}
			}
		}
	}	
	
	/*
	 * returns the list of adjacencies for a particular boardcell
	 */
	public Set<BoardCell> getAdjList(BoardCell cell) {
		return adjMtx.get(cell);
	}
	
	/*
	 * calculates the targets for a given starting cell and a given path length(roll of the die)
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited.add(startCell);
		
		Set<BoardCell> adjList = getAdjList(startCell);
		
		// For each adjcell in adjacentcells
		for(BoardCell cell : adjList) {
			// if already in visited list, skip rest
			if(!visited.contains(cell)) {
				//add adjcell to visited list
				visited.add(cell);
				// if pathlength == 1, add adjcell to targets
				if (pathLength ==1) {
					targets.add(cell);
				}// else, call calcTargets with adjcell, pathLength - 1
				else {
					calcTargets(cell, pathLength - 1);
				}	
				//remove adjcell from visited list
				visited.remove(cell);
			}
		}
	}
	
	/*
	 * Returns the targets for a given starting cell
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}

	/*
	 * returns the grid pointer for a certain boardcell
	 */
	public BoardCell getCell(int row, int column) {
		return grid[row][column];
	}
	
	/*
	 * used for debugging in order to get tests to work
	 */
	public static void main(String[] args) {
		IntBoard board = new IntBoard();
		BoardCell cell = board.getCell(0,0);
		System.out.println(cell);
		Set<BoardCell> testList = board.getAdjList(cell); 
		
		BoardCell cell1 = board.getCell(0, 0);
		board.calcTargets(cell1, 3);
		Set <BoardCell>targets = board.getTargets();
		
		BoardCell cell2 = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set <BoardCell> targets1 = board.getTargets();
	}
	
}
