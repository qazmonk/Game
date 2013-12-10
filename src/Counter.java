import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Counter extends GameObj {

	
	
	private int val;
	
	private BufferedImage bi = null;
	
	public Counter(int s, int x, int y) {
		val = s;
		
		try {
			bi = ImageIO.read(new File("numbers.png"));
		} catch (IOException e) {
			System.out.println("error with file");
		}
		pos_x = x;
		pos_y = y;
		width = bi.getWidth();
		height = width;
		System.out.println(width);
	}
	@Override
	public void update(double dt) {
		// do nothing
		
	}

	@Override
	public void hitWall(Vec n) {
		// do nothing

	}

	@Override
	public void paint(Graphics g) {
		int t = val;
		int l = 3;
		
		for (int i = 0; i < l; i++) {
			int d = t%10;
			t /= 10;
			int x = (int)(width*(l/2-i));
			g.drawImage(bi.getSubimage(0, (int)(width*d), (int)width, (int)height),
					(int)(pos_x-width/2)+x, (int)(pos_y-height), (ImageObserver)null);
		}
		
		

	}
	
	public void inc() {
		val++;
	}

}
