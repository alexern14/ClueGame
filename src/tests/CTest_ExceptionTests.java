package tests;

/*
 * This program tests that, when loading config files, exceptions 
 * are thrown appropriately.
 */

import java.io.FileNotFoundException;

import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;

public class CTest_ExceptionTests {

	// Test that an exception is thrown for a config file that does not 
	// have the same number of columns for each row
	@Test (expected = BadConfigFormatException.class)
	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException {
		// Note that we are using a LOCAL Board variable, because each 
		// test will load different files
		Board board = Board.getInstance();
		board.setConfigFiles("CTest_ClueLayoutBadColumns.csv", "CTest_ClueLegend.txt");
		// Instead of initialize, we call the two load functions directly.
		// This is necessary because initialize contains a try-catch. 
		board.loadRoomConfig();
		// This one should throw an exception
		board.loadBoardConfig();
	}
	
	// Test that an exception is thrown for a config file that specifies 
	// a room that is not in the legend. See first test for other important comments. 
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
		Board board = Board.getInstance();
		board.setConfigFiles("CTest_ClueLayoutBadRoom.csv", "CTest_ClueLegend.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	
	// Test that an exception is thrown for a config file with a room type 
	// that is not Card or Other
	@Test (expected = BadConfigFormatException.class)
	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
		Board board = Board.getInstance();
		board.setConfigFiles("CTest_ClueLayout.csv", "CTest_ClueLegendBadFormat.txt");
		board.loadRoomConfig();
	}

}
