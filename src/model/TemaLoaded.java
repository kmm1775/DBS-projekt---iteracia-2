package model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TemaLoaded {
	private final StringProperty nazov;
	private final IntegerProperty id;
	int tid;
	
    // defaultny konstruktor
    public TemaLoaded() {
        this(null, 0, 0);
    }
    
    // hlavny konstruktor
    public TemaLoaded(String nazov, Integer id, int tid) {
        this.nazov =  new SimpleStringProperty(nazov);
        this.id = new SimpleIntegerProperty(id);
        this.tid = tid;
    }
    
    
  public int getTid()
  {
	  return tid;
  }
    
}
