package model;

import java.time.LocalDate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Forum {
	private final StringProperty popis;
	private final StringProperty nazov;
	private final StringProperty autor;
	private final IntegerProperty id;
	
    // defaultny konstruktor
    public Forum() {
        this(null, null, null, 0);
    }
    
    // hlavny konstruktor
    public Forum(String autor, String popis, String nazov, Integer id) {
        this.autor = new SimpleStringProperty(autor);
        this.popis = new SimpleStringProperty(popis);
        this.nazov =  new SimpleStringProperty(nazov);
        this.id = new SimpleIntegerProperty(id);
    }
    
    
    /* Autor */
    public String getAutor()
    {
    	return autor.get();
    }
    
    public void setAutor(String autor)
    {
    	this.autor.set(autor);
    }
    
    public StringProperty AutorProperty() {
        return autor;
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
    
    /* Popis */
    public String getPopis()
    {
    	return popis.get();
    }
    
    public void setPopis(String obsah)
    {
    	this.popis.set(obsah);
    }
    
    public StringProperty PopisProperty() {
        return popis;
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
