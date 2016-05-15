package aplikacia;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import model.*;

public class Spustac {
	public static void main(String[] args) throws SQLException, IOException
	{
		Prihlasenie akt_prihlaseny = new Prihlasenie(new User("guest", "ziadny")); // TODO - test len prihlasenie()
	
		/* pre odstranenie warningov */
		String log4j_konfig = "./lib/log4j.properties";
		PropertyConfigurator.configure(log4j_konfig);
		
		System.out.println("***************************");
		System.out.println("PROJEKT - ZADANIE 1 - FORUM");
		System.out.println("***************************");
		
		CLI cli = new CLI();
		cli.spusti(akt_prihlaseny);
	}
}
