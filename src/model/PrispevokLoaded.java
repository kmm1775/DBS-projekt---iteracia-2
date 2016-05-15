package model;

import java.time.LocalDate;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PrispevokLoaded extends Prispevok {	
	int pid;
<<<<<<< HEAD
=======
	int cislo_reakcie=0;
>>>>>>> refs/remotes/origin/master
	
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
    
<<<<<<< HEAD
=======
    public void setReakcia(int reakcia)
    {
    	cislo_reakcie = reakcia;
    }
    
    public int getReakcia()
    {
    	return cislo_reakcie;
    }
    
>>>>>>> refs/remotes/origin/master
    public int getPID()
    {
    	return pid;
    }
<<<<<<< HEAD
=======
    
    public void setPID(int pid)
    {
    	this.pid = pid;
    }
>>>>>>> refs/remotes/origin/master
}
