# Adapter2 Ariketaren Analisia

## 1. Covid19PacientTableModelAdapter klasean behar den kodea

### Hasierako egoera (src-old karpetan)

`src-old/adapter2/Covid19PacientTableModelAdapter.java` fitxategian, adaptadorea ez dago inplementatuta eta metodo guztiek balio estatikoak itzultzen dituzte:

```java
public int getColumnCount() {
    return 1;  // Oker dago
}

public String getColumnName(int i) {
    return "Column name 1";  // Oker dago
}

public int getRowCount() {
    return 1;  // Oker dago
}

public Object getValueAt(int row, int col) {
    return "value";  // Oker dago
}
```

### Inplementazio zuzena (src karpetan)

`src/adapter2/Covid19PacientTableModelAdapter.java` fitxategian, adaptadoreak behar bezala funtzionatzen du:

```java
public int getColumnCount() {
    return columnNames.length;  // 2 zutabe: "Symptom" eta "Weight"
}

public String getColumnName(int i) {
    return columnNames[i];  // Zutabe-izenak array-tik hartzen ditu
}

public int getRowCount() {
    return pacient.getSymptoms().size();  // Pazientearen sintoma kopurua
}

public Object getValueAt(int row, int col) {
    domain.Symptom[] symptoms = pacient.getSymptoms().toArray(new domain.Symptom[0]);
    domain.Symptom symptom = symptoms[row];
    
    if (col == 0) {
        return symptom.getName();  // Lehenengo zutabea: sintomaren izena
    } else {
        return pacient.getWeight(symptom);  // Bigarren zutabea: sintomaren pisua
    }
}
```

### Kodea azalpena

**Adapter Pattern-aren aplikazioa:**

- `AbstractTableModel` Swing-eko interfazea da taulak datuak bistaratzeko
- `Covid19Pacient` klasea ez da zuzenean `TableModel`-rekin bateragarria
- `Covid19PacientTableModelAdapter` adaptadore gisa jokatzen du bien artean

**Metodo bakoitzaren funtzioa:**

1. **`getColumnCount()`**: Taula zenbat zutabe dituen itzultzen du (2: Symptom eta Weight)

2. **`getColumnName(int i)`**: Zutabe bakoitzaren goiburukoa itzultzen du

3. **`getRowCount()`**: Errenkada kopurua itzultzen du (pazientearen sintoma kopurua)

4. **`getValueAt(int row, int col)`**: Errenkada eta zutabe jakin bateko balioa lortzen du:
   - Sintoma guztiak array batera pasatzen ditu
   - Errenkadako sintoma lortzen du
   - Zutabearen arabera itzultzen du sintomaren izena (0) edo pisua (1)

---

## 2. Bi paziente eta bi taula

### Main.java inplementazioa

`src/adapter2/Main.java` fitxategian bi paziente sortu eta bi taula-leiho irekitzen dira:

```java
// Lehenengo pazienta
Covid19Pacient pacient = new Covid19Pacient("aitor", 35);
pacient.addSymptomByName("disnea", 2);
pacient.addSymptomByName("cefalea", 1);
pacient.addSymptomByName("astenia", 3);

ShowPacientTableGUI gui = new ShowPacientTableGUI(pacient);
gui.setPreferredSize(new java.awt.Dimension(300, 200));
gui.setVisible(true);

// Bigarren pazienta
Covid19Pacient pacient2 = new Covid19Pacient("maria", 28);
pacient2.addSymptomByName("fiebre", 3);
pacient2.addSymptomByName("tos", 2);
pacient2.addSymptomByName("disnea", 1);
pacient2.addSymptomByName("cefalea", 2);

ShowPacientTableGUI gui2 = new ShowPacientTableGUI(pacient2);
gui2.setPreferredSize(new java.awt.Dimension(300, 200));
gui2.setLocation(350, 0);  // Bigarren leihoa lehen leihoarekiko desplazatuta
gui2.setVisible(true);
```

### Emaitza

Aplikazioa exekutatu ondoren:
- **Lehenengo taula**: "Covid Symptoms aitor" izenarekin, 3 sintoma (disnea, cefalea, astenia)
- **Bigarren taula**: "Covid Symptoms maria" izenarekin, 4 sintoma (fiebre, tos, disnea, cefalea)
- Bi taulak independenteki bistaratzen dira, bakoitza bere pazientearen datuak erakutsiz

---

## 3. Observer Pattern-a taula-leihoarekin integratzea (HAUTAZKOA)

### Problema

Observer ariketan, `PacientSymptomGUI` interfazea erabiliz paziente bati sintomak gehitu eta kentzen zaizkio. `Covid19Pacient` klasea `Observable` klasetik heredatzen da eta `addSymptom` eta `removeSymptom` metodoek `notifyObservers()` deitzen dute.

Galdera: Nola gehitu `ShowPacientTableGUI` taula-leihoa pazientea behatzaile moduan, sintoma aldaketa bakoitzean eguneratzeko?

### Irtenbidea

#### 1. ShowPacientTableGUI Observer bihurtu

`ShowPacientTableGUI` klaseak `Observer` interfazea inplementatu behar du:

```java
import java.util.Observer;
import java.util.Observable;

public class ShowPacientTableGUI extends JFrame implements Observer {
    
    private JTable table;
    private Covid19Pacient pacient;
    private Covid19PacientTableModelAdapter tableModel;
    
    public ShowPacientTableGUI(Covid19Pacient pacient) {
        this.setTitle("Covid Symptoms " + pacient.getName());
        this.pacient = pacient;
        
        // Observer bezala erregistratu
        pacient.addObserver(this);
        
        setFonts();
        
        // TableModel-a gorde erreferentzia batekin
        tableModel = new Covid19PacientTableModelAdapter(pacient);
        table = new JTable(tableModel);
        table.setRowHeight(36);
        
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new java.awt.Dimension(300, 200));
        this.getContentPane().add(pane);
    }
    
    @Override
    public void update(Observable o, Object arg) {
        // Pazientearen datuak aldatu direnean taula eguneratu
        tableModel.fireTableDataChanged();
    }
    
    // Leihoa itxitakoan observer-etik kendu
    @Override
    public void dispose() {
        pacient.deleteObserver(this);
        super.dispose();
    }
}
```

#### 2. Funtzionamendu-mekanismoa

1. **Erregistroa**: `ShowPacientTableGUI` sortzerakoan, `pacient.addObserver(this)` deitzen da pazientea behatzaile gisa erregistratzeko

2. **Jakinarazpena**: Pazienteari sintoma bat gehitzen edo kentzen zaionean:
   - `Covid19Pacient.addSymptomByName()` edo `removeSymptomByName()` metodoak `setChanged()` eta `notifyObservers()` deitzen dituzte
   - Observer guztiek (PacientObserverGUI, PacientThermometerGUI, SemaphorGUI, eta orain ShowPacientTableGUI) `update()` metodoa jasotzen dute

3. **Eguneratzea**: `update()` metodoan, `tableModel.fireTableDataChanged()` deitzen da:
   - Swing-eko `AbstractTableModel`-en metodo hau taulari esaten dio datuak aldatu direla
   - Taulak automatikoki bistaratu berria egiten du, sintoma berriak edo aldaketak erakutsiz

#### 3. Abantailak

- **Deskonexioa**: Taulak ez du ezagutu behar sintomak noiz gehitzen edo kentzen diren
- **Automatismoa**: Edozein aldaketa automatikoki islatzen da taula guztietan
- **Estentsibilitatea**: Paziente beraren taula anitzak izan daitezke, denak sinkronizatuta
- **Observer Pattern estandarra**: Java-ren `Observable`/`Observer` interfazea erabiltzen du

#### 4. Erabilera-adibidea

```java
// Pazienta sortu
Covid19Pacient pacient = new Covid19Pacient("aitor", 35);
pacient.addSymptomByName("disnea", 2);

// Bi interfaze sortu: sintomak gehitzeko eta taula ikusteko
PacientSymptomGUI symptomGUI = new PacientSymptomGUI(pacient);
ShowPacientTableGUI tableGUI = new ShowPacientTableGUI(pacient);

// Erabiltzaileak PacientSymptomGUI-tik sintoma bat gehitzen duenean,
// ShowPacientTableGUI automatikoki eguneratuko da sintoma berria erakutsiz
```

### Laburpena

Observer Pattern-a erabiliz, `ShowPacientTableGUI` taula-leihoak pazientearen aldaketak automatikoki jaso eta bistaratzeko gai da, kodea deskonektatuta eta mantengarria mantenduz. Sintomak gehitzeko interfazea (`PacientSymptomGUI`) eta taula bistaratzeko interfazea (`ShowPacientTableGUI`) independenteak dira, baina biak sinkronizatuta daude Observer Pattern-ari esker.

