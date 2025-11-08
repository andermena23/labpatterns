# Observer Pattern - Programa Nagusiaren Analisia

## Eskaera
Programa nagusia aldatu 2 Covid19Pacient sortzeko bere ondoko PacientSymptomGUI interfazearekin.

## Aldaketak

### src-old/observer/Main.java (Hasierako bertsioa)
```java
public static void main(String[] args) {
    // Hutsa
}
```

### src/observer/Main.java (Inplementatutako bertsioa)
```java
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
```

## Analisia

### 1. Observer Pattern-aren Aplikazioa

Observer Pattern-a implementatu da Covid19Pacient eta haren behatokien artean:

- **Subject (Observable)**: `Covid19Pacient` extends `Observable`
  - Sintomak gehitu/kentzean, `setChanged()` eta `notifyObservers()` deitzen dira
  - Behatzaileak automatikoki eguneratzen dira

- **Observers**: Hiru behatzaile mota:
  - `PacientObserverGUI`: Pazientearen sintomen zerrenda bistaratzen du
  - `PacientThermometerGUI`: Covid impaktua termometro baten moduan erakusten du
  - `PacientSymptomGUI`: Sintomak gehitu/kentzeko interfazea (ez da Observer baina Subject-a aldatzen du)

### 2. Bi Paziente Independente

Programa nagusiak bi paziente independente sortzen ditu:

#### Lehenengo Pazienta (aitor, 35 urte)
- **Kokapena**: Ezkerraldean goian (100-200px-tik hasita)
- **Hiru leiho**:
  - PacientSymptomGUI (200, 100): Sintomak kontrolatzeko interfazea
  - PacientThermometerGUI (0, 100): Termometroa
  - PacientObserverGUI (650, 100): Sintomen zerrenda

#### Bigarren Pazienta (maria, 50 urte)
- **Kokapena**: Behean (420-500px-tik hasita)
- **Hiru leiho**:
  - PacientSymptomGUI (200, 420): Sintomak kontrolatzeko interfazea
  - PacientThermometerGUI (0, 500): Termometroa
  - PacientObserverGUI (650, 420): Sintomen zerrenda

### 3. Funtzionaltatea

#### PacientSymptomGUI Funtzionalitateak:
1. **Sintoma gehitzea**: 
   - ComboBox-etik sintoma bat aukeratu
   - Pisua sartu (1-3 artean)
   - "Add Symptom" botoia sakatu
   - `pacient.addSymptomByName()` deitzen da

2. **Sintoma kentzea**:
   - ComboBox-etik sintoma bat aukeratu
   - "Remove Symptom" botoia sakatu
   - `pacient.removeSymptomByName()` deitzen da

3. **Sintomak eskuragarri**:
   - Pisua 5: fiebre, tos seca, astenia, expectoracion
   - Pisua 3: disnea, dolor de garganta, cefalea, mialgia, escalofríos
   - Pisua 1: náuseas o vómitos, congestión nasal, diarrea, hemoptisis, congestión conjuntival

#### Observer Pattern Fluxua:
1. Erabiltzaileak sintoma bat gehitzen/kentzen du `PacientSymptomGUI`-n
2. `Covid19Pacient` objektuak `setChanged()` eta `notifyObservers()` deitzen ditu
3. Bi observer-ak automatikoki eguneratzen dira:
   - `PacientObserverGUI`: `update()` metodoa exekutatzen da → sintomen zerrenda eguneratzen da
   - `PacientThermometerGUI`: `update()` metodoa exekutatzen da → termometroa eguneratzen da

#### Covid Impact Kalkulua:
```
afection = Σ(symptom.severityIndex * weight) / numberOfSymptoms
increment = (age > 65) ? afection * 0.5 : 0
impact = afection + increment
```

Termometroaren koloreak:
- **Berdea**: impact < 5
- **Horia**: 5 ≤ impact < 10
- **Gorria**: impact ≥ 10

### 4. Pantaila Antolamendua

```
┌─────────────────────────────────────────────────────────────┐
│                                                             │
│  [Thermo1]  [Symptom1]                    [Observer1]      │
│   (0,100)    (200,100)                     (650,100)        │
│                                                             │
│                                                             │
│                                                             │
│                                                             │
│                                                             │
│  [Thermo2]  [Symptom2]                    [Observer2]      │
│   (0,500)    (200,420)                     (650,420)        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### 5. Ondorioak

Inplementazioak arrakastaz aplikatzen du Observer Pattern-a:

✅ **Deskonexioa**: Subject (Covid19Pacient) eta Observer-ak (GUI-ak) deskonektatuta daude

✅ **Eguneratze automatikoak**: Pazientearen egoera aldatzean, GUI guztiak automatikoki eguneratzen dira

✅ **Eskalagarritasuna**: Erraz gehitu daitezke observer berriak paziente bakoitzarentzat

✅ **Independentzia**: Bi paziente guztiz independenteak dira, bakoitzak bere behatzaileak dituena

✅ **Erabiltzaile-interfaze aparta**: `PacientSymptomGUI` ez da behatzailea baina Subject-a aldatzen du, eta aldaketa horiek beste behatzaileek automatikoki ikusten dituzte

### 6. Java Observable/Observer Erabilera

`Covid19Pacient` extends `Observable`:
- `setChanged()`: Egoera aldatu dela markatu
- `notifyObservers()`: Behatzaile guztiei jakinarazi

GUI klaseak implement `Observer`:
- `update(Observable o, Object arg)`: Behatzaileak automatikoki deitzen duen metodoa Subject-ak aldatzen denean
