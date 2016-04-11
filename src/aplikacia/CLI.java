package aplikacia;

import java.sql.SQLException;
import java.util.*;

public class CLI {
	void spusti(Prihlasenie akt_prihlas) throws SQLException
	{
		boolean loop = true;
		String prikaz = "null";
		Spracovanie.akt_prihlas = akt_prihlas;
	
		while(loop)
		{
			System.out.print(">  ");
			Scanner skener = new Scanner(System.in);
			prikaz = skener.nextLine();
			
			loop = Spracovanie.spracujPrikaz(prikaz);
		}
	}
}
