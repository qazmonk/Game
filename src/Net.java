import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class Net extends GameObj {

	
	
	public Net(double ppm, double x, double y) {
		PIXELS_PER_METER = ppm;
		pos_x = x;
		pos_y = y;
		
		height = toPixels(1.7);
		width = toPixels(0.1);
	}
	
	@Override
	public void update(double dt) {
		// do nothing

	}

	@Override
	public void hitWall(Vec n) {
		// do nothing

	}
	
	
	public void collideBirdies(ArrayList<Birdie> bs, double dt) {
		for (Birdie b: bs) {
			double x = (pos_x-b.width/2*Math.signum(b.v_x))-b.pos_x;
			double y = pos_y-b.pos_y;
			
			double t = x/b.v_x/dt;
			
			
			
			
			double vd = Math.sqrt(b.v_x*b.v_x + b.v_y*b.v_y)*dt;
			y = pos_y-height/2-b.pos_y;
			x = pos_x-b.pos_x;
			double m = Math.sqrt(x*x + y*y);
			x /= m;
			y /= m;
			
			double dp = (-b.v_y*dt*x*m+b.v_x*dt*y*m)/vd;		
			
			double t2 = -1;
			
			
			if (dp < b.width/2) {
				double dp2 = (b.v_x*dt*x*m + b.v_y*dt*y*m)/vd;
				double d = Math.sqrt(b.width*b.width/4-dp*dp);
				double d2 = dp2-d;
				
				t2 = d2/vd;
			}
			
			if ((t < t2 || t2 < 0 || Double.isNaN(t2)) && t > 0 && t < 1) {
				
				double ty = b.pos_y+b.v_y*dt*t;
				if (ty > pos_y-height/2 && ty < pos_y+height/2) {
					System.out.println("collide net really");
					b.pos_x = pos_x;
					b.pos_y = ty;
					if (b.v_x > 0)
						b.hitWall(new Vec(-1, 0));
					else
						b.hitWall(new Vec(1, 0));
				}
				
				
			}
			if (t2 < t && t2 > 0 && t2 < 1) {
				System.out.println("collide top " + t2);
				double tx = b.pos_x+b.v_x*dt*t2;
				double ty = b.pos_y+b.v_y*dt*t2;
				
				b.pos_x = tx;
				b.pos_y = ty;
				
				b.hitWall(new Vec(-x, -y));
			}
			
		}
		
	}
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.drawRect((int)(pos_x-width/2), (int)(pos_y-height/2), (int)width, (int)height);

	}

}
