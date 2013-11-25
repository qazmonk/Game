import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class MainMenu extends JPanel{
	
	private int WIDTH, HEIGHT;
	public MainMenu(int width, int height, ArrayList<JButton> buttons) {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
		
		WIDTH = width;
		HEIGHT = height;
		column.add(Box.createVerticalGlue());
		for (JButton button: buttons) {
			column.add(button);
		}
		column.add(Box.createVerticalGlue());
		this.add(Box.createHorizontalGlue());
		this.add(column);
		this.add(Box.createHorizontalGlue());
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}

}
