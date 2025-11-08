package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Medicament {
	private String name;
	private List<Symptom> symptoms=new ArrayList<Symptom>();


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

	public Symptom addSymptomByName(String symptom){
		Symptom s=null;
		s=createSymptom(symptom);
		if (s!=null) {
			symptoms.add(s);
		}
		return s;

	}
	
	public void removeSymptom(Symptom s){
		symptoms.remove(s);

	}
	public Iterator<Symptom> getSymptoms() {
		return symptoms.iterator();
	}
	public Symptom getSymptomByName(String symptomName) {
		Iterator<Symptom> i= symptoms.iterator();
		Symptom s=null;
		while (i.hasNext()) {
			s=i.next();
			if (s!=null && s.getName().compareTo(symptomName)==0) return s;
		}
		return null;	
	}
	public Symptom removeSymptomByName(String symptomName) {
		Symptom s=getSymptomByName(symptomName);
		if (s!=null)removeSymptom(s);
		return s;
	}
	private Symptom createSymptom(String symptomName) {
	    List<String> impact5 = Arrays.asList("fiebre", "tos seca", "astenia","expectoracion");
	    List<Double> index5 = Arrays.asList(87.9, 67.7, 38.1, 33.4);
	    List<String> impact3 = Arrays.asList("disnea", "dolor de garganta", "cefalea","mialgia","escalofrios");
	    List<Double> index3 = Arrays.asList(18.6, 13.9, 13.6, 14.8, 11.4);
	    List<String> impact1 = Arrays.asList("nauseas", "vómitos", "congestión nasal","diarrea","hemoptisis","congestion conjuntival");
	    List<Double> index1 = Arrays.asList(5.0, 4.8, 3.7, 0.9, 0.8);
	    
	    List<String> digestiveSymptom=Arrays.asList("nauseas", "vómitos","diarrea");
	    List<String> neuroMuscularSymptom=Arrays.asList("fiebre", "astenia", "cefalea", "mialgia","escalofrios");
	    List<String> respiratorySymptom=Arrays.asList("tos seca","expectoracion","disnea","dolor de garganta", "congestión nasal","hemoptisis","congestion conjuntival");


	    int impact=0;
	    double index=0;
	    if (impact5.contains(symptomName)) {impact=5; index= index5.get(impact5.indexOf(symptomName));}
	      else if (impact3.contains(symptomName)) {impact=3;index= index3.get(impact3.indexOf(symptomName));}
	        else if (impact1.contains(symptomName)) {impact=1; index= index1.get(impact1.indexOf(symptomName));}
	 
	    if (impact!=0)  {
	    	if (digestiveSymptom.contains(symptomName)) return new DigestiveSymptom(symptomName,(int)index, impact);
	    	if (neuroMuscularSymptom.contains(symptomName)) return new NeuroMuscularSymptom(symptomName,(int)index, impact);
	    	if (respiratorySymptom.contains(symptomName)) return new RespiratorySymptom(symptomName,(int)index, impact);
	    }
	    return null;		
		
	}}
