package LightsOut;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 5x5 Lights Out game in which users toggle buttons between 
 * white and black with the goal to switch all buttons to black (off)
 * position.
 * @author Jared Knight CS1410 Assignment 9
 *
 */
public class LightsOut extends JPanel implements ActionListener {
	
	private Buttons[][] buttons; // two-dimension Buttons array for button location
	private JButton reset = new JButton("Reset"); // reset game button
	private JButton quit = new JButton("Quit"); //quit game button
	private JButton hint = new JButton("Hint"); // get hint button
	private JPanel game = new JPanel(new GridLayout(5,5)); // JPanel to hold game
	private JLabel count = new JLabel("Clicks: " + 0); // click counter JLabel
	private Timer time; // timer for flashinLights  win
	private int dialog; // holds value for JOptionPane button selected
	private int counter = 0; // click counter count
	
	
	/**
	 * Lights Out constructor initializes a 5x5 game
	 * Fills with buttons in off state
	 */
	public LightsOut() {
		super();
		
		// builds layout of 
		buildLayout();
		// initializes two-dimension array
		buttons = new Buttons[5][5];
		// fill 5x5 game with buttons in off position
		for(int i = 0; i < 5.; i++) {
			for(int j = 0; j < 5; j++) {
				buttons[i][j] = new Buttons(1, i, j);
				game.add(buttons[i][j]); // to the panel
				buttons[i][j].addActionListener(this); // the panel provides the interaction logic
			}
		}
		
		// make sure state doesn't randomize to gameWinner
		while(isGameWinner()) {
			// simulates random 10 clicks
			initializeLights(10);
		}
	}
	
	/**
	 * Listens for events on buttons and performs tasks for each
	 */
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == reset) {
			// resets to new game
			reset();
		} else if(e.getSource() == quit) {
			// Exits Program 
			System.exit(0);
		} else if(e.getSource() == hint) {
			// checks if game is in state for hint
			// displays appropriate JOptionPane message
			if(readyForHint()) {
				getHintMessage();
			} else {
				JOptionPane.showMessageDialog(null,
						"A hint is only provided when all lights are off except on the bottom row...", 
						"Hint of hint", JOptionPane.PLAIN_MESSAGE);
			}
		 } else {
				
			//Toggles button and neighbors colors
			// stores button of event
			Buttons button = (Buttons)e.getSource(); 
			// gets button row and column for button
			int buttonRow = button.buttonRow();
			int buttonColumn = button.buttonColumn();
			// toggles clicked button color
			button.toggleValues(button.buttonState()); 
			// toggle neighbor button color
			toggleNeighbors(buttonRow, buttonColumn);	
			//Adds to count of clicks
			counter++;
			// updates JLabel text
			count.setText("Clicks: " + counter);
			
			// if game is won flash light, give dialog options
			if(isGameWinner()) {
				gameWonTheatrics();
				
				// listen for reset and quit buttons to be pressed
				if(dialog == 0) {
					// ends flashing lights
					time.stop();
					// reset game
					reset();
				} else if(dialog == 1) {
					// exit game
					System.exit(0);
				}
			}
		}
		
	}
	
	/**
	 * Toggles lights of north, south, east, and west of clicked button.
	 * 
	 * @param i row number of clicked button
	 * @param j column number of clicked button
	 */
	private void toggleNeighbors(int i, int j) {
		// toggles button North 
		if(i != 0) {
			Buttons buttonNorth = buttons[i - 1][j];
			buttonNorth.toggleValues(buttonNorth.buttonState());
		}
		// toggles button South
		if(i != 4) {
			Buttons buttonSouth = buttons[i + 1][j];
			buttonSouth.toggleValues(buttonSouth.buttonState());
		}
		// toggles button West
		if(j != 0) {
			Buttons buttonWest = buttons[i][j - 1];
			buttonWest.toggleValues(buttonWest.buttonState());
		}
		// toggles button East
		if(j != 4) {
			Buttons buttonEast = buttons[i][j + 1];
			buttonEast.toggleValues(buttonEast.buttonState());
		}
	}
	
	/** 
	 * Makes game solvable by preparing board by simulating random amount of clicks 
	 * 
	 * @param i number of times to simulate clicks
	 */
	public void initializeLights(int i) {
		Random randNum = new Random();
		int randomRow; // row number
		int randomColumn; // column number
		
		// gets random row and column for button,
		// toggles selected button and neighbors
		for(int hit = 0; hit < i; hit++) {
			randomRow = randNum.nextInt(5); // random row number
			randomColumn = randNum.nextInt(5); // random column number
			
			// toggles values at random button and neighbors
			buttons[randomRow][randomColumn].toggleValues(buttons[randomRow][randomColumn].buttonState());
			toggleNeighbors(randomRow, randomColumn);
			
		}
		
	}

	/**
	 * resets board to initial condition
	 */
	private void reset() {
		// reset game to initials
		counter = 0;
		count.setText("Clicks: " + counter);
		blackoutLights(Color.black);
		initializeLights(6);
	}
	
	/**
	 * Sets all lights to off position with specified color
	 */
	private void blackoutLights(Color color) {
		//Loops through array to switch lights off
		for(int i = 0; i < 5; i++) 
			for(int j = 0; j < 5; j++)
				buttons[i][j].startState(color);
	}
	
	/**
	 * Checks to see if game has all lights off above last row,
	 * 
	 * @return  true if lights in rows 0 through 3 are off. False otherwise.
	 */
	private boolean readyForHint() {
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 5; j++) {
				if(buttons[i][j].buttonState() == 0) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Creates string to represent bottom row current state
	 * @return string of current state
	 */
	private int getBottomRow() {
		String row = "";
			for(int i = 0; i < 5; i++) {
				if(buttons[4][i].buttonState() == 0)
					row += 0;
				else 
					row += 1;
			}
			int rowNum = Integer.parseInt(row);
		return rowNum;
	}
	
	/** 
	 * Checks to see if all lights are off
	 * 
	 * @return boolean false of lights are on, true otherwise
	 */
	private boolean isGameWinner() {
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if(buttons[i][j].buttonState() == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Uses timer to change each button to random color every specified amount of time
	 */
	private void flashingLights() {
		ActionListener taskPerformer = new ActionListener() {
			  // changes to random color once for each button
		      public void actionPerformed(ActionEvent evt) {
		    	  for(int i = 0; i < 5.; i++) {
		  			for(int j = 0; j < 5; j++) {
			    	    int red = (int) (Math.random() * 256);
			    	    int green = (int) (Math.random() * 256);
			    	    int blue = (int) (Math.random() * 256);
			    	    Color c = new Color(red,green, blue);
			            buttons[i][j].setBackground(c);
		  			}
		    	  }
		      }
		  };
		 // creates timer for action event
		time = new Timer(100, taskPerformer); 
		// starts timer
		time.start();
	}
	
	/**
	 * Calls method for flashing lights, imports icon and builds JOptionPain
	 * to display message as well as to give reset and quit options
	 */
	private void gameWonTheatrics() {
		flashingLights();
		//import Icon
		ImageIcon icon = new ImageIcon("../A9_KNIGHT_JARED/src/Resources/64x64.png");
		// create option buttons ad pane
	    Object[] option = {"Restart", "Quit"};
	    dialog = JOptionPane.showOptionDialog(null, 
		      "You are a Winner!!", 
		      "WINNER", 
		      JOptionPane.DEFAULT_OPTION, 
		      JOptionPane.INFORMATION_MESSAGE,icon, option, null);
	    
	}
	
	/**
	 * Creates JOptionPane to display hint based on bottom row state
	 */
	private void getHintMessage() {
		
		int bottomRow = getBottomRow();
		switch (bottomRow) {
			case 1110 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row from left to right, toggle 1st and 2nd lights on, "
						+ "then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
			case 10101 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row counting from left to right, toggle 1st and 4th " 
				+ "lights on, then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
			case 11 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row counting from left to right, toggle 2nd light on, "
						+ "then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
			case 11000 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row counting from left to right, toggle 4th light on, "
						+ "then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
			case 1001 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row counting from left to right, toggle 5th light on, "
						+ "then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
			case 10010 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row counting from left to right, toggle 1st light on, "
						+ "then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
			case 100 :
				JOptionPane.showMessageDialog(null,
						"On the Top Row counting from left to right, toggle 3rd light on, "
						+ "then solve top down.", 
						"Hint", JOptionPane.PLAIN_MESSAGE);
				break;
		}
    }
	
	/**
	 * Sets GUI layout and adds buttons and Action Listeners
	 */
	private void buildLayout() {
		// set super layout
				setLayout(new GridBagLayout());
				GridBagConstraints c = new GridBagConstraints();
				// create button for reset
				
				// add hint button and action listener
				c.fill = GridBagConstraints.HORIZONTAL;
				c.insets = new Insets(10, 10, 10, 10);  //padding
				c.weightx = .8;
				c.gridx = 0;
				c.gridy = 0;
				add(hint, c);
				hint.addActionListener(this);
				
				// add count
				c.insets = new Insets(10, 30, 10, 10);
				c.weightx = .1;
				c.gridx = 1;
				c.gridy = 0;
				add(count, c);
				
				// create game panel in super panel
				c.fill = GridBagConstraints.BOTH;
				c.insets = new Insets(0, 10, 0, 10);
				c.gridy = 1;
				c.gridx = 0;
				c.gridwidth = 2;
				c.weightx = 1.0;
				c.weighty = 1.0;
				add(game, c);
				
				//add reset button and action listener
				c.anchor = GridBagConstraints.PAGE_END; //bottom of space
				c.insets = new Insets(10, 10, 10, 10);  //padding
				c.weightx = 0;
				c.weighty = 0;
				c.gridwidth = 1;
				c.gridx = 0;
				c.gridy = 2;
				add(reset, c);
				reset.addActionListener(this);
				
				//add quit button and action listener
				c.gridx = 1;
				c.gridy = 2;
				add(quit, c);
				quit.addActionListener(this);
	}
	

	/**
	 * Creates JFrame and initializes conditions. Initializes Lights Out Game
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Launches and creates main window JFrame 
		JFrame mainWindow = new JFrame("Lights Out");
		// Makes JFrame visible
		mainWindow.setVisible(true);
		// Exits when closed
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Machines have issues with colors without this
		try {
			 UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
		// creates LightsOut instance
		LightsOut game = new LightsOut();
		// assigns game to content pane
		mainWindow.setContentPane(game);
		// specifies starting size of JFrame
		mainWindow.setPreferredSize(new Dimension(500, 500));
		mainWindow.pack();
		
	}

}
