package observer;

import java.util.Observable;

import domain.Covid19Pacient;

public class Main {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// First patient
		Observable pacient1 = new Covid19Pacient("aitor", 35);
		PacientObserverGUI observer1 = new PacientObserverGUI(pacient1);
		observer1.setLocation(650, 100);
		PacientThermometerGUI thermo1 = new PacientThermometerGUI(pacient1);
		thermo1.setLocation(0, 100);
		PacientSymptomGUI symptom1 = new PacientSymptomGUI((Covid19Pacient) pacient1);
		symptom1.setLocation(200, 100);
		
		// Second patient
		Observable pacient2 = new Covid19Pacient("maria", 50);
		PacientObserverGUI observer2 = new PacientObserverGUI(pacient2);
		observer2.setLocation(650, 420);
		PacientThermometerGUI thermo2 = new PacientThermometerGUI(pacient2);
		thermo2.setLocation(0, 500);
		PacientSymptomGUI symptom2 = new PacientSymptomGUI((Covid19Pacient) pacient2);
		symptom2.setLocation(200, 420);
	}


}