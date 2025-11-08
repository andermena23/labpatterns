package adapter;

import domain.Covid19Pacient;
import domain.Symptom;
import java.util.Iterator;

public class Main {
    
    public static void main(String[] args) {
        // Covid19Pacient objektu bat sortu 5 sintomekin
        Covid19Pacient pacient = new Covid19Pacient("Ander", 25);
        
        // 5 sintomak gehitu
        pacient.addSymptom(new Symptom("Cough", 3, 2), 5);
        pacient.addSymptom(new Symptom("Fever", 4, 4), 7);
        pacient.addSymptom(new Symptom("Headache", 2, 1), 3);
        pacient.addSymptom(new Symptom("Fatigue", 3, 3), 6);
        pacient.addSymptom(new Symptom("Breathlessness", 5, 5), 8);
        
        System.out.println("Pazientea: " + pacient.getName());
        System.out.println("Sintoma kopurua: " + pacient.getSymptoms().size());
        System.out.println();
        
        // Covid19PacientAdapter sortu
        Covid19PacientAdapter adapter = new Covid19PacientAdapter(pacient);
        
        // Comparator-eak sortu
        SymptomNameComparator nameComparator = new SymptomNameComparator();
        SeverityIndexComparator severityComparator = new SeverityIndexComparator();
        
        // Lehendabizi symptomName ordenatuaz inprimatu
        System.out.println("=== Sintomak symptomName arabera ordenatuta ===");
        Iterator<Object> sortedByName = Sorting.sortedIterator(adapter, nameComparator);
        while (sortedByName.hasNext()) {
            Symptom s = (Symptom) sortedByName.next();
            System.out.println("  - " + s.getName() + " (severityIndex: " + s.getSeverityIndex() + ")");
        }
        System.out.println();
        
        // Adapter berri bat sortu bigarren ordenaziorako
        adapter = new Covid19PacientAdapter(pacient);
        
        // Jarraian severityIndex ordenatuaz inprimatu
        System.out.println("=== Sintomak severityIndex arabera ordenatuta ===");
        Iterator<Object> sortedBySeverity = Sorting.sortedIterator(adapter, severityComparator);
        while (sortedBySeverity.hasNext()) {
            Symptom s = (Symptom) sortedBySeverity.next();
            System.out.println("  - " + s.getName() + " (severityIndex: " + s.getSeverityIndex() + ")");
        }
    }
}
