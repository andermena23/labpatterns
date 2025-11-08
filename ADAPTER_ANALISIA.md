# ADAPTER Pattern-aren Analisia

## Ariketa Deskribapena

Eskatzen da programa nagusi batean 5 Symptom-a duen Covid19Pacient bat sortu, eta jarraian `Sorting.sortedIterator` metodoa erabiliz, bere sintomak inprimatu:
1. Lehendabizi `symptomName` ordenatuaz
2. Jarraian `severityIndex` ordenatuaz

**Murriztapena**: `Covid19Pacient` eta `Sorting` klasetan **EZIN DA EZER ALDATU**.

---

## Inplementatutako Soluzioa

### 1. Comparator Interfazea Inplementatzen duten Klaseak

#### 1.1. SymptomNameComparator
```java
package adapter;

import java.util.Comparator;
import domain.Symptom;

public class SymptomNameComparator implements Comparator<Object>
```

**Funtzioa**: Sintomak `getName()` metodoa erabiliz alfabetikoki ordenatzea.

**Inplementazioa**:
- `Comparator<Object>` interfazea inplementatzen du
- `compare()` metodoan bi sintomaren izenak konparatzen ditu `String.compareTo()` erabiliz
- Emaitza: alfabeto ordenan jartzen ditu sintomak

#### 1.2. SeverityIndexComparator
```java
package adapter;

import java.util.Comparator;
import domain.Symptom;

public class SeverityIndexComparator implements Comparator<Object>
```

**Funtzioa**: Sintomak `getSeverityIndex()` metodoa erabiliz ordenatzea.

**Inplementazioa**:
- `Comparator<Object>` interfazea inplementatzen du
- `compare()` metodoan bi sintomaren severity indizeak konparatzen ditu `Integer.compare()` erabiliz
- Emaitza: txikitik handira ordena jarraitzen dute sintomak

---

### 2. Covid19PacientAdapter Klasea

```java
package adapter;

import domain.Covid19Pacient;
import domain.Symptom;
import java.util.ArrayList;
import java.util.List;

public class Covid19PacientAdapter implements InvertedIterator
```

**Funtzioa**: `Covid19Pacient` objektua `InvertedIterator` interfazera egokitzea.

**Adapter Pattern-aren Aplikazioa**:
- **Target Interface**: `InvertedIterator` (previous, hasPrevious, goLast metodoak)
- **Adaptee**: `Covid19Pacient` (getSymptoms() metodoa itzultzen du Set<Symptom>)
- **Adapter**: `Covid19PacientAdapter`

**Inplementazio Xehetasunak**:
- Eraikitzaileak `Covid19Pacient` objektua jasotzen du
- Pazientearen sintomak `ArrayList` batean gordetzen ditu (Set-etik List-era bihurtuz)
- Atributuak:
  - `List<Symptom> symptoms`: sintomen zerrenda
  - `int position`: uneko posizioa iterazioan

**Metodoak**:
- `goLast()`: azken elementuan kokatzen da (position = symptoms.size() - 1)
- `hasPrevious()`: position >= 0 denean true itzultzen du
- `previous()`: uneko elementua itzultzen du eta posizioa dekrementatzen du

---

### 3. InvertedIterator Interfazea

```java
package adapter;

public interface InvertedIterator {
    public Object previous();      // uneko elementua itzuli eta aurrekora joan
    public boolean hasPrevious();  // aurreko elementua badago true
    public void goLast();          // azken elementuan kokatu
}
```

**Ezaugarriak**:
- Iterator normalaren kontrakoa da (atzetik aurrera nabigatu)
- `Sorting.sortedIterator()` metodoak erabiltzeko diseinatuta dago

---

### 4. Sorting Klasea (Aldatu GABE)

```java
public static Iterator<Object> sortedIterator(InvertedIterator it, Comparator<Object> comparator)
```

**Funtzionamendua**:
1. ArrayList huts bat sortzen du
2. InvertedIterator-a azken elementuan jartzen du (`goLast()`)
3. Atzetik aurrera elementuak ateratzen ditu eta listan gehitzen (`previous()`)
4. Lista osoa ordenatzen du emandako Comparator-arekin (`Collections.sort()`)
5. Ordenatutako listaren iterator normala itzultzen du

---

### 5. Main Programa

**Egitura**:

#### 5.1. Pazientea sortu eta sintomak gehitu
```java
Covid19Pacient pacient = new Covid19Pacient("Ander", 25);
pacient.addSymptom(new Symptom("Cough", 3, 2), 5);
pacient.addSymptom(new Symptom("Fever", 4, 4), 7);
pacient.addSymptom(new Symptom("Headache", 2, 1), 3);
pacient.addSymptom(new Symptom("Fatigue", 3, 3), 6);
pacient.addSymptom(new Symptom("Breathlessness", 5, 5), 8);
```

#### 5.2. Adapter eta Comparator-eak sortu
```java
Covid19PacientAdapter adapter = new Covid19PacientAdapter(pacient);
SymptomNameComparator nameComparator = new SymptomNameComparator();
SeverityIndexComparator severityComparator = new SeverityIndexComparator();
```

#### 5.3. Lehenengo ordenaketa: symptomName arabera
```java
Iterator<Object> sortedByName = Sorting.sortedIterator(adapter, nameComparator);
```

**Kontuan hartzekoa**: Adapter berria sortu behar da bigarren ordenaziorako, lehen erabilitako adapter-aren posizioa aldatuta dagoelako.

#### 5.4. Bigarren ordenaketa: severityIndex arabera
```java
adapter = new Covid19PacientAdapter(pacient); // Adapter BERRIA!
Iterator<Object> sortedBySeverity = Sorting.sortedIterator(adapter, severityComparator);
```

---

## Exekuzioaren Emaitza

```
Pazientea: Ander
Sintoma kopurua: 5

=== Sintomak symptomName arabera ordenatuta ===
  - Breathlessness (severityIndex: 5)
  - Cough (severityIndex: 2)
  - Fatigue (severityIndex: 3)
  - Fever (severityIndex: 4)
  - Headache (severityIndex: 1)

=== Sintomak severityIndex arabera ordenatuta ===
  - Headache (severityIndex: 1)
  - Cough (severityIndex: 2)
  - Fatigue (severityIndex: 3)
  - Fever (severityIndex: 4)
  - Breathlessness (severityIndex: 5)
```

---

## Adapter Pattern-aren Analisia

### Zergatik Adapter Pattern?

**Problema**: 
- `Sorting.sortedIterator()` metodoak `InvertedIterator` interfazea espero du
- `Covid19Pacient` klaseak ez du inplementatzen `InvertedIterator`
- Ez dugu aldatu behar `Covid19Pacient` (murriztapena)

**Soluzioa**: 
- `Covid19PacientAdapter` klasea sortu
- Interfaze bateraezina bihurtu bateragarria

### Pattern-aren Elementuak

1. **Target (InvertedIterator)**: bezeroak erabili nahi duen interfazea
2. **Adaptee (Covid19Pacient)**: lehendik dagoen klasea, bateraezina Target-arekin
3. **Adapter (Covid19PacientAdapter)**: Target interfazea inplementatzen du eta Adaptee-ko funtzionalitatera deiak egiten ditu
4. **Client (Sorting)**: Target interfazea erabiltzen du Adapter bidez

### Abantailak

✅ **Single Responsibility Principle**: Konbertsio logika bereizita dago  
✅ **Open/Closed Principle**: Covid19Pacient aldatu gabe funtzionalitate berria gehitzen da  
✅ **Berrerabilgarritasuna**: Sorting klasea edozein InvertedIterator inplementaziorekin funtzionatzen du  
✅ **Malgutasuna**: Comparator desberdinak erabiliz ordenaketa modu desberdinak lor daitezke  

### Strategy Pattern-arekin Konbinazioa

Sorteo mota desberdinak lortzeko **Strategy Pattern** ere erabiltzen da:
- **Strategy Interface**: `Comparator<Object>`
- **Concrete Strategies**: `SymptomNameComparator`, `SeverityIndexComparator`
- **Context**: `Sorting.sortedIterator()` metodoa

Horrela, exekuzio denboran hautatu daiteke zein ordenaketa erabili.

---

## Ondorioak

Inplementazioak arrakastaz erakusten du:

1. **Adapter Pattern-a** erabilgarria dela interfaze bateraezineko klaseak integratzeko
2. **Comparator Strategy** pertsonalizatuek ordenaketa modu desberdinak ahalbidetzen dituztela
3. **Covid19Pacient eta Sorting aldatu gabe** emaitza lortu dela
4. **InvertedIterator** abstrakzioak `Sorting` klasea malgua egiten duela

Sistema modularra eta hedagarria da, design pattern egokiak aplikatuz lortu dena.
