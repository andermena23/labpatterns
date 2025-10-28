package observer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.Observable;
import java.util.Observer;

import domain.Covid19Pacient;

import javax.swing.JLabel;

public class PacientThermometerGUI extends Frame implements Observer {	
    private TemperatureCanvas gauges;
    private final JLabel label = new JLabel("New label");
    private Covid19Pacient pacient;
    
	public PacientThermometerGUI(Observable obs, Covid19Pacient pacient){ 
		super("Temperature Gauge");
		this.pacient = pacient;
        Panel Top = new Panel();
        add("North", Top);
        gauges = new TemperatureCanvas(0,15);
        gauges.setSize(500,280);
        add("Center", gauges);		
        setSize(200, 380);
        setLocation(0, 100);
        setVisible(true);
		// Subscribe this GUI to the provided observable
		if (obs != null) {
			obs.addObserver(this);
		}
    }

    @Override
    public void update(Observable o, Object arg) {
		// The observable 'o' is expected to be the Covid19Pacient that changed.
		if (o instanceof Covid19Pacient) {
			this.pacient = (Covid19Pacient) o;
		}
		gauges.set((int) pacient.covidImpact());
        gauges.repaint();
    }

    class TemperatureCanvas extends Canvas {	
		
		public void set(int level) { current = level; }	
		public int get(){return current;}
		public int getMax(){return Max;}
		public int getMin(){return Min;}
		
		private int Max, Min, current;
		
		public TemperatureCanvas(int min, int max){ Min = min; Max = max; }
		
		public void paint(Graphics g)
		{	Color c;
		    if (current<5) c=Color.green;
		     else if (current<10) c=Color.yellow;
		      else c=Color.red;
			g.setColor(Color.black);
			g.drawRect(left,top, width, height);
			g.setColor(c);
			g.fillOval(left-width/2, top+height-width/3,width*2, width*2);
			g.setColor(Color.black);
			g.drawOval(left-width/2, top+height-width/3,width*2, width*2);
			g.setColor(Color.white);
			g.fillRect(left+1,top+1, width-1, height-1);
			g.setColor(c);
			long redtop = height*(get()-getMax())/(getMin()-getMax());
			g.fillRect(left+1, top + (int)redtop, width-1, height-(int)redtop);
		}
		
		private static final int width = 20;
		private static final int top = 20;
		private static final int left = 100;
		private static final int right = 250;
		private static final int height = 200;
	}
}
