package observer;

import domain.Covid19Pacient;

public class Main {

    public static void main(String[] args) {
        Covid19Pacient pacient = new Covid19Pacient("John", 35);
        
        // Create observers
        new PacientSymptomGUI(pacient);
        PacientObserverGUI observerGUI = new PacientObserverGUI(pacient);
        // Pass the observable (pacient) into the thermometer GUI so it can subscribe itself
        new PacientThermometerGUI(pacient, pacient);
        SemaphorGUI semaphorGUI = new SemaphorGUI(pacient);
        
        // Register observers
        pacient.addObserver(observerGUI);
        // thermometerGUI registers itself in its constructor, so don't register it here to avoid double subscription
        pacient.addObserver(semaphorGUI);
    }
}
