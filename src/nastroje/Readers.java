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
}
