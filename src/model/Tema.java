package model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tema {
	private final StringProperty nazov;
	private final IntegerProperty id;
	
    // defaultny konstruktor
    public Tema() {
        this(null, 0);
    }
    
    // hlavny konstruktor
    public Tema(String nazov, Integer id) {
        this.nazov =  new SimpleStringProperty(nazov);
        this.id = new SimpleIntegerProperty(id);
    }
    
    
    /* Subjekt */
    public String getNazov()
    {
    	return nazov.get();
    }
    
    public void setNazov(String subjekt)
    {
    	this.nazov.set(subjekt);
    }
    
    public StringProperty NazovProperty() {
        return nazov;
    }
    
    /* ID */
    public Integer getID()
    {
    	return id.get();
    }
    
    public void setID(Integer id)
    {
    	this.id.set(id);
    }
    
    public IntegerProperty IDProperty() {
        return id;
    }    
    
}
