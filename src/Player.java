import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;


public class Player extends GameObj{
	private boolean facesRight;
	private double drag = 0.5;
	private static double WALK_SPEED;
	private static double MAX_SPEED;
	
	private static final boolean DEBUG = false;
	public boolean UP = false, LEFT = false, RIGHT = false, HIT = false;
	
	private boolean onGround = false;
	private double jumpStrength = 400;
	private Color c = Color.BLUE;
	
	private int hitCount = 0;
	private static final int hitSpeed = 7;
	
	private double smashStrength = 80;
	private double hitStrength = 40;
	private double dropStrength = 15;
	private double serveStrength = 25;
	private double arm_x, arm_y;
	
	private double range_x, range_y;
	
	private Birdie birdie;
	
	public boolean isServing = false;
	public boolean isBeingServedTo = false;
	
	private boolean notMoving = false;
	
	private double racketPos = Math.PI;
	private static final double racketSpeed = Math.PI/6;
	private boolean swinging = false;
	private int swingCount = 0;
	
	private PlayerAI opp;
	
	public Player(boolean right, double ppm, int min_x, int min_y, int max_x, int max_y) {
		
		
		
		this.initVars(right, ppm, min_x, min_y, max_x, max_y);
		
		
		double[] t = getHitVector(0, arm_x-range_y/2);
	}
	
	public Player(boolean right, double ppm, int min_x, int min_y, int max_x, int max_y, PlayerAI o) {
		this.initVars(right, ppm, min_x, min_y, max_x, max_y);
		opp = o;
	}
	
	public void setOpponent(PlayerAI o) {
		opp = o;
	}
	private void initVars(boolean right, double ppm, int min_x, int min_y, int max_x, int max_y) {
		facesRight = right;
		PIXELS_PER_METER = ppm;
		
		
		pos_x = 0;
		pos_y = 0;
		
		v_x = 0;
		v_y = 0;
		
		this.max_x = max_x;
		this.max_y = max_y;
		this.min_x = min_x;
		this.min_y = min_y;
		
		width = toPixels(0.7);
		height = toPixels(1.2);
		arm_x = 0;
		arm_y = -toPixels(1.2*0.3);
		WALK_SPEED = toPixels(0.5);
		MAX_SPEED = toPixels(4);
		range_x = toPixels(1.3);
		range_y = toPixels(1.2);
		
		smashStrength = toPixels(smashStrength);
		hitStrength = toPixels(hitStrength);
		dropStrength = toPixels(dropStrength);
		serveStrength = toPixels(serveStrength);
	}
	
	public void rightPressed() {
		RIGHT = true;
	}
	public void leftPressed() {
		LEFT = true;;
	}
	public void stopMoving() {
		notMoving = true;
	}
	public void allowMoving() {
		notMoving = false;
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
		if (!facesRight) {
			x *= -1;
		}
		double hv_y = x-30;
		double hv_x = -5*y;
		
		double m = Math.sqrt(hv_x*hv_x + hv_y*hv_y);
		
		if (!facesRight) {
			hv_x *= -1;
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
		
		if (notMoving) {
			v_x = 0;
		}
		
		v_y += toPixels(grav)*dt;
		pos_x += v_x*dt;
		pos_y += v_y*dt;
		if (isServing || isBeingServedTo) {
			moveToServePosition();
		}
		if (isServing) {
			grabBirdie();
		}
	
		
		if (HIT && hitCount < 0 ) {
			hitCount = hitSpeed;
			swingCount = hitSpeed;
			
		}
		if (swingCount > 0) {
			if (swing()) {
				isServing = false;
				swingCount = 0;
			}
		}
		if (hitCount > 0) {
			v_x = 0;
		}
		swingCount--;
		hitCount--;
		
		if (racketPos < 0) {
			racketPos = Math.PI;
			swinging = false;
		}
		if (swinging) {
			racketPos -= racketSpeed;
		}
		
	}
	
	public void hitWall(Vec n) {
		if (n.x == 0) {
			hitGround();
		}
		else {
			v_x = 0;
		}
	}
	public void addBirdie(Birdie b) {
		birdie = b;
	}
	public void removeBirdie(Birdie b) {
		birdie = b;
	}
	public boolean swing() {
		
		swinging = true;
		double x = birdie.pos_x - (pos_x + arm_x);
		double y = birdie.pos_y - (pos_y + arm_y);

		double m = Math.sqrt(x * x / range_x / range_x + y * y / range_y
				/ range_y);

		if (m < 1 && y < 0) {
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
			double[] hv = getHitVector(x, y);

			if (isServing) {
				str = serveStrength;
			}
			birdie.v_x = hv[0] * str;
			birdie.v_y = hv[1] * str;

			birdie.lastHitBy = this;
			if (opp != null)
				opp.opponentHit();
			else {
				System.out.println("wut?");
			}
			
			return true;

		}
		
		
		return false;
	}
	
	
	public void  paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		
		g.setColor(c);
		g.fillOval((int)(pos_x-width/2), (int)(pos_y+arm_y-(height/2*0.8)/2), (int)width, (int)(height/2*0.8));
		g2.setColor(Color.black);
		g2.drawOval((int)(pos_x-width/2), (int)(pos_y+arm_y-(height/2*0.8)/2), (int)width, (int)(height/2*0.8));
		
		
		g.setColor(c);
		g.fillRect((int)(pos_x-width/2), (int)(pos_y+arm_y), (int)width, (int)(height/2+Math.abs(arm_y)));
		g2.setColor(Color.black);
		g2.drawRect((int)(pos_x-width/2), (int)(pos_y+arm_y), (int)width, (int)(height/2+Math.abs(arm_y)));
		
		
		int x2 = 0;
		if (facesRight) {
			x2 = (int)(pos_x+range_y*Math.cos(racketPos));
		} else {
			x2 = (int)(pos_x-range_y*Math.cos(racketPos));
		}
		
		g2.setColor(Color.black);
		g.drawLine((int)(pos_x), (int)(pos_y+arm_y), x2, (int)(pos_y+arm_y-range_y*Math.sin(racketPos)));
		Color t = new Color(0, 0, 0, 150);
		
		g.setColor(t);
		
		if (DEBUG) {
			g.fillOval((int)(pos_x-range_x+arm_x), (int)(pos_y-range_y+arm_y), (int)range_x*2, (int)range_y*2);
		}
		
	}
	
	public void grabBirdie() {
		
		
		if (facesRight) {
			birdie.pos_x = pos_x - 0.7*range_x;
		} else {
			birdie.pos_x = pos_x + 0.7*range_x;
		}
		birdie.pos_y = pos_y - 0.5*range_y + arm_y;
		birdie.v_x = 0;
		birdie.v_y = 0;
	}
	public boolean inRange(Birdie b) {
		double x = b.pos_x-(pos_x+arm_x);
		double y = b.pos_y-(pos_y+arm_y);
		double m = Math.sqrt(x*x/range_x/range_x + y*y/range_y/range_y);
		return m < 1;
	}

	public void moveToServePosition() {
		if (facesRight) {
			pos_x = max_x-toPixels(1.98);
		} else {
			pos_x = min_x+toPixels(1.98);
		}
	}

	@Override
	public String toString() {
		return "Player " + ((facesRight) ? "Left" : "Right");
	}
	
	
	

}
