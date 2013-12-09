/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.Graphics;
import java.awt.Point;

/** An object in the game. 
 *
 *  Game objects exist in the game court. They have a position, 
 *  velocity, size and bounds. Their velocity controls how they 
 *  move; their position should always be within their bounds.
 */
public abstract class GameObj {

	/** Current position of the object (in terms of graphics coordinates)
	 *  
	 * Coordinates are given by the upper-left hand corner of the object.
	 * This position should always be within bounds.
	 *  0 <= pos_x <= max_x 
	 *  0 <= pos_y <= max_y 
	 */
	public double pos_x; 
	public double pos_y;

	/** Size of object, in pixels */
	public double width;
	public double height;
	
	/** Velocity: number of pixels to move every time move() is called */
	public double v_x;
	public double v_y;

	/** Upper bounds of the area in which the object can be positioned.  
	 *    Maximum permissible x, y positions for the upper-left 
	 *    hand corner of the object
	 */
	public double max_x;
	public double max_y;
	
	public double PIXELS_PER_METER;
	
	public double grav = 9.8;

	/**
	 * Constructor
	 */
	
	
	


	/**
	 * Moves the object by its velocity.  Ensures that the object does
	 * not go outside its bounds by clipping.
	 */
	public abstract void update(double dt);

	/**
	 * Prevents the object from going outside of the bounds of the area 
	 * designated for the object. (i.e. Object cannot go outside of the 
	 * active area the user defines for it).
	 */ 
	public void clip(){
		if (pos_x < width/2) {
			pos_x = width/2;
			hitWall(new Vec(1, 0));
		}
		else if (pos_x > max_x-width/2) {
			pos_x = max_x-width/2;
			hitWall(new Vec(-1, 0));
		}

		if (pos_y < height/2){
			pos_y = height/2;
			hitWall(new Vec(0, -1));
		}
		else if (pos_y > max_y-height/2) {
			pos_y = max_y-height/2;
			hitWall(new Vec(0, 1));
		}
	}
	
	abstract public void hitWall(Vec n);

	public double toMeters(double p) {
		return p/PIXELS_PER_METER;
	}
	public double toPixels(double m) {
		return (m*PIXELS_PER_METER);
	}
	

	
	/**
	 * Default draw method that provides how the object should be drawn 
	 * in the GUI. This method does not draw anything. Subclass should 
	 * override this method based on how their object should appear.
	 * 
	 * @param g 
	 *	The <code>Graphics</code> context used for drawing the object.
	 * 	Remember graphics contexts that we used in OCaml, it gives the 
	 *  context in which the object should be drawn (a canvas, a frame, 
	 *  etc.)
	 */
	abstract public void paint(Graphics g);
	
}