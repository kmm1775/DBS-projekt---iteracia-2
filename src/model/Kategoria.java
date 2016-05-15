package model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Kategoria {
	private final IntegerProperty id;
	private final StringProperty nazov;
	
    // defaultny konstruktor
    public Kategoria() {
        this(0, "null");
    }
    
    // hlavny konstruktor
    public Kategoria(int id, String nazov) {
    	this.id = new SimpleIntegerProperty(id);
    	this.nazov = new SimpleStringProperty(nazov);
    }
    
    /* Nazov */
    public String getNazov()
    {
    	return nazov.get();
    }
    
    public void setNazov(String nazov)
    {
    	this.nazov.set(nazov);
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
