package model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrispevokLoaded extends Prispevok {	
	int pid;
	int cislo_reakcie=0;
	
    // defaultny konstruktor
    public PrispevokLoaded() {
        this(null, null, null, 0);
    }
    
    // hlavny konstruktor
    public PrispevokLoaded(String autor, String subjekt, String obsah, int pid) {
        setAutor(autor);
        setSubjekt(subjekt);
        setObsah(obsah);
        
        this.pid = pid;
    }
    
    public void setReakcia(int reakcia)
    {
    	cislo_reakcie = reakcia;
    }
    
    public int getReakcia()
    {
    	return cislo_reakcie;
    }
    
    public int getPID()
    {
    	return pid;
    }
    
    public void setPID(int pid)
    {
    	this.pid = pid;
    }
}
