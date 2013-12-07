import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Player extends GameObj{
	private boolean facesRight;
	
	private static double WALK_SPEED;
	
	
	private double jumpStrength = -100;
	private Color c = Color.BLUE;
	
	
	public Player(boolean right, double ppm, int max_x, int max_y) {
		facesRight = right;
		PIXELS_PER_METER = ppm;
		
		
		pos_x = 0;
		pos_y = 0;
		
		v_x = 0;
		v_y = 0;
		
		this.max_x = max_x;
		this.max_y = max_y;
		
		width = toPixels(0.45);
		height = toPixels(1.55);
		WALK_SPEED = toPixels(10);
	}
	

	
	public void moveRight() {
		v_x = WALK_SPEED;
	}
	public void moveLeft() {
		v_x = -WALK_SPEED;
	}
	public void stopMoving() {
		v_x = 0;
	}
	public void hitGround() {
		v_y = 0;
	}
	public void jump() {
		v_y = jumpStrength;
	}
	
	public void update(double dt) {
		
		v_y += toPixels(grav)*dt;
		pos_x += v_x*dt;
		pos_y += v_y*dt;
		
	}
	
	public void hitWall(Point n) {
		if (n.x == 0) {
			hitGround();
		}
		else {
			stopMoving();
		}
	}
	
	public void  paint(Graphics g) {
		g.setColor(c);
		g.fillRect((int)(pos_x-width/2), (int)(pos_y-height/2), (int)width, (int)height);
		
	}
	

}
