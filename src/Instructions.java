import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.*;


public class Instructions extends JPanel {
	
	private int WIDTH, HEIGHT;
	public Instructions(int width, int height, String i) {
		
		WIDTH = width;
		HEIGHT = height;
		
		
		JLabel text = new JLabel(i);
		this.setLayout(new GridBagLayout());
		this.add(text);
	}
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}
