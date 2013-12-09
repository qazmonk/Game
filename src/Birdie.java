import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Birdie extends GameObj {
	
	private double drag = 0.2766;
	private double friction = 0.8;
	private double bounce = 0.5;
	
	
	private Color c = Color.GRAY;
	
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
		v_y -= toPixels(drag*(toMeters(v_y)*toMeters(v_y))*dt*Math.signum(v_y));
		
		v_x -= toPixels(drag*(toMeters(v_x)*toMeters(v_x))*dt*Math.signum(v_x));
		/*
		 * v = ppm*drag*v^2/ppm^2*dt
		 * ppm/drag/dt = v
		 */
		v_y += toPixels(grav)*dt;
		pos_y += v_y*dt;
		pos_x += v_x*dt;
		
		
	}
	
	public void hitWall(Vec n) {
		
		double d = (v_x*n.x + v_y*n.y)*bounce;
		double d2 = (v_x*(-n.y) + v_y*n.x)*friction;
		
		v_x = -d*n.x+d2*-n.y;
		v_y = -d*n.y+d2*n.x;
		
	}
	public void paint(Graphics g) {
		g.setColor(c);
		
		g.fillOval((int)(pos_x- width/2), (int)(pos_y - width/2), (int)width, (int)height);
		
	}
	
	
	

}
