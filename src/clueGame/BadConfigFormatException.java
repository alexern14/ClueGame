/*
 * Authors: Alexandra Ernst & Mia Belliveau
 * 
 * This class is an exception for when the config files have a bad format
 */

package clueGame;

public class BadConfigFormatException extends Exception{
	public BadConfigFormatException() {
		super ("Bad Configuration Format");	//generic error message
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}
