package aplikacia;

import java.sql.SQLException;

import model.*;

public class Spustac {
	public static void main(String[] args) throws SQLException
	{
		Prihlasenie akt_prihlaseny = new Prihlasenie(new User("guest")); // TODO - test len prihlasenie()
		
		System.out.println("PROJEKT - ZADANIR 1 - FORUM");
		CLI cli = new CLI();
		cli.spusti(akt_prihlaseny);
	}
}
