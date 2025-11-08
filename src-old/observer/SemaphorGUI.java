package observer;

import java.awt.Color;
import java.awt.Frame;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;


public class SemaphorGUI extends JFrame {
	/** stores the associated ConcreteSubject */
	public SemaphorGUI () {
		setSize(100, 100);
		setLocation(350,10);
		Color c=Color.green;
		getContentPane().setBackground(c);
		repaint();
		setVisible(true);
	}
}     

