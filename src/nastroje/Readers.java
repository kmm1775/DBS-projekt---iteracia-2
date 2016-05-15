package nastroje;

import java.util.Scanner;

public class Readers {
	/* Nacitanie do stringu */
	public static String nahrajVstupString(String vypis)
	{
		String vystup;
		System.out.print(vypis);
		Scanner skener = new Scanner(System.in);
		vystup = skener.nextLine();
		
		return vystup;
	}
	
	/* Nacitanie do intu */
	public static int nahrajVstupInt(String vypis)
	{
		int vystup;
		System.out.print(vypis);
		Scanner skener = new Scanner(System.in);
		vystup = skener.nextInt();
		
		return vystup;
	}
<<<<<<< HEAD
=======
	
	/* Nacitanie prikazu */
	public static String nacitajPrikaz()
	{
		System.out.print(">  ");
		Scanner skener = new Scanner(System.in);
		return skener.nextLine();
	}
	
	/* Prehladne vypisanie obsahu */
	public static void vypisObsahuHitu(String obsah_vyp)
	{
		int dlzka_obsahu = obsah_vyp.length();
		int dlzka_na_vypis=dlzka_obsahu;
		if (dlzka_obsahu > 10)
		{
			dlzka_na_vypis = 10;
		}
	
		String na_vypis= obsah_vyp.substring(0, dlzka_na_vypis);
	
		if (dlzka_na_vypis == 10)
		{
			System.out.println(na_vypis + " ...");
		}
		else
		{
			System.out.println(na_vypis);
		}
	}
>>>>>>> refs/remotes/origin/master
}
