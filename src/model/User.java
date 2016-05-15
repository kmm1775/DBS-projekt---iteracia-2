package model;

import java.time.LocalDate;

// beany na gettery a settery
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty nick;
    private final ObjectProperty<LocalDate> datum_registracie;
    private final StringProperty email;
    private final StringProperty heslo;
    
    boolean je_admin = false;
    
    // defaultny konstruktor
    public User() {
        this(null);
    }
    
    /* nastavi len nick */
    public User(String nick) {
    	// nastavi nick
        this.nick = new SimpleStringProperty(nick);
        // vytvori akt. datum
        this.datum_registracie = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        this.email = new SimpleStringProperty("null");
        this.heslo = new SimpleStringProperty("null");
    }
    
    /* nastavi nick + email */
    public User(String nick, String email) {
    	// nastavi nick
        this.nick = new SimpleStringProperty(nick);
        // vytvori akt. datum
        this.datum_registracie = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        this.email = new SimpleStringProperty(email);
        this.heslo = new SimpleStringProperty("null");
    }
    
    public User(String nick, String email, boolean je_admin) {
    	// nastavi nick
        this.nick = new SimpleStringProperty(nick);
        // vytvori akt. datum
        this.datum_registracie = new SimpleObjectProperty<LocalDate>(LocalDate.now());
        this.email = new SimpleStringProperty(email);
        this.heslo = new SimpleStringProperty("null");
        this.je_admin = je_admin;
    }
    
    /* NIcky */
    public String getNick()
    {
    	return nick.get();
    }
    
    public void setNick(String nick)
    {
    	this.nick.set(nick);
    }
    
    public StringProperty NickProperty() {
        return nick;
    }
    
    /* Email */
    public String getEmail()
    {
    	return email.get();
    }
    
    public void setEmail(String email)
    {
    	this.email.set(email);
    }
    
    public StringProperty EmailProperty() {
        return email;
    }    
    
/* Datum */ 
    public LocalDate getDatumReg()
    {
    	return datum_registracie.get();
    }

    public ObjectProperty<LocalDate> DatumRegProperty() {
    	return datum_registracie;
    }

/* Heslo */
	public String getHeslo() {
		// TODO Auto-generated method stub
		return heslo.get();
	}
   
	/* je admin */
	public boolean getJeAdmin() {
		// TODO Auto-generated method stub
		return je_admin;
	}
}
