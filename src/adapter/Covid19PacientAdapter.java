package adapter;

import domain.Covid19Pacient;
import domain.Symptom;
import java.util.ArrayList;
import java.util.List;

public class Covid19PacientAdapter implements InvertedIterator {
    
    private List<Symptom> symptoms;
    private int position;
    
    public Covid19PacientAdapter(Covid19Pacient pacient) {
        this.symptoms = new ArrayList<>(pacient.getSymptoms());
        this.position = 0;
    }
    
    @Override
    public Object previous() {
        if (hasPrevious()) {
            Object symptom = symptoms.get(position);
            position--;
            return symptom;
        }
        return null;
    }
    
    @Override
    public boolean hasPrevious() {
        return position >= 0;
    }
    
    @Override
    public void goLast() {
        position = symptoms.size() - 1;
    }
}
