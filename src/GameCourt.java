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
	ArrayList<Birdie> bs;
	public boolean playing = false;  // whether the game is running
	
	//afslhsa
	// Game constants
	public int COURT_WIDTH = 600;
	public int COURT_HEIGHT = 600;
	public int COURT_LENGTH = 700;
	private Counter score_left;
	private Counter score_right;
	public double PIXELS_PER_METER = COURT_WIDTH/13.4;
	public static final int SQUARE_VELOCITY = 4;
	// Update interval for timer in milliseconds 
	public static final int INTERVAL = 25; 
	
	private Birdie b;
	private Player p, o;
	private Net n;
	private PlayerAI opponentAI;
	private Player serving = null;
	
	private Player lastTouch;
	
	private enum GameState {
		Playing, LeftPlayerServing, RightPlayerServing, PointOver, NotPlaying
	}
	
	private GameState state = GameState.NotPlaying;
	
	
	private int c = 50;

	public GameCourt (int w, int h) {
		
		
		// creates border around the court area, JComponent method
		COURT_WIDTH = w;
		COURT_HEIGHT = h;
		PIXELS_PER_METER = COURT_LENGTH/13.4;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		bs = new ArrayList<Birdie>();
        
		
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
		
		
		addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
            	if (e.getKeyCode() == KeyEvent.VK_D)
            		p.rightPressed();
                else if (e.getKeyCode() == KeyEvent.VK_A)
                    p.leftPressed();
                else if (e.getKeyCode() == KeyEvent.VK_W)
                    p.upPressed();
                else if (e.getKeyCode() == KeyEvent.VK_G)
                	p.hitPressed();
            }
            public void keyReleased(KeyEvent e){
            	if (e.getKeyCode() == KeyEvent.VK_D)
                    p.rightReleased();
            	else if (e.getKeyCode() == KeyEvent.VK_A)
                    p.leftReleased();
            	else if (e.getKeyCode() == KeyEvent.VK_W)
                    p.upReleased();
            	else if (e.getKeyCode() == KeyEvent.VK_G)
                	p.hitReleased();
            }
    });


		// this key listener allows the square to move as long
		// as an arrow key is pressed, by changing the square's
		// velocity accordingly. (The tick method below actually 
		// moves the square.)
		

		
	}

	public void init() {
		
		
		
		
	}
	/** (Re-)set the state of the game to its initial state.
	 */
	public void reset() {

		
		state = GameState.LeftPlayerServing;
		
		
		
		p = new Player(true, PIXELS_PER_METER, 0, 0, COURT_WIDTH/2, COURT_HEIGHT);
		p.pos_x = 200.0;
		p.pos_y = COURT_HEIGHT-p.height/2;
		
		o = new Player(false, PIXELS_PER_METER, COURT_WIDTH/2, 0, COURT_WIDTH, COURT_HEIGHT);
		o.pos_x = 400.0;
		o.pos_y = COURT_HEIGHT-p.height/2;
		
		
		
		b = new Birdie(PIXELS_PER_METER, COURT_WIDTH, COURT_HEIGHT);
		b.pos_x = 10.0;
		b.pos_y = 500;
		b.v_x = COURT_WIDTH;
		b.v_y = -COURT_HEIGHT;
		
		n = new Net(PIXELS_PER_METER, COURT_WIDTH/2, COURT_HEIGHT);
		n.pos_y -= n.height/2;
		
		
		p.addBirdie(b);
		
		p.isServing = true;
		
		opponentAI = new PlayerAI(o, p, b);
		
		bs.add(b);
		
		o.addBirdie(b);
		
	
		score_left = new Counter(0, COURT_WIDTH/3, 200);
		score_right = new Counter(0, COURT_WIDTH*2/3, 200);
		// Make sure that this component has the keyboard focus
		requestFocusInWindow();
	}

    /**
     * This method is called every time the timer defined
     * in the constructor triggers.
     */
	private Player pointFor(double x) {
		if (x > COURT_WIDTH/2 && x < COURT_WIDTH/2 + COURT_LENGTH/2) {
			return p;
		} else {
			return o;
		}
	}
	private boolean out(double x) {
		return x < COURT_WIDTH/2-COURT_LENGTH/2 || 
				x > COURT_WIDTH/2+COURT_LENGTH/2;
	}
	private boolean RightPlayerPoint() {
		
		if (out(b.pos_x)) {
			System.out.println("hit out");
			return b.lastHitBy == p;
		}
		System.out.println("hit in");
		return pointFor(b.pos_x) == o;
	}
	void tick(){
		if (state != GameState.NotPlaying) {
			switch (state) {
			case Playing:
				//System.out.println("playing");
				if (!b.inPlay()) {
					
					if (RightPlayerPoint()) {
						System.out.println("Right won");
						serving = o;
						score_right.inc();
					} else {
						System.out.println("Left won");
						serving = p;
						score_left.inc();
					}
					
					System.out.println("Point Over");
					state = GameState.PointOver;
				}
				if (b.wasHit()) {
					System.out.println(lastTouch + " " + b.lastHitBy);
					lastTouch = b.lastHitBy;
				}
				break;
			case LeftPlayerServing:
				
				System.out.println("Left Serve");
				p.isServing = true;
				p.grabBirdie();
				b.serve();
				o.isServing = false;
				p.moveToServePosition();
				o.moveToServePosition();
				state = GameState.Playing;
				break;
			case RightPlayerServing:
				
				System.out.println("Right Serve");
				p.isServing = false;
				
				b.serve();
				o.isServing = true;
				o.grabBirdie();
				p.moveToServePosition();
				o.moveToServePosition();
				
				state = GameState.Playing;
				break;
			case PointOver:
				p.stopMoving();
				o.stopMoving();
				if (!b.isMoving(INTERVAL/1000.0)) {
					System.out.println("point won by " + serving.toString());
					if (serving == p) {
						state = GameState.LeftPlayerServing;
					} else if (serving == o) {
						state = GameState.RightPlayerServing;
					}
					p.allowMoving();
					o.allowMoving();
					
				
				}
				break;
			} 
			n.collideBirdies(bs, INTERVAL/1000.0);
			// advance the square and snitch in their
			// current direction.
			b.update(INTERVAL/1000.0);
			b.clip();
			
			p.update(INTERVAL/1000.0);
			p.clip();
			
			
			opponentAI.update(INTERVAL/1000.0);
			o.update(INTERVAL/1000.0);
			o.clip();
			
			
			repaint();
		}
		
	}
	
	@Override 
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		b.paint(g);
		p.paint(g);
		n.paint(g);
		o.paint(g);
		
		g.drawRect(COURT_WIDTH/2-2-COURT_LENGTH/2, (int)(COURT_HEIGHT-2*PIXELS_PER_METER), 4, (int)(2*PIXELS_PER_METER));
		g.drawRect(COURT_WIDTH/2-2+COURT_LENGTH/2, (int)(COURT_HEIGHT-2*PIXELS_PER_METER), 4, (int)(2*PIXELS_PER_METER));
		
		score_left.paint(g);
		score_right.paint(g);

	
		
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(COURT_WIDTH,COURT_HEIGHT);
	}
}
