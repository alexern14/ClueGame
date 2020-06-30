/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * This class tests the reading in of the game board files is correct
 */

package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class FileInitTests {

	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 26;
	public static final int NUM_COLUMNS = 26;

	// NOTE: I made Board static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test.
	private static Board board;

	// Do this before any of the tests, because each test will use these
	// No need to setup before each individual test
	@BeforeClass
	public static void setUp() {
		board = Board.getInstance();	// Board is singleton, get the only instance

		board.setConfigFiles("BoardLayout.csv", "ClueRooms.txt");	//pre-made files

		board.initialize();	// Initialize will load both config files 
	}
	
	// Test that the legend is read in correctly
	@Test
	public void testRooms() {
		// Get the map of initial => room 
		Map<Character, String> legend = board.getLegend();
		// Ensure we read the correct number of rooms
		assertEquals(LEGEND_SIZE, legend.size());
		// To ensure data is correctly loaded, test retrieving rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Bathroom", legend.get('B'));
		assertEquals("Dining room", legend.get('D'));
		assertEquals("Family room", legend.get('F'));
		assertEquals("Garage", legend.get('G'));
		assertEquals("Kitchen", legend.get('K'));
		assertEquals("Master bedroom", legend.get('M'));
		assertEquals("Office", legend.get('O'));
		assertEquals("Play room", legend.get('P'));
		assertEquals("Theatre", legend.get('T'));
		assertEquals("Walkway", legend.get('W'));
		assertEquals("Closet", legend.get('X'));

	}

	// Test the board dimensions are correct
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}

	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus 
	// two cells that are not a doorway.
	// These tests are WHITE on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell room;
		
		room = board.getCellAt(4, 6);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		
		room = board.getCellAt(17, 4);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		
		room = board.getCellAt(13, 7);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		
		room = board.getCellAt(6, 2);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(3, 21);
		assertFalse(room.isDoorway());	
		
		// Test that walkways are not doors
		BoardCell walkway = board.getCellAt(14, 16);
		assertFalse(walkway.isDoorway());		

	}
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		for (int row = 0; row < board.getNumRows(); row++)
			for (int col = 0; col < board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		Assert.assertEquals(14, numDoors);
	}

	// Test random room cells to ensure the room initial is correct.
	@Test
	public void testRoomInitials() {
		// Test random cells from each room
		assertEquals('D', board.getCellAt(0, 0).getInitial());
		assertEquals('G', board.getCellAt(0, 19).getInitial());
		assertEquals('B', board.getCellAt(21, 10).getInitial());
		assertEquals('O', board.getCellAt(13, 23).getInitial());
		assertEquals('M', board.getCellAt(25, 25).getInitial());
		assertEquals('F', board.getCellAt(22, 3).getInitial());
		assertEquals('P', board.getCellAt(2, 12).getInitial());
		assertEquals('K', board.getCellAt(13, 5).getInitial());
		assertEquals('T', board.getCellAt(18, 15).getInitial());
		
		// Test a walkway
		assertEquals('W', board.getCellAt(7, 8).getInitial());
		
		// Test the closet
		assertEquals('X', board.getCellAt(11,11).getInitial());
	}
}
