package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import factory.SymptomFactory;

public class Medicament {
    private String name;
    private List<Symptom> symptoms = new ArrayList<Symptom>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Medicament(String name) {
        super();
        this.name = name;
    }

    public Symptom addSymptomByName(String symptom) {
        Symptom s = null;
        s = SymptomFactory.createSymptom(symptom);
        if (s != null) {
            symptoms.add(s);
        }
        return s;
    }

    public void removeSymptom(Symptom s) {
        symptoms.remove(s);

    }

    public Iterator<Symptom> getSymptoms() {
        return symptoms.iterator();
    }

    public Symptom getSymptomByName(String symptomName) {
        Iterator<Symptom> i = symptoms.iterator();
        Symptom s = null;
        while (i.hasNext()) {
            s = i.next();
            if (s != null && s.getName().compareTo(symptomName) == 0) return s;
        }
        return null;
    }

    public Symptom removeSymptomByName(String symptomName) {
        Symptom s = getSymptomByName(symptomName);
        if (s != null) removeSymptom(s);
        return s;
    }
}
