package LightsOut;

import java.awt.Color;
import java.awt.Point;

import javax.swing.JButton;

/**
 * Creates custom JButtons with starting state on or off, 
 * knowledge of that state as well as where they are in array
 * relative to other buttons.
 * 
 * @author Jared Knight CS1410 Assignment 9
 *
 */
public class Buttons extends JButton
{
	private static final long serialVersionUID = 1L;
	private int buttonRow; // row button is in
	private int buttonColumn; // column button is in
	private int currentState; // state of button
	private int on = 0; // button is on
	private int off = 1; // button is off
	
	/**
	 * Creates button giving row, column, and current state
	 * @param _currentState
	 * @param row
	 * @param column
	 */
	public Buttons(int _currentState, int row, int column) {
		super();
		buttonRow = row;
		buttonColumn = column;
		currentState = _currentState;
		
		startState(Color.black);
	}
	
	/**
	 * Toggles buttons color and current state
	 *  
	 * @param i current button state
	 */
	public void toggleValues(int i) {
		if(i == on) {
			startState(Color.black);
		} else {
			setBackground(Color.white);
			currentState = on;
		}
	}
	
	/**
	 * Returns Button Row
	 * 
	 * @return button row
	 */
	public int buttonRow() {
		return this.buttonRow;
	}
	
	/**
	 * Returns Button Column
	 * 
	 * @return button Column
	 */
	public int buttonColumn() {
		return this.buttonColumn;
	}
	
	/**
	 * Returns Button State
	 * 
	 * @return button State
	 */
	public int buttonState() {
		return this.currentState;
	}
	
	/**
	 * Sets start state to all black
	 * 
	 * @param color object to set buttons to
	 */
	public void startState(Color color) {
		currentState = off;
		setBackground(color);
	}

}
