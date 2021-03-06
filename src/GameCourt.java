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
	private Square square;          // the Black Square, keyboard control
	private Circle snitch;          // the Golden Snitch, bounces
	private Poison poison;          // the Poison Mushroom, doesn't move
	ArrayList<Planet> board;
	public boolean playing = false;  // whether the game is running
	private JLabel status;       // Current status text (i.e. Running...)

	// Game constants
	public int COURT_WIDTH = 600;
	public int COURT_HEIGHT = 600;
	public static final int SQUARE_VELOCITY = 4;
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 35; 

	public GameCourt(JLabel status, int w, int h) {
		
		
		// creates border around the court area, JComponent method
		COURT_WIDTH = w;
		COURT_HEIGHT = h;
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
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					square.v_x = -SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					square.v_x = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					square.v_y = SQUARE_VELOCITY;
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					square.v_y = -SQUARE_VELOCITY;
			}
			public void keyReleased(KeyEvent e){
				square.v_x = 0;
				square.v_y = 0;
			}
		});

		this.status = status;
	}

	public void init(String boardFile) throws IOException{
		board = new ArrayList<Planet>();
		
		Scanner scan = new Scanner(new FileReader(boardFile));
		
		while (scan.hasNext()) {
			int x = scan.nextInt();
			int y = scan.nextInt();
			int r = scan.nextInt();
			int pop = scan.nextInt();
			
			Planet p = new Planet(x, y, r, pop);
			p.controlled = (int)(3.0*Math.random());
			for (Planet p1: board) {
				p.bridgesTo.add(p1);
			}
			board.add(p);
			
			
		}
	}
	/** (Re-)set the state of the game to its initial state.
	 */
	public void reset() {

		square = new Square(COURT_WIDTH, COURT_HEIGHT);
		poison = new Poison(COURT_WIDTH, COURT_HEIGHT);
		snitch = new Circle(COURT_WIDTH, COURT_HEIGHT);

		playing = true;
		status.setText("Running...");

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
			square.move();
			snitch.move();

			// make the snitch bounce off walls...
			snitch.bounce(snitch.hitWall());
			// ...and the mushroom
			snitch.bounce(snitch.hitObj(poison));
		
			// check for the game end conditions
			if (square.intersects(poison)) { 
				playing = false;
				status.setText("You lose!");

			} else if (square.intersects(snitch)) {
				playing = false;
				status.setText("You win!");
			}
			
			// update the display
			repaint();
		} 
	}

	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for (Planet p : board) {
			p.draw(g);
		}
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_WIDTH,COURT_HEIGHT);
	}
}
