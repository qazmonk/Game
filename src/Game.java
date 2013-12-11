/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

/** 
 * Game
 * Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	private static final String instructions = 
			"<html>The rules to the game are very simple. You control a person sized "
			+ "<br>robot who for some reason is playing badminton against another"
			+ "<br>version of the same robot. The rules are exactly the same as "
			+ "<br>standard badmiton. If the birdie lands in your opponents court or"
			+ "<br>your opponent hits it out you get a point. The same goes for your"
			+ "<br>oppenent. The robot badminton league isn't quite as popular as "
			+ "<br>the robot soccer leauge so these robots are fairly simply. The "
			+ "<br>robot can move left and right with the 'a' and 'd' keys an can "
			+ "<br>jump with the 'w' key. When you hit enter your robot swings his"
			+ "<br>racquet over head in a 180 degree arc. You are locked in infinite"
			+ "<br>badminton combot with your robot adversary so play for as long as"
			+ "<br>you like and have fun!</html>";
	private int WIDTH = 900, HEIGHT = 750;
    public void run(){
        // NOTE : recall that the 'final' keyword notes inmutability
		  // even for local variables. 3

        // Top-level frame in which game components live
		  // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300,300);

		  // Status panel
       
        final JLabel status = new JLabel("Main Menu");
        
        
        final Instructions instructPage = new Instructions(WIDTH, HEIGHT, instructions);
        
        
        //Center Screen
        final JPanel screen = new JPanel();
        
        
        frame.add(screen, BorderLayout.CENTER);

        // Main playing area
        final GameCourt court = new GameCourt(WIDTH, HEIGHT);
        
        court.init();
        //frame.add(court, BorderLayout.CENTER);
        final JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		screen.removeAll();
        		screen.add(court);
        		court.reset();
        	
        		frame.repaint();
        		frame.validate();
        	}
        });
        final JButton instruct = new JButton("Instructions");
        instruct.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		screen.removeAll();
        		screen.add(instructPage, BorderLayout.CENTER);
        	
        	
        		frame.repaint();
        		frame.validate();
        	}
        });
        ArrayList<JButton> buttons = new ArrayList<JButton>();
        buttons.add(start);
        buttons.add(instruct);
        final MainMenu main = new MainMenu(WIDTH, HEIGHT, buttons);
        screen.add(main, BorderLayout.CENTER);
        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset
        // button, we define it as an anonymous inner class that is 
        // an instance of ActionListener with its actionPerformed() 
        // method overridden. When the button is pressed,
        // actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    court.reset();
                }
            });
        control_panel.add(reset);
        final JButton returnToMenu = new JButton("Main Menu");
        returnToMenu.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                screen.removeAll();
                screen.add(main, BorderLayout.CENTER);
                
                frame.validate();
                frame.repaint();
            }
        });
        control_panel.add(returnToMenu);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        
    }

    /*
     * Main method run to start and run the game
     * Initializes the GUI elements specified in Game and runs it
     * NOTE: Do NOT delete! You MUST include this in the final submission of your game.
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Game());
    }
}
