package adapter2;

import javax.swing.table.AbstractTableModel;

import domain.Covid19Pacient;

public class Covid19PacientTableModelAdapter extends AbstractTableModel {
	  protected Covid19Pacient pacient;
	  protected String[] columnNames =
	    new String[] {"Symptom", "Weight" };

	  public Covid19PacientTableModelAdapter(Covid19Pacient p) {
	    this.pacient=p;
	  }

	  public int getColumnCount() {
	    return columnNames.length;
	  }

	  public String getColumnName(int i) {
	    return columnNames[i];
	  }

	  public int getRowCount() {
	    return pacient.getSymptoms().size();
	  }

	  public Object getValueAt(int row, int col) {
	    domain.Symptom[] symptoms = pacient.getSymptoms().toArray(new domain.Symptom[0]);
	    domain.Symptom symptom = symptoms[row];
	    
	    if (col == 0) {
	      return symptom.getName();
	    } else {
	      return pacient.getWeight(symptom);
	    }
	  }
	}
