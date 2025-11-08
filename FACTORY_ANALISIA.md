# Simple Factory Patroiaren Analisia

## 1. Diseinu Berriaren Deskribapena eta Egindako Aldaketak

### Aurreko Diseinuaren Ahultasunak

**Bertsio zaharrean** (`src-old`), `Covid19Pacient` klaseak `createSymptom()` metodo pribatua zuen bere barruan. Honek ondorengo arazoak eragiten zituen:

1. **Ardura gehiegi**: `Covid19Pacient` klaseak bi ardura nagusi zituen:
   - Pazienteen informazioa kudeatzea (izena, adina, sintomak, impact-a kalkulatzea)
   - Symptom objektuak sortzea (instantziazioa)

2. **Kode bikoiztua**: `Medicament` klaseak ere sintoma objektuak sortu behar baditu, kode berdina errepikatu beharko zen.

3. **Mantentze zailak**: Sintoma berri bat gehitzeko edo existitzen den bat aldatzeko, `Covid19Pacient` klasea (eta agian beste klase batzuk) aldatu beharko genituzke.

4. **Akoplamendu handia**: Paziente klasea Symptom motaren azpi-klaseetara (DigestiveSymptom, NeuroMuscularSymptom, RespiratorySymptom) zuzenean lotuta zegoen.

### Diseinu Berrian Egindako Aldaketak

**Simple Factory Patroia** aplikatuz, honako aldaketa hauek egin dira:

#### 1.1. SymptomFactory Klase Berria

Klase estatiko berri bat sortu da: `factory.SymptomFactory`

**Ardura nagusia**:
- Symptom objektuak sortzea sintomaren izenaren arabera
- Sintoma moten logika (impact, severity index, sintoma mota) zentralizatzea

**Ezaugarriak**:
```java
public static Symptom createSymptom(String symptomName)
```

Metodo honek:
- Sintomaren izena jasotzen du parametro gisa
- Impact maila (5, 3, edo 1) eta severity index-a zehazten ditu
- Sintoma motak (digestive, neuromuscular, respiratory) identifikatzen ditu
- Egokia den Symptom azpi-klasearen instantzia itzultzen du
- `null` itzultzen du sintoma ezezaguna bada

#### 1.2. Covid19Pacient Klasean Egindako Aldaketak

**Ezabatuta**:
- `createSymptom()` metodo pribatua KENDU egin da

**Aldatuta**:
- `addSymptomByName()` metodoa orain `SymptomFactory.createSymptom()` deitzen du:

```java
public Symptom addSymptomByName(String symptom, Integer w) {
    Symptom s = null;
    s = SymptomFactory.createSymptom(symptom);  // Factory-a erabiltzen
    if (s != null) {
        symptoms.put(s, w);
        setChanged();
        notifyObservers();
    }
    return s;
}
```

**Onurak**:
- Klase argiago eta sinpleagoa
- Ardura bakarra: pazienteen gestioa
- Sintomen sorkuntza logikaren independentea

#### 1.3. Medicament Klasean Egindako Aldaketak

`Medicament` klaseak ere `SymptomFactory` erabiltzen du:

```java
public Symptom addSymptomByName(String symptom) {
    Symptom s = null;
    s = SymptomFactory.createSymptom(symptom);  // Factory berdina
    if (s != null) {
        symptoms.add(s);
    }
    return s;
}
```

**Onurak**:
- Ez dago kode bikoizturik
- Bi klaseek factory berdina erabiltzen dute
- Sintomen logika aldatzeko, soilik `SymptomFactory` aldatu behar da

### 1.4. Laburbilduz: Simple Factory Patroiaren Abantailak

1. **Single Responsibility Principle (SRP)**: Klase bakoitzak ardura bakarra du
2. **DRY (Don't Repeat Yourself)**: Sintomen sorrera logika leku bakarrean dago
3. **Errazago mantentzea**: Aldaketa guztiak `SymptomFactory`-n bakarrik
4. **Berrerabilgarritasuna**: Edozein klasek factory-a erabil dezake
5. **Akoplamendu txikiagoa**: Cliente klaseak ez dakite nola sortzen diren sintomak

---

## 2. "Mareos" Sintoma Berria Gehitzea (1 inpaktuarekin)

Sintoma berri bat gehitzeko, **soilik SymptomFactory klasea aldatu behar da**. Hau da Factory patroiaren abantaila nagusietako bat.

### Aldaketak SymptomFactory-n:

#### Aldaketa 1: Impact 1 sintomen zerrendara gehitu
```java
List<String> impact1 = Arrays.asList("nauseas", "vómitos", "congestión nasal", 
                                      "diarrea", "hemoptisis", "congestion conjuntival", 
                                      "mareos");  // <-- GEHITUTA
```

#### Aldaketa 2: Severity index-aren zerrendara gehitu
```java
List<Double> index1 = Arrays.asList(5.0, 4.8, 3.7, 0.9, 0.8, 0.4, 0.4);  // <-- 0.4 gehitu
```

#### Aldaketa 3: Sintoma motaren sailkapenera gehitu
```java
List<String> neuroMuscularSymptom = Arrays.asList("fiebre", "astenia", "cefalea", 
                                                   "mialgia", "escalofrios", 
                                                   "mareos");  // <-- GEHITUTA
```

### Prozesua

1. "Mareos" impact 1 duen sintoma bat da
2. Severity index-a 0.4 da
3. NeuroMuscular sintoma motakoa da
4. Factory-ak automatikoki `NeuroMuscularSymptom` instantzia bat sortuko du

### Aldaketaren Onurak

- **Ez da beste klaserik aldatu behar**: Ez `Covid19Pacient`, ez `Medicament`, ez GUI klaseak
- **Zentalizatuta**: Informazio guztia leku bakarrean
- **Erraza probatzea**: Factory klasea bakarrik probatu behar da
- Beste sintomak sortzeko prozesua ez da ukitzen

---

## 3. Symptom Objektuak Bakarrak Izateko (Singleton-a Factory-arekin)

### Arazoa

Gaur egun, `createSymptom("fiebre")` deitu bakoitzean, **Symptom objektu BERRI bat** sortzen da. Hau da:
- 10 pazientek "fiebre" badute, 10 objektu `NeuroMuscularSymptom` desberdin daude memorian
- Behar baino memoria gehiago erabiltzen da
- Objuktu berdina da, baina instantzia desberdinak dira

### Irtenbidea: Flyweight Pattern + Factory

`SymptomFactory` klasean **cache bat** inplementatu da sintoma objektuak gordetzeko eta berrerabiltzeko.

### Inplementazio Estrategia

#### 3.1. Cache-a Gehitu

```java
public class SymptomFactory {
    
    // Cache to store unique symptom instances
    private static Map<String, Symptom> symptomCache = new HashMap<>();
    
    public static Symptom createSymptom(String symptomName) {
        // Check if symptom already exists in cache
        if (symptomCache.containsKey(symptomName)) {
            return symptomCache.get(symptomName);  // Existitzen den objetua itzuli
        }
        
        // ... [kode berdina sintoma sortzeko] ...
        
        Symptom symptom = null;
        
        if (impact != 0) {
            if (digestiveSymptom.contains(symptomName)) {
                symptom = new DigestiveSymptom(symptomName, (int)index, impact);
            } else if (neuroMuscularSymptom.contains(symptomName)) {
                symptom = new NeuroMuscularSymptom(symptomName, (int)index, impact);
            } else if (respiratorySymptom.contains(symptomName)) {
                symptom = new RespiratorySymptom(symptomName, (int)index, impact);
            }
            
            // Store in cache before returning
            if (symptom != null) {
                symptomCache.put(symptomName, symptom);  // Cache-an gorde
            }
        }
        
        return symptom;
    }
}
```

### Nola Funtzionatzen du?

1. **Lehenengo deialdia**: `createSymptom("fiebre")`
   - Cache-an ez dago
   - Objektu berria sortzen da: `new NeuroMuscularSymptom("fiebre", 87, 5)`
   - Cache-an gordetzen da: `symptomCache.put("fiebre", symptom)`
   - Objektua itzultzen da

2. **Bigarren deialdia**: `createSymptom("fiebre")`
   - Cache-an BADAGO
   - **Objektu berdina** itzultzen da (ez da berria sortzen)
   - Memoria aurrezten da

3. **Beste paziente batek**: `createSymptom("fiebre")`
   - Cache-an existitzen den objektu berdina jasotzen du
   - **Sintoma bakoitzeko objektu BAKARRA** sisteman

### Abantailak

1. **Memoria efizientzia**: 
   - N pazientek sintoma berdina badute, objektu 1 bakarrik memorian
   - Aurreko inplementazioan: N objektu desberdin

2. **Errendimendua**: 
   - Cache-tik objektua lortzea oso azkarra da
   - Ez da beharrezkoa objektu berria sortzea

3. **Erreferentzia berdina**: 
   - Objektu berdina denez, konparaketak `==` operadorearekin egin daitezke
   - Hashcode-ak berdinak dira

4. **Thread-safe potentziala**: 
   - Cache estatikoa denez, hari guztiek cache berdina erabiltzen dute
   - Sinkronizazioa gehitu daiteke behar izanez gero

### Desabantailak eta Kontuan Hartzekoak

1. **Estado partekatu**: 
   - **ARRETA**: Symptom objektuak aldatu egin badaitezke (mutable), arazoak sor daitezke
   - Paziente guztiek aldaketa berdina ikusiko dute
   - **Irtenbidea**: Symptom klasea **immutable** izan behar da (ez daude setter-ik)

2. **Memoria kudeaketa**: 
   - Cache-ak objektuak mantentzen ditu betirako
   - Ez dira inoiz ezabatzen (garbage collector-eak ez ditu askatu)
   - Aplikazio txikientzat ez da arazoa

3. **Paziente-sintoma harremana**: 
   - `Covid19Pacient` klaseak `Map<Symptom, Integer>` erabiltzen du
   - Integer-a pisua da (weight), paziente bakoitzeko desberdina
   - Symptom objektua PARTEKATZEN da, baina pisua EZ

### Pattern Konbinazioa

Inplementazioa **Factory + Flyweight** ereduen konbinazioa da:

- **Factory Pattern**: Objektuen sorkuntza zentralizatzen du
- **Flyweight Pattern**: Objektu berdinak berrerabiltzen ditu memoria aurrezteko

### Probatu eta Egiaztatzea

```java
Symptom s1 = SymptomFactory.createSymptom("fiebre");
Symptom s2 = SymptomFactory.createSymptom("fiebre");

System.out.println(s1 == s2);  // true - objektu BERDINA da
System.out.println(s1.equals(s2));  // true
```

---

## Ondorioak

**Simple Factory Patroia** erabiltzeak kode kalitatea hobetzen du:
- Mantentze errazagoa
- Berrerabilgarritasun handiagoa  
- Arduren banaketa argiago
- Hedagarritasun hobea

**Cache mekanismoa** gehitzeak (Flyweight):
- Memoria optimizatzen du
- Errendimendua hobetzen du
- Objektu bakarren printzipioa gordetzen du

Bi patroiak konbinatuz, diseinu **malgua, eraginkorra eta mantentzen erraza** lortzen da.
