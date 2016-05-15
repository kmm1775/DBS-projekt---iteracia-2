package aplikacia;

<<<<<<< HEAD
import java.sql.SQLException;
=======
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.*;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
>>>>>>> refs/remotes/origin/master

import model.*;

public class Spustac {
<<<<<<< HEAD
	public static void main(String[] args) throws SQLException
	{
		Prihlasenie akt_prihlaseny = new Prihlasenie(new User("guest")); // TODO - test len prihlasenie()
		
		System.out.println("PROJEKT - ZADANIR 1 - FORUM");
=======
	public static void main(String[] args) throws SQLException, IOException
	{
		Prihlasenie akt_prihlaseny = new Prihlasenie(new User("guest", "ziadny")); // TODO - test len prihlasenie()
	
		/* pre odstranenie warningov */
		String log4j_konfig = "./lib/log4j.properties";
		PropertyConfigurator.configure(log4j_konfig);
		
		System.out.println("***************************");
		System.out.println("PROJEKT - ZADANIE 1 - FORUM");
		System.out.println("***************************");
		
>>>>>>> refs/remotes/origin/master
		CLI cli = new CLI();
		cli.spusti(akt_prihlaseny);
	}
}
