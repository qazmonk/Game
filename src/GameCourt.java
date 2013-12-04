/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * GameCourt
 * 
 * This class holds the primary game logic of how different objects 
 * interact with one another.  Take time to understand how the timer 
 * interacts with the different methods and how it repaints the GUI 
 * on every tick().
 *
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

	// the state of the game logic
	        // the Poison Mushroom, doesn't move
	ArrayList<Planet> board;
	public boolean playing = false;  // whether the game is running
	private JLabel status;       // Current status text (i.e. Running...)
	//afslhsa
	// Game constants
	public int COURT_WIDTH = 600;
	public int COURT_HEIGHT = 600;
	public double PIXELS_PER_METER = COURT_WIDTH/13.4;
	public static final int SQUARE_VELOCITY = 4;
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 35; 
	
	private Birdie b;

	public GameCourt(JLabel status, int w, int h) {
		
		
		// creates border around the court area, JComponent method
		COURT_WIDTH = w;
		COURT_HEIGHT = h;
		PIXELS_PER_METER = COURT_WIDTH/13.4;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		
        
		
        // The timer is an object which triggers an action periodically
        // with the given INTERVAL. One registers an ActionListener with
        // this timer, whose actionPerformed() method will be called 
        // each time the timer triggers. We define a helper method
        // called tick() that actually does everything that should
        // be done in a single timestep.
		Timer timer = new Timer(INTERVAL, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tick();
			}
		});
		timer.start(); // MAKE SURE TO START THE TIMER!

		// Enable keyboard focus on the court area
		// When this component has the keyboard focus, key
		// events will be handled by its key listener.
		setFocusable(true);

		// this key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually 
		// moves the square.)
		

		this.status = status;
	}

	public void init(String boardFile) throws IOException{
		b = new Birdie(PIXELS_PER_METER);
		b.setPosition(10, 500);
		b.setVelocity(COURT_WIDTH, -COURT_HEIGHT);
		
		
		
	}
	/** (Re-)set the state of the game to its initial state.
	 */
	public void reset() {

		
		playing = true;
		status.setText("Running...");
		
		b = new Birdie(PIXELS_PER_METER);
		b.setPosition(10, 500);
		b.setVelocity(COURT_WIDTH, -COURT_HEIGHT);

		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

    /**
     * This method is called every time the timer defined
     * in the constructor triggers.
     */
	void tick(){
		if (playing) {
			// advance the square and snitch in their
			// current direction.
			b.update(INTERVAL/1000.0);
			Point p = b.getPosition();
			if (p.y > COURT_HEIGHT) {
				b.setPosition(p.x, COURT_HEIGHT);
				Point v = b.getVelocity();
				b.setVelocity(v.x, -v.y);
			}
			// update the display
			repaint();
		} 
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		b.paint(g);
		
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_WIDTH,COURT_HEIGHT);
	}
}
