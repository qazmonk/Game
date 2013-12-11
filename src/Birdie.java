import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Birdie extends GameObj {
	
	private double drag = 0.2766;
	private double friction = 0.8;
	private double bounce = 0.5;
	
	
	private Color c = new Color(255, 252, 26);
	
	
	
	public Player lastHitBy = null;
	private Player lastLastHitBy = null;
	
	private boolean justHit = false;
	private boolean hasHitSomething = false;
	
	public Birdie(double ppm, int max_x, int max_y) {
		PIXELS_PER_METER = ppm;
		
		
		pos_x = 0;
		pos_y = 0;
		
		v_x = 0;
		v_y = 0;
		
		this.max_x = max_x;
		this.max_y = max_y;
		
		width = toPixels(0.2);
		height = width;
		
	
	}
	
	
	public void update(double dt) {
		
		
		justHit = (lastLastHitBy != lastHitBy && lastHitBy != null);
		lastLastHitBy = lastHitBy;
			
		v_y -= toPixels(drag*(toMeters(v_y)*toMeters(v_y))*dt*Math.signum(v_y));
		v_x -= toPixels(drag*(toMeters(v_x)*toMeters(v_x))*dt*Math.signum(v_x));
		v_y += toPixels(grav)*dt;
		pos_y += v_y*dt;
		pos_x += v_x*dt;
		
		
	}
	@Override
	public void hitWall(Vec n) {

		hasHitSomething = true;
		double d = (v_x*n.x + v_y*n.y)*bounce;
		double d2 = (v_x*(-n.y) + v_y*n.x)*friction;
		
		v_x = -d*n.x+d2*-n.y;
		v_y = -d*n.y+d2*n.x;
		
	}
	public void paint(Graphics g) {
		g.setColor(c);
		
		g.fillOval((int)(pos_x- width/2), (int)(pos_y - width/2), (int)width, (int)height);
		
	}
	public void serve() {
		
		hasHitSomething = false;
	}
	public boolean wasHit() {
		return justHit;
	}
	public boolean inPlay() {
		
		return !hasHitSomething;
	}
	
	public double predictXforY(double tar_y, double dt) {
		double x = pos_x;
		double y = pos_y;
		double vx = v_x;
		double vy = v_y;
		tar_y = max_y-tar_y;
		
		System.out.println(x + " " + y + " " + tar_y + " " + max_x/2);
		while (y < tar_y || x < max_x/2) {
			vy -= toPixels(drag*(toMeters(vy)*toMeters(vy))*dt*Math.signum(vy));
			vx -= toPixels(drag*(toMeters(vx)*toMeters(vx))*dt*Math.signum(vx));
			vy += toPixels(grav)*dt;
			y += vy*dt;
			x += vx*dt;
			System.out.println(x + " " + y + " " + tar_y);
		}
		return x;
	}
	
	
	

}
