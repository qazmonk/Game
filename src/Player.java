import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class Player extends GameObj{
	private boolean facesRight;
	private double drag = 0.5;
	private static double WALK_SPEED;
	private static double MAX_SPEED;
	
	private static final boolean DEBUG = false;
	private boolean UP = false, LEFT = false, RIGHT = false, HIT = false;
	
	private boolean onGround = false;
	private double jumpStrength = 400;
	private Color c = Color.BLUE;
	
	private int hitCount = 0;
	private static final int hitSpeed = 7;
	
	private double smashStrength = 80;
	private double hitStrength = 40;
	private double dropStrength = 15;
	private double serveStrength = 20;
	private double arm_x, arm_y;
	
	private double range_x, range_y;
	
	private ArrayList<Birdie> birdies;
	
	public boolean isServing = false;
	
	public Player(boolean right, double ppm, int max_x, int max_y) {
		facesRight = right;
		PIXELS_PER_METER = ppm;
		
		
		pos_x = 0;
		pos_y = 0;
		
		v_x = 0;
		v_y = 0;
		
		this.max_x = max_x;
		this.max_y = max_y;
		
		width = toPixels(0.7);
		height = toPixels(1.2);
		arm_x = 0;
		arm_y = -toPixels(1.2*0.5);
		WALK_SPEED = toPixels(0.5);
		MAX_SPEED = toPixels(4);
		range_x = toPixels(1.3);
		range_y = toPixels(1.2);
		
		smashStrength = toPixels(smashStrength);
		hitStrength = toPixels(hitStrength);
		dropStrength = toPixels(dropStrength);
		serveStrength = toPixels(serveStrength);
		
		birdies = new ArrayList<Birdie>();
	}
	

	
	public void rightPressed() {
		RIGHT = true;
	}
	public void leftPressed() {
		LEFT = true;;
	}
	public void stopMoving() {
		v_x = 0;
	}
	public void hitGround() {
		onGround = true;
		v_y = 0;
	}
	public void upPressed() {
		UP = true;
	}
	
	public void rightReleased() {
		RIGHT = false;
	}
	
	public void leftReleased() {
		LEFT = false;
	}
	
	public void upReleased() {
		UP = false;
	}
	
	public void hitPressed() {
		HIT = true;
	}
	public void hitReleased() {
		HIT = false;
	}
	private double[] getHitVector(double x, double y) {
		double hv_y = x-30;
		double hv_x = -5*y;
		
		double m = Math.sqrt(hv_x*hv_x + hv_y*hv_y);
		if (isServing) {
			System.out.println(hv_x + " " + hv_y);
		}
		double[] out = {hv_x/m, hv_y/m};
		return out;
	}
	public void update(double dt) {
		
		
		if (onGround) {
			if (LEFT) {
				v_x += -WALK_SPEED;
			}
			if (RIGHT) {
				v_x += WALK_SPEED;
			}
			if (UP) {
				onGround = false;
				v_y = -jumpStrength;
				v_x *= 0.1;
			}
			if (!LEFT && !RIGHT)
				v_x *= drag;
			
		} 
		
		
		
		if (v_x > MAX_SPEED)
			v_x = MAX_SPEED;
		
		if (v_x < -MAX_SPEED)
			v_x = -MAX_SPEED;
		
		
		v_y += toPixels(grav)*dt;
		pos_x += v_x*dt;
		pos_y += v_y*dt;
		
		if (isServing) {
			if (facesRight) {
				pos_x = max_x-toPixels(1.98);
			} else {
				pos_x = max_x+toPixels(1.98);
			}
			
			Birdie t = birdies.get(0);
			
			if (facesRight) {
				t.pos_x = pos_x - 0.7*range_x;
			} else {
				t.pos_x = pos_x + 0.7*range_x;
			}
			t.pos_y = pos_y - 0.5*range_y + arm_y;
			t.v_x = 0;
			t.v_y = 0;
		}
		
		if (HIT && hitCount <= 0) {
			if (swing()) {
				isServing = false;
			}
			hitCount = hitSpeed;
		}
		if (hitCount > 0) {
			stopMoving();
		}
		hitCount--;
		
	}
	
	public void hitWall(Vec n) {
		if (n.x == 0) {
			hitGround();
		}
		else {
			stopMoving();
		}
	}
	public void addBirdie(Birdie b) {
		birdies.add(b);
	}
	public void removeBirdie(Birdie b) {
		birdies.remove(b);
	}
	public boolean swing() {
		
		for (Birdie b: birdies) {
			double x = b.pos_x-(pos_x+arm_x);
			double y = b.pos_y-(pos_y+arm_y);
			
			
			double m = Math.sqrt(x*x/range_x/range_x + y*y/range_y/range_y);
			
			if (m < 1) {
				double str = hitStrength;	
				if (RIGHT) {
					if (facesRight)
						str = smashStrength;
					else
						str = dropStrength;
				}
				if (LEFT) {
					if (facesRight)
						str = dropStrength;
					else
						str = smashStrength;
				}
				double[] hv = getHitVector(x,y);
				
				if (isServing) {
					str = serveStrength;
				}
				b.v_x = hv[0]*str;
				b.v_y = hv[1]*str;
				
				return true;
				
			}
		}
		
		return false;
	}
	
	
	public void  paint(Graphics g) {
		g.setColor(c);
		g.fillRect((int)(pos_x-width/2), (int)(pos_y-height/2), (int)width, (int)height);
		Color t = new Color(0, 0, 0, 150);
		
		g.setColor(t);
		if (DEBUG) {
			g.fillOval((int)(pos_x-range_x+arm_x), (int)(pos_y-range_y+arm_y), (int)range_x*2, (int)range_y*2);
		}
		
	}
	

}
