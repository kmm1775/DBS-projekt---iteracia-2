package model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Prispevok {
	private final StringProperty subjekt;
	private final StringProperty obsah;
	private final StringProperty autor;
	
    // defaultny konstruktor
    public Prispevok() {
        this(null, null, null);
    }
    
    // hlavny konstruktor
    public Prispevok(String autor, String subjekt, String obsah) {
        this.autor = new SimpleStringProperty(autor);
        this.subjekt = new SimpleStringProperty(subjekt);
        this.obsah =  new SimpleStringProperty(obsah);
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
    public String getSubjekt()
    {
    	return subjekt.get();
    }
    
    public void setSubjekt(String subjekt)
    {
    	this.subjekt.set(subjekt);
    }
    
    public StringProperty SubjektProperty() {
        return subjekt;
    }
    
    /* Obsah */
    public String getObsah()
    {
    	return obsah.get();
    }
    
    public void setObsah(String obsah)
    {
    	this.obsah.set(obsah);
    }
    
    public StringProperty ObsahProperty() {
        return obsah;
    }
    
    
}
