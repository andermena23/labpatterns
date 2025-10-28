package observer;

import java.awt.Color;
import java.awt.Frame;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import domain.Covid19Pacient;

public class SemaphorGUI extends JFrame implements Observer {
    private Covid19Pacient pacient;
    
    public SemaphorGUI(Covid19Pacient pacient) {
        this.pacient = pacient;
        setSize(100, 100);
        setLocation(350,10);
        Color c=Color.green;
        getContentPane().setBackground(c);
        repaint();
        setVisible(true);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        double impact = pacient.covidImpact();
        Color c;
        if (impact < 5) c = Color.green;
        else if (impact < 10) c = Color.yellow;
        else c = Color.red;
        getContentPane().setBackground(c);
        repaint();
    }
}

