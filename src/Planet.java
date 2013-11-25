
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.*;


public class Planet {
	
	private static final Color ONE = Color.RED;
	private static final Color TWO = Color.BLUE;
	private static final Color NEUTRAL = Color.GRAY;
	private static final Color BRIDGE = Color.BLACK;
	
	private Font f;
	public int x, y, r;
	public int max_pop;
	public int controlled = 0;
	public int e_one, m_one, s_one, a_one;
	public int e_two, m_two, s_two, a_two;
	public ArrayList<Planet> bridgesTo;
	
	private String pop_string;
	
	
	public Planet(int x, int y, int r, int pop) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.max_pop = pop;
		pop_string = Integer.toString(pop);
		
		e_one = 0;
		m_one = 0;
		s_one = 0;
		a_one = 0;
		
		e_two = 0;
		m_two = 0;
		s_two = 0;
		a_two = 0;
		
		f = new Font("Verdana",1,r);
		
		bridgesTo = new ArrayList<Planet>();
		
		String pop_string = Integer.toString(max_pop);
		
		
	}
	
	public void draw(Graphics g) {
		switch (controlled) {
		case 0:
			g.setColor(NEUTRAL);
			break;
		case 1:
			g.setColor(ONE);
			break;
		case 2:
			g.setColor(TWO);
			break;
		}
		
		g.fillOval(x-r, y-r, r*2, r*2);
		
		g.setColor(BRIDGE);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(5));
		
		
		for(Planet p: bridgesTo) {
			double d = Math.sqrt((p.x-x)*(p.x-x) + (p.y-y)*(p.y-y));
			int r1_x = (int)((p.x-x)/d*r);
			int r1_y = (int)((p.y-y)/d*r);
			
			int r2_x = (int)((x-p.x)/d*p.r);
			int r2_y = (int)((y-p.y)/d*p.r);
			
			g2.draw(new Line2D.Float(x+r1_x, y+r1_y, p.x+r2_x, p.y+r2_y));
		}
		
		g.setFont(f);
		
		g.drawChars(pop_string.toCharArray(), 0, pop_string.length(), x, y);
		
		
		
		
		
	}
}
