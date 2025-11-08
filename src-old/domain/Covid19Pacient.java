package domain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import iterator.Covid19PacientIterator;

public class Covid19Pacient {
	private String  name; 
	private int age;
	private Map<Symptom,Integer> symptoms=new HashMap<Symptom,Integer>();

	public Covid19Pacient(String name, int years) {
		this.name = name;
		this.age = years;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getWeight(Symptom s) {
		return symptoms.get(s);
	}

	public Set<Symptom> getSymptoms() {
		return symptoms.keySet();
	}
	
	public Symptom getSymptomByName(String symptomName) {
		Iterator<Symptom> i= getSymptoms().iterator();
		Symptom s=null;
		while (i.hasNext()) {
			s=i.next();
			if (s!=null && s.getName().equals(symptomName)) return s;
		}
		return null;	
	}

	public void addSymptom(Symptom c, Integer w){
		symptoms.put(c,w);
	}
	
	public Symptom addSymptomByName(String symptom, Integer w){
		Symptom s=null;
		s=createSymptom(symptom); 
		if (s!=null) 
			symptoms.put(s,w);		
		return s;
	}

	public Symptom removeSymptomByName(String symptomName) {
		Symptom s=getSymptomByName(symptomName);
		System.out.println("Simptom to remove: "+s);
		if (s!=null) symptoms.remove(s);
		return s;
	}
	public Iterator iterator() {
		return new Covid19PacientIterator(this.symptoms.keySet());
	}
	
	public double covidImpact() {
		double afection=0;
		double increment=0;
		double impact=0;
		
		//calculate afection
		for (Symptom c: symptoms.keySet()) {
			if (c!=null )
			   afection=afection+c.getSeverityIndex()*symptoms.get(c);
		}
		afection=afection/symptoms.size();
		
		//calculate increment
		if (getAge()>65) increment=afection*0.5;
		
		//calculate impact
		impact=afection+increment;
		return impact;
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
		
	}
}

