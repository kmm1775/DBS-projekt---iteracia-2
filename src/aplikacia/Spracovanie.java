package aplikacia;

import java.sql.*;
import java.util.*;

import databaza.*;
import model.*;
import nastroje.*;

public class Spracovanie {
	static Prihlasenie akt_prihlas;
	
	Spracovanie(Prihlasenie akt_prihlas)
	{
		this.akt_prihlas = akt_prihlas;
	}
	
	public static boolean spracujPrikaz(String prikaz) throws SQLException
	{
		boolean priznak = true;
		
		/* UKONCENIE CELEHO PROGRAMU */
		if (prikaz.equals("koniec") || prikaz.equals("quit") || prikaz.equals("exit"))
		{
			priznak = false;
			System.out.println("PROGRAM KONCI...");
		}
		/* PRIHLASENIE */
		else if (prikaz.equals("prihlasenie"))
		{
			/* standardne je prihlaseny guest */
			/* pokial som prihlaseny ako guest */
			if (!akt_prihlas.konto.getNick().equals("guest"))
			{
				/* najskor sa musime odhlasit */
				akt_prihlas = new Prihlasenie(new User("guest"));
				
			}
			/* teraz si nacitame potr. udaje */
			String nick, heslo;
			nick = Readers.nahrajVstupString("Vlozte nick: ");
			heslo = Readers.nahrajVstupString("Vlozte heslo (neoveruje sa): ");
			
			/* teraz sa prihlasime */
			User zisteny_user = KontaManager.existujeKonto(nick, heslo);
			
			if(zisteny_user == null)
			{
				System.out.println("Uzivatel sa v databaze nenachadza!");
			}
			else
			{
				akt_prihlas = new Prihlasenie(zisteny_user);
				System.out.println("Boli ste prihlaseny ako " + akt_prihlas.konto.getNick());
			}
			
		}
		/* ODHLASENIE */
		else if (prikaz.equals("odhlasenie"))
		{
			/* zistime ci sme prihlaseny ako non-guest */
			if (akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Nie ste prihlaseny! Nemozte sa odhlasit!");
			}
			/* tu prebieha samotne odhlasenie */
			else
			{
				akt_prihlas = new Prihlasenie(new User("guest"));
				System.out.println("Boli ste odhlaseny!");
			}
		}
		/* FILTROVANIE KONT PODLA ZADANYCH PARAMETROV */
		/* ODHLASENIE */
		else if (prikaz.equals("filtrovanie kont"))
		{
			System.out.println("Filtrovanie kont podla zadanych parametrov");
			/* zadanie ci je admin a mod */
			String admin_vstup, mod_vstup;
			Boolean je_admin = true;
			Boolean je_mod = false;
			
			//Boolean je_admin = spracujTrueFalse(admin_vstup = Readers.nahrajVstupString("Chcete hladat adminov? (ano/nie)"));
			//Boolean je_mod = spracujTrueFalse(mod_vstup = Readers.nahrajVstupString("Chcete hladat moderatorov? (ano/nie)"));
			
			/* zadanie rozsahu datumu */
			String datum_zac = Readers.nahrajVstupString("Dat. OD kt. vyhladavat; Format (RRRR-MM-DD)");
			String datum_kon = Readers.nahrajVstupString("Dat. DO kt. vyhladavat; Format (RRRR-MM-DD)");
			/* zadanie obs. mena */
			String cast_mena = Readers.nahrajVstupString("Zadajte meno al. jeho cast: ");
			/* zadanie min. poctu prispevkov */
			int min_pp = Readers.nahrajVstupInt("Zadajte min. pocet prispevkov na autora: ");
			
			List<VyslFiltra> vysledky = KontaManager.filtrujKonta(cast_mena, datum_zac, datum_kon, je_admin, je_mod, min_pp);
			
			System.out.println("\n Vyhladane zaznamy \n");
			
			if(vysledky.isEmpty())
			{
				System.out.println("Ziadne zaznamy nevyhovuju zadanim kriteriam");
			}
			else
			{
				for (VyslFiltra vf : vysledky)
				{
					System.out.println("Uzivatel: " + vf.getNick() + " s poctom prisp.: " + vf.getPP());
				}
			}
			
		}
		/* INFO O AKT. PRIHLAS. UZIVATELOVI */
		else if (prikaz.equals("ktosom"))
		{
			System.out.println("Akt. prihlaseny uzivatel je: " + akt_prihlas.konto.getNick());
			System.out.println("E-Mail: " + akt_prihlas.konto.getEmail());
		}
		/* REGISTRACIA */
		else if (prikaz.equals("registracia"))
		{
			/* prave nie som guest, asi by som nemal chciet novy ucet */
			if (!akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Prave ste prihlaseny ako " + akt_prihlas.konto.getNick());
				System.out.println("Naozaj chcete novy ucet?");
				// TODO: sem by sa mozno hodilo yes/no
			}
			
			/* samotna registracia */
			/* zadanie udajov */
			
			// TODO: overenie by mohlo ist cez cyklus
			System.out.println("Vlozte MENO noveho pouzivatela: ");
			String novyuser_meno;
			Scanner meno_sken = new Scanner(System.in);
			novyuser_meno = meno_sken.nextLine();
	
			System.out.println("Vlozte HESLO noveho pouzivatela: ");
			String novyuser_heslo;
			Scanner heslo_sken = new Scanner(System.in);
			novyuser_heslo = heslo_sken.nextLine();
			
			System.out.println("Vlozte EMAIL noveho pouzivatela: ");
			String novyuser_email;
			Scanner email_sken = new Scanner(System.in);
			novyuser_email = email_sken.nextLine();
			
			
			/* overenie udajov */
			/* overenie ci sa tam uz nenachadza taky user */
			User zisteny_user = KontaManager.existujeKonto(novyuser_meno, "null");
			/* vlozenie do databazy */
			if (zisteny_user == null)
			{
				KontaManager.vytvorKonto(novyuser_meno, novyuser_heslo, novyuser_email);
			}
			else
			{
				System.out.println("Pouzivatelske meno sa uz v databaze nachadza.");
			}
		}
		else if (prikaz.equals("upravenie konta"))
		{
			if(akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Nie ste prihlaseny!");
			}
			else
			{
				/* dochadza k samotnej uprave */
				/* volba co chcem zmenit */
				System.out.println("Co chcete zmenit? E-mail (e), Heslo (h)");
				
				String zmena;
				Scanner aka_zmena = new Scanner(System.in);
				zmena = aka_zmena.nextLine();
				
				if (zmena.equals("h"))
				{
					String nove_heslo;
					System.out.println("Vlozte nove heslo: ");
					Scanner heslo_skener = new Scanner(System.in);
					nove_heslo = heslo_skener.nextLine();
					/* overenie spravnosti hesla */
					/* dojde k update hesla */
					KontaManager.upravKonto(akt_prihlas.konto.getNick(), nove_heslo, akt_prihlas.konto.getEmail());
				}
				else if (zmena.equals("e"))
				{
					String novy_email;
					System.out.println("Vlozte novy email: ");
					Scanner email_skener = new Scanner(System.in);
					novy_email = email_skener.nextLine();
					/* overenie spravnosti hesla */
					/* dojde k update hesla */
					KontaManager.upravKonto(akt_prihlas.konto.getNick(), akt_prihlas.konto.getHeslo(), novy_email);
				}
				else
				{
					System.out.println("Bola zadana nespravna volba!");
				}
				
			}
		}
		else if (prikaz.equals("vymazanie konta") || prikaz.equals("zmazanie konta"))
		{
			if (akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Nie ste prihlaseny");
			}
			/* mozeme mazat */
			else
			{
				/* najprv zmazeme akt. konto */
				KontaManager.zmazKonto(akt_prihlas.konto.getNick());
				/* nastavime akt. usera na 'guest' */
				akt_prihlas = new Prihlasenie(new User("guest"));
				System.out.println("Vase konto bolo zmazane");
			}
		}
		/* ZOBRAZENIE KONT UZIVATELOV FORA */
		else if (prikaz.equals("zobrazenie kont"))
		{
			System.out.println("Zadajte podmienky zobrazenia: ");
			
			// tu zadame podmienky
			System.out.println("Zakladne statistiky: ");
			List<UserLoaded> infoKonta = KontaManager.ziskajInfoKonta();
			int counter=1;
			
			System.out.print("Celkovy pocet reg. uzivatelov je: ");
			System.out.println(VseobManager.zistiPocty("konta"));
			
			System.out.println("id - autor - email - # prispevkov");
			for (UserLoaded ul : infoKonta)
			{
				// TODO toto formatovanie treba vyladit
				String riadok_konto = String.format("%1$2d %2$-5s %3$-5s %4$-15s", counter, ul.getNick(), ul.getEmail(), ul.ziskajPocetPrisp());
				System.out.println(riadok_konto);
				
				counter++;
			}
		}
		
		/*
		 * 
		 * PRIKAZY PRE PRISPEVKY
		 * 
		 */
		
		/* CHCEME ZOBRAZIT CELE FORUM AJ S PRISPEVKAMI */
		else if (prikaz.equals("zobrazenie fora"))
		{
			List<Forum> vs_fora = PrispevkyManager.ziskajVsFora();
			
			/* zistujeme temy */
			for (Forum f : vs_fora)
			{
				// TODO ak je nieco selected, zislo by sa to dat do zatvoriek
				// not selected -> [ selected ]
				System.out.println(" * " + f.getNazov() + " {" + f.getID() + "}");
				
				List<Tema> vs_temy = PrispevkyManager.ziskajvsTemybyFID(f.getID());
				for (Tema t: vs_temy)
				{
					System.out.println("   * " + t.getNazov());
					
					List<PrispevokLoaded> vs_prispevky = PrispevkyManager.ziskajvsPrispevkybyTID(t.getID());
					for(PrispevokLoaded p : vs_prispevky)
					{
						System.out.println("     * " + p.getSubjekt() + " " + "(" + p.getPID() + ")");
					}
				}
			}
		}
		else if (prikaz.equals("pridanie fora"))
		{
			if (akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Nie ste prihlaseny. Nemozete vytvarat nove forum");
			}
			else
			{
				// skontrolovat ci je admin
				User zisteny_user = KontaManager.existujeKonto(akt_prihlas.konto.getNick(), "null");
				// ak je tak vytvorit
				if (zisteny_user.getJeAdmin() == true)
				{
					// zadanie parametrov
					String forum_nazov, forum_popis;
					
					System.out.print("Vlozte nazov fora: ");
					Scanner nazov_skener = new Scanner(System.in);
					forum_nazov = nazov_skener.nextLine();
					
					System.out.print("Vlozte popis fora: ");
					Scanner popis_skener = new Scanner(System.in);
					forum_popis = popis_skener.nextLine();
					
					// vlozenie fora do databazy
					PrispevkyManager.vytvorForum(forum_nazov, forum_popis, akt_prihlas.konto.getNick());
					
					
				}
				// ak nie je tak koniec
				else
				{
					System.out.println("Nemate administratorske prava!");
				}
				
			}
		}
		/* ZMAZANIE FORA */
		else if (prikaz.equals("zmazanie fora"))
		{
			int forum_id;
			System.out.print("Zvolte id (id) fora na zmazanie");
			Scanner forum_id_skener = new Scanner(System.in);
			forum_id = forum_id_skener.nextInt();
			
			System.out.println("Zadane id bolo: " + forum_id);
			

			// zistime ci mame admin prava
			if (akt_prihlas.konto.getJeAdmin() == false)
			{
				System.out.println("Nie ste administrator!");
			}
			else
			{
				// zistime ci je id spravne
				Forum vratene_forum = PrispevkyManager.existujeFID(forum_id);
				if (vratene_forum != null)
				{
					// zmazeme forum (vs. prispevky a temy s nim)
					PrispevkyManager.zmazZaznam("fora", forum_id);
					System.out.println("Zaznam bol zmazany.");
				}
				else
				{
					System.out.println("Zadali ste zle id.");
				}
			
			}
		}
		/* VYPISANIE PRISPEVKOV PODLA ID TEMY */
		else if (prikaz.equals("vypisanie prispevkov"))
		{
			int tema_id;
			System.out.print("Zvolte id (id) TEMY: ");
			Scanner tema_id_skener = new Scanner(System.in);
			tema_id = tema_id_skener.nextInt();
			
			// overime ci tema existuje
			Tema vratena_tema = PrispevkyManager.existujeTID(tema_id);
			
			if(vratena_tema != null)
			{
				// ak ano vypiseme vs. prispevky
				List<PrispevokLoaded> vs_prispevky = PrispevkyManager.ziskajvsPrispevkybyTID(tema_id);
				
				for (PrispevokLoaded p : vs_prispevky)
				{
					System.out.println("Subjekt: " + p.getSubjekt() + " Autor: " + p.getAutor());
				}
			}
		}
		/* PRIDANIE REAKCIE */
		else if(prikaz.equals("pridanie reakcie"))
		{
			/* najprv chceme zadat id prispevku na kt. reagujeme */
			int id_prispevku = Readers.nahrajVstupInt("Zadajte ID prisp. na kt. regujete: ");
			
			/* vyhladame ci sa prispevok nachadza v db */
			int tid = PrispevkyManager.existujeByID("prispevky", id_prispevku);
			
			if(tid > 0)
			{
				/* ID prispevku sa naslo */
				/* pridavame reakciu */
				pridajReakciuNaPrisp(id_prispevku, tid);
				
			}
			else
			{
				/* ID prispevku sme nanasli */
				System.out.println("Zadane ID prispevku neexistuje.");
			}
		}
		/* VYPISANIE REAKCII NA PRISPEVOK */
		else if(prikaz.equals("vypisanie reakcii"))
		{
			// zadame id clanku
			int id_clanku_na_reakcie = Readers.nahrajVstupInt("Vloz ID clanku: ");
			// predpokladame ze existuje
			// zistenie vs. reakcii podla pid clanku
			List<PrispevokLoaded> reakcie = PrispevkyManager.zistiReakcie(id_clanku_na_reakcie);
			
			if (reakcie.isEmpty())
			{
				System.out.println("Nenasli sa ziadne reakcie.");
			}
			else
			{
				for (PrispevokLoaded pl : reakcie)
				{
					System.out.println("Prispevok c. " + pl.getPID() + " | Autor: " + pl.getAutor());
					System.out.println("Predmet: " + pl.getSubjekt());
					System.out.println("Obsah: " + pl.getObsah());
				}
			}
					
		}
		else if(prikaz.equals("vypisanie prispevku"))
		{
			// zadame id clanku
			int id_clanku_na_reakcie = Readers.nahrajVstupInt("Vloz ID prisp.: ");
			Prispevok prispevok = PrispevkyManager.vypisPrispevok(id_clanku_na_reakcie);
			
			// zobraz prispevok
			System.out.println("Prispevok c. " + id_clanku_na_reakcie + " | Autor: " + prispevok.getAutor());
			System.out.println("Predmet: " + prispevok.getSubjekt());
			System.out.println("Obsah: " + prispevok.getObsah());
			
		}
		else
		{
			System.out.println("Neplatny prikaz. Opakujte volbu...");
		}
		
		return priznak;
	}
	
	/*
	 * 
	 * HLAVNY BLOK HLAVNYCH PRIKAZOV
	 * 
	 */
	static void pridajReakciuNaPrisp(int id_prispevku, int tid) throws SQLException
	{
		String subjekt = Readers.nahrajVstupString("Vlozte predmet reakcie: ");
		String obsah = Readers.nahrajVstupString("Vlozte text reakcie: ");
		String autor = akt_prihlas.konto.getNick();
		
		/* musime ziskat ID autora */
		int autor_id = VseobManager.existujeByID("konta", "id", "nick", autor);
		
		/* vlozime novy prispevok podla autora */
		//PrispevkyManager.pridajPrispevok(subjekt, autor_id, obsah, tid);
		/* vlozime reakcie do samostatnej tabulky */
		
	}
	
	static Boolean spracujTrueFalse(String retazec)
	{
		if (retazec.equals("ano"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
}
