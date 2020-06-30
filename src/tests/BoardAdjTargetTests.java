package tests;

/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {
	// We make the Board static because we can load it one time and then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("BoardLayout.csv", "ClueRooms.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		Set<BoardCell> testList;
		// Test a corner
		testList = board.getAdjList(0, 0);			
		assertEquals(0, testList.size());		
		// Test one that has walkway underneath
		testList = board.getAdjList(5, 0);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(16, 15);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(20, 23);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(3, 6);
		assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(7, 19);
		assertEquals(0, testList.size());
		// Test one on the left edge
		testList = board.getAdjList(9, 0);
		assertEquals(1, testList.size());
		// Test one on the right edge
		testList = board.getAdjList(7, 25);
		assertEquals(1, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the walkway 
	// NOTE: This test could be merged with door direction test. 
	// These tests are PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		Set<BoardCell> testList;
		// Test doorway RIGHT 
		testList = board.getAdjList(3, 15);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(3, 16)));
		// Test doorway LEFT 
		testList = board.getAdjList(13, 18);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(13, 17)));
		// Test doorway DOWN
		testList = board.getAdjList(7, 25);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 25)));
		// Test doorway UP
		testList = board.getAdjList(18, 22);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(17, 22)));
		// Test doorway LEFT, where ther's a walkway below
		testList = board.getAdjList(6, 2);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(6, 1)));
	}
	
	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		Set<BoardCell> testList;
		// Test beside a door direction RIGHT
		testList = board.getAdjList(13, 8);
		assertTrue(testList.contains(board.getCellAt(12, 8)));
		assertTrue(testList.contains(board.getCellAt(14, 8)));
		assertTrue(testList.contains(board.getCellAt(13, 7)));
		assertTrue(testList.contains(board.getCellAt(13, 9)));
		assertEquals(4, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(5, 6);
		assertTrue(testList.contains(board.getCellAt(4, 6)));
		assertTrue(testList.contains(board.getCellAt(5, 7)));
		assertTrue(testList.contains(board.getCellAt(6, 6)));
		assertEquals(3, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(12, 17);
		assertTrue(testList.contains(board.getCellAt(12, 18)));
		assertTrue(testList.contains(board.getCellAt(12, 16)));
		assertTrue(testList.contains(board.getCellAt(11, 17)));
		assertTrue(testList.contains(board.getCellAt(13, 17)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(8, 0);
		assertTrue(testList.contains(board.getCellAt(7, 0)));
		assertTrue(testList.contains(board.getCellAt(8, 1)));
		assertTrue(testList.contains(board.getCellAt(9, 0)));
		assertEquals(3, testList.size());
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		Set<BoardCell> testList;
		// Test on top edge of board, just one walkway piece
		testList = board.getAdjList(0, 7);
		assertTrue(testList.contains(board.getCellAt(1, 7)));
		assertEquals(1, testList.size());
		
		// Test on left edge of board, two walkway pieces
		testList = board.getAdjList(16, 0);
		assertTrue(testList.contains(board.getCellAt(15, 0)));
		assertTrue(testList.contains(board.getCellAt(16, 1)));
		assertEquals(2, testList.size());

		// Test between two rooms, walkways up and down
		testList = board.getAdjList(23, 6);
		assertTrue(testList.contains(board.getCellAt(22, 6)));
		assertTrue(testList.contains(board.getCellAt(24, 6)));
		assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(15,9);
		assertTrue(testList.contains(board.getCellAt(15, 8)));
		assertTrue(testList.contains(board.getCellAt(15, 10)));
		assertTrue(testList.contains(board.getCellAt(14, 9)));
		assertTrue(testList.contains(board.getCellAt(16, 9)));
		assertEquals(4, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(9, 25);
		assertTrue(testList.contains(board.getCellAt(8, 25)));
		assertTrue(testList.contains(board.getCellAt(9, 24)));
		assertEquals(2, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(25, 19);
		assertTrue(testList.contains(board.getCellAt(25, 18)));
		assertTrue(testList.contains(board.getCellAt(24, 19)));
		assertEquals(2, testList.size());

		// Test on walkway next to  door that is not in the needed direction to enter
		testList = board.getAdjList(11, 18);
		assertTrue(testList.contains(board.getCellAt(10, 18)));
		assertTrue(testList.contains(board.getCellAt(11, 17)));
		assertEquals(2, testList.size());
	}
	
	
	// Tests of just walkways, 1 step, includes on edge of board and beside room
	// Have already tested adjacency lists on all four edges, will only test two edges here
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		Set<BoardCell> targets;
		
		board.calcTargets(25, 11, 1);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(25, 12)));
		assertTrue(targets.contains(board.getCellAt(24, 11)));	
		assertEquals(2, targets.size());
		targets.clear();
		
		board.calcTargets(7, 14, 1);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(7, 13)));
		assertTrue(targets.contains(board.getCellAt(8, 14)));	
		assertTrue(targets.contains(board.getCellAt(7, 15)));	
		assertEquals(3, targets.size());
		targets.clear();
	}
	
	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		Set<BoardCell> targets;
		
		// Includes a path that doesn't have enough length
		board.calcTargets(25, 11, 2);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(23, 11)));
		assertEquals(1, targets.size());
		targets.clear();
		
		board.calcTargets(7, 14, 2);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(7, 12)));
		assertTrue(targets.contains(board.getCellAt(8, 13)));	
		assertTrue(targets.contains(board.getCellAt(8, 15)));	
		assertTrue(targets.contains(board.getCellAt(7, 16)));	
		assertEquals(4, targets.size());
		targets.clear();
	}
	
	// Tests of just walkways, 4 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		Set<BoardCell> targets;
		
		// Includes a path that doesn't have enough length
		board.calcTargets(25, 11, 4);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(21, 11)));
		assertEquals(1, targets.size());
		targets.clear();
		
		board.calcTargets(7, 14, 4);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(7, 10)));
		assertTrue(targets.contains(board.getCellAt(8, 11)));	
		assertTrue(targets.contains(board.getCellAt(8, 13)));	
		assertTrue(targets.contains(board.getCellAt(7, 12)));	
		assertTrue(targets.contains(board.getCellAt(8, 15)));	
		assertTrue(targets.contains(board.getCellAt(7, 16)));	
		assertTrue(targets.contains(board.getCellAt(9, 16)));	
		assertTrue(targets.contains(board.getCellAt(8, 17)));	
		assertTrue(targets.contains(board.getCellAt(5, 16)));	
		assertTrue(targets.contains(board.getCellAt(6, 17)));	
		assertTrue(targets.contains(board.getCellAt(7, 18)));	
		assertEquals(11, targets.size());
		targets.clear();
	}	
	
	// Tests of just walkways plus one door, 6 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsSixSteps() {
		Set<BoardCell> targets;
		
		// Includes a path that doesn't have enough length
		board.calcTargets(25, 11, 6);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(19, 11)));
		assertEquals(1, targets.size());
		targets.clear();
		
		board.calcTargets(7, 14, 6);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(5, 10)));
		assertTrue(targets.contains(board.getCellAt(6, 9)));	
		assertTrue(targets.contains(board.getCellAt(7, 8)));	
		assertTrue(targets.contains(board.getCellAt(7, 10)));	
		assertTrue(targets.contains(board.getCellAt(8, 9)));	
		assertTrue(targets.contains(board.getCellAt(9, 10)));	
		assertTrue(targets.contains(board.getCellAt(8, 11)));	
		assertTrue(targets.contains(board.getCellAt(7, 12)));	
		assertTrue(targets.contains(board.getCellAt(8, 13)));	
		assertTrue(targets.contains(board.getCellAt(8, 15)));	
		assertTrue(targets.contains(board.getCellAt(3, 16)));	
		assertTrue(targets.contains(board.getCellAt(4, 17)));	
		assertTrue(targets.contains(board.getCellAt(5, 16)));	
		assertTrue(targets.contains(board.getCellAt(5, 18)));	
		assertTrue(targets.contains(board.getCellAt(6, 17)));	
		assertTrue(targets.contains(board.getCellAt(7, 16)));	
		assertTrue(targets.contains(board.getCellAt(7, 18)));	
		assertTrue(targets.contains(board.getCellAt(8, 17)));	
		assertTrue(targets.contains(board.getCellAt(8, 19)));	
		assertTrue(targets.contains(board.getCellAt(9, 16)));	
		assertTrue(targets.contains(board.getCellAt(9, 18)));	
		assertTrue(targets.contains(board.getCellAt(10, 17)));
		assertTrue(targets.contains(board.getCellAt(11, 16)));	
		assertEquals(23, targets.size());
		targets.clear();
	}	
	
	// Test getting into a room with exact amount of steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(3, 17, 2);
		Set <BoardCell> targets = board.getTargets();
		// into room exactly 2 away
		assertTrue(targets.contains(board.getCellAt(3, 15)));
		// other walkways
		assertTrue(targets.contains(board.getCellAt(2, 16)));
		assertTrue(targets.contains(board.getCellAt(1, 17)));
		assertTrue(targets.contains(board.getCellAt(2, 18)));
		assertTrue(targets.contains(board.getCellAt(4, 16)));
		assertTrue(targets.contains(board.getCellAt(4, 18)));
		assertTrue(targets.contains(board.getCellAt(5, 17)));
		assertEquals(7, targets.size());
		targets.clear();
	}
	
	// Test getting into room, doesn't require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(17, 13, 3);
		Set<BoardCell> targets = board.getTargets();
		// into a room 2 steps away, not 3
		assertTrue(targets.contains(board.getCellAt(18, 12)));
		// other walkways
		assertTrue(targets.contains(board.getCellAt(18, 11)));
		assertTrue(targets.contains(board.getCellAt(17, 12)));
		assertTrue(targets.contains(board.getCellAt(17, 10)));
		assertTrue(targets.contains(board.getCellAt(16, 11)));
		assertTrue(targets.contains(board.getCellAt(16, 13)));
		assertTrue(targets.contains(board.getCellAt(15, 12)));
		assertTrue(targets.contains(board.getCellAt(15, 14)));
		assertTrue(targets.contains(board.getCellAt(14, 13)));	
		assertEquals(9, targets.size());
		targets.clear();
	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		Set<BoardCell> targets;
		
		board.calcTargets(7, 25, 1);
		targets = board.getTargets();
		assertTrue(targets.contains(board.getCellAt(8, 25)));	
		assertEquals(1, targets.size());
		targets.clear();
		
		// Take two steps
		board.calcTargets(7, 25, 2);
		targets= board.getTargets();
		assertTrue(targets.contains(board.getCellAt(8, 24)));
		assertTrue(targets.contains(board.getCellAt(9, 25)));
		assertEquals(2, targets.size());
		targets.clear();
		
		// Ensure doesn't exit through the wall
		// check door that is next to 2 walkways 
		board.calcTargets(18, 18, 1);
		targets= board.getTargets();
		assertTrue(targets.contains(board.getCellAt(17, 18)));
		assertEquals(1, targets.size());
		targets.clear();
	}

}
