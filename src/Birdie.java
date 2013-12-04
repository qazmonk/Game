import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Birdie {
	private double x, y, vx, vy;
	private double drag = 0.2766;
	private double pixelsPerMeter;
	private double grav = 9.8;
	private int r = 5;
	private Color c = Color.GRAY;
	
	public Birdie(double ppm) {
		x = 0; y = 0;
		vx = 0; vy = 0;
		pixelsPerMeter = ppm;
		System.out.println(ppm*13.4);
	}
	
	private double toMeters(double p) {
		return p/pixelsPerMeter;
	}
	private int toPixels(double m) {
		return (int)(m*pixelsPerMeter);
	}
	public void update(double dt) {
		vy -= drag*(vy*vy)*dt*Math.signum(vy);
		vx -= drag*(vx*vx)*dt*Math.signum(vx);
		vy += grav*dt;
		y += vy*dt;
		x += vx*dt;
		
		
	}
	public void paint(Graphics g) {
		g.setColor(c);
		
		g.fillOval(toPixels(x) - r, toPixels(y) - r, (int)r*2, (int)r*2);
		
	}
	
	public void setVelocity(double x, double y) {
		vx = toMeters(x);
		vy = toMeters(y);
	}
	public void setPosition(double x, double y) {
		this.x = toMeters(x);
		this.y = toMeters(y);
	}
	public Point getPosition() {
		return new Point(toPixels(x), toPixels(y));
	}
	public Point getVelocity() {
		return new Point(toPixels(vx), toPixels(vy));
	}
	

}
