package observer;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.Covid19Pacient;
import domain.Symptom;

import javax.swing.JLabel;

public class PacientObserverGUI extends JFrame implements Observer {

    private JPanel contentPane;
    private final JLabel symptomLabel = new JLabel("");
    private Covid19Pacient pacient;

    public PacientObserverGUI(Covid19Pacient pacient) {
        this.pacient = pacient;
        setTitle("Pacient symptoms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(650, 100, 200, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        symptomLabel.setBounds(19, 38, 389, 199);
        contentPane.add(symptomLabel);
        symptomLabel.setText("Still no symptoms");
        this.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        String s = "<html>Symptoms:<br>";
        Iterator<Symptom> it = pacient.getSymptoms().iterator();
        while (it.hasNext()) {
            Symptom symptom = it.next();
            s += symptom.getName() + " (weight: " + pacient.getWeight(symptom) + ")<br>";
        }
        s += "</html>";
        symptomLabel.setText(s);
    }
}
