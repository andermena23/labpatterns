package observer;

import java.util.Observable;

import domain.Covid19Pacient;

public class Main {

    public static void main(String[] args) {
        Covid19Pacient pacient = new Covid19Pacient("John", 35);
        
        // Create observers
        PacientSymptomGUI symptomGUI = new PacientSymptomGUI(pacient);
        PacientObserverGUI observerGUI = new PacientObserverGUI(pacient);
        PacientThermometerGUI thermometerGUI = new PacientThermometerGUI(pacient);
        SemaphorGUI semaphorGUI = new SemaphorGUI(pacient);
        
        // Register observers
        pacient.addObserver(observerGUI);
        pacient.addObserver(thermometerGUI);
        pacient.addObserver(semaphorGUI);
    }
}
