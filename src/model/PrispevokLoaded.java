package model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrispevokLoaded extends Prispevok {	
	int pid;
	
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
    
    public int getPID()
    {
    	return pid;
    }
}
