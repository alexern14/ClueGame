/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * Tests for the correct calculations for the adjacencies and the targets
 */

package tests;

import static org.junit.Assert.*;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import experiment.BoardCell;
import experiment.IntBoard;

public class IntBoardTests {
	private IntBoard board;	//initialize instance var
	
	@Before 
	
	public void beforeAll() {
		board = new IntBoard();
	}

	/*
	 * 6 methods to test the creation of adjacency lists
	 */
	///Tests [0][0]
	@Test
	public void testAdjacencyTL() {
		BoardCell cell = board.getCell(0,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	//Tests [3][3]
	@Test
	public void testAdjacencyBR() {
		BoardCell cell = board.getCell(3,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(2, testList.size());
	}
			
	//Tests [1][3]
	@Test
	public void testAdjacencyRightEdge() {
		BoardCell cell = board.getCell(1,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(3, testList.size());
	}
	
	//Tests [3][0]
	@Test
	public void testAdjacencyLeftEdge() {
		BoardCell cell = board.getCell(2,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(3, 0)));
		assertEquals(3, testList.size());
	}
	
	//Tests [1][1]
	@Test
	public void testSecondColumnMiddleOfGrid() {
		BoardCell cell = board.getCell(1,1);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertEquals(4, testList.size());
	}
	
	//Tests [2][2]
	@Test
	public void testSecondFromLastColumnMiddleOfGrid() {
		BoardCell cell = board.getCell(2, 2);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(4, testList.size());
	}
	
	/*
	 * 6 methods to test target creation
	 */
	@Test
	public void testTargets00_3() {
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 3);
		Set <BoardCell>targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertEquals(6, targets.size());
	}
	
	@Test
	public void testTargets00_2() {
		BoardCell cell = board.getCell(0, 0);
		board.calcTargets(cell, 2);
		Set <BoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertEquals(3, targets.size());
	}
	
	@Test
	public void testTargets11_2() {
		BoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 2);
		Set <BoardCell>targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertEquals(6, targets.size());
	}
	
	@Test
	public void testTarget11_1() {
		BoardCell cell = board.getCell(1, 1);
		board.calcTargets(cell, 1);
		Set<BoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertEquals(4, targets.size());
	}
	
	@Test
	public void testTargets21_2() {
		BoardCell cell = board.getCell(2, 1);
		board.calcTargets(cell, 2);
		Set<BoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertEquals(6, targets.size());
	}
	
	@Test
	public void testTarget22_3() {
		BoardCell cell = board.getCell(2, 2);
		board.calcTargets(cell, 3);
		Set<BoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertEquals(8, targets.size());
	}

}