package aplikacia;

/* java imports */
import java.sql.*;
import java.util.*;
import java.io.*;
import java.net.*;

/* elasticsearch & related imports */
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders; // Query builder
import org.elasticsearch.search.SearchHit;
import static org.elasticsearch.common.xcontent.XContentFactory.*;	// JSON builder

/* forum project model imports */
import databaza.*;
import model.*;
import nastroje.*;

public class Spracovanie {
	static Prihlasenie akt_prihlas;
	public static Client elastic_client;
	
	Spracovanie(Prihlasenie akt_prihlas)
	{
		this.akt_prihlas = akt_prihlas;
	}
	
	Spracovanie(Prihlasenie akt_prihlas, Client elastic_client)
	{
		//super();
		this.akt_prihlas = akt_prihlas;
		this.elastic_client = elastic_client;
	}
	
	public static boolean spracujPrikaz(String prikaz) throws SQLException, IOException
	{
		boolean priznak = true;
		
		/*
		 * VYHLADAVANIE (FULL-TEXT) V PRISPEVKOCH
		 * POMOCOU FULL-TEXT
		 */
		if (prikaz.equals("vyhladavanie") || prikaz.equals("fulltext") || prikaz.equals("vh"))
		{
			System.out.println("Full textove vyhladavanie v prispevkoch (reakciach)");
			//String hladane_slova = Readers.nahrajVstupString("Zadajte hlad.slovo: ");
			//String meno_autora = Readers.nahrajVstupString("Zadajte meno autora: ");
			//int pocet_prisp = Readers.nahrajVstupInt("Pocet prispevkov: ");
			
			/* sem zadame parametre hladania */
			boolean autor_y=false, subjekt_y=false, obsah_y=false;
			String zadane_mena_autorov = "";
			String hladany_subjekt = "";
			String hladany_obsah = "";
			String vstup_ft = Readers.nahrajVstupString("Vlozte podla coho hladat"
					+ "( autora [a], subjektu [s], obsahu [o] ): ");
			
			/* tu to rozsekame */
			if (vstup_ft.contains("a")) 
			{ 
				autor_y = true;
				/* hladat podla mien */
				zadane_mena_autorov = Readers.nahrajVstupString("Zadajte mena autorov: ");
				
				/* tu to potom chceme nasekat do query */
			}
			
			if (vstup_ft.contains("s")) 
			{ 
				subjekt_y = true;
				
				/* hladat podla subjektu */
				hladany_subjekt = Readers.nahrajVstupString("Zadajte cast al. cely nazov subjektu: ");
				
				/* tu spracujeme subjekt */
				
			}
			if (vstup_ft.contains("o")) 
			{ 
				obsah_y = true;
				
				/* hladane slovo al. fraza */
				hladany_obsah = Readers.nahrajVstupString("Zadajte hladany obsah al. frazu [regexp]: ");
				
				/* tu spracujeme zadane slovo al. frazu */
				
			}
			
			/* HLADANIE PODLA DATUMU */
			String vstup_datum = Readers.nahrajVstupString("Chcete hladat aj podla datumu [a/n]: ");
			String date_start = "", date_end="";
			if(vstup_datum.equals("a"))
			{
				date_start = Readers.nahrajVstupString("Zadajte start. datum [yyyy-mm-dd]: ");
				date_end = Readers.nahrajVstupString("Zadajte konec. datum [yyyy-mm-dd]: ");
			}
			
			/* REAKCIE */
			String hladat_reakcie = "";
			hladat_reakcie = Readers.nahrajVstupString("Chcete hladat podla reakcii [a/n]: ");
			int reakcia=0;
			
			if (hladat_reakcie.equals("a"))
			{
				reakcia = Readers.nahrajVstupInt("Reakcie na kt. clanok? [id clanku]");
			}
			else
			{
				System.out.println("!! Budeme hladat len v hlavnych clankoch.");
				reakcia = 0;
			}
			
			/* limit na pocet clankov */
			int limit=100;
			int limit_velkosti=100;
			
			/* Chceme limitovat pocet najdenych zaznamov */
			String limitovat_pocet = Readers.nahrajVstupString("Chcete limit. pocet clankov [a/n]: ");
			if(limitovat_pocet.contains("a"))
			{
				limit = Readers.nahrajVstupInt("Vlozte max. pocet najden. clankov: ");
				limit_velkosti = limit;
			}
			else if(limitovat_pocet.contains("n"))
			{
				System.out.println("-> Limit nebude uplatneny");
			}
			else
			{
				System.out.println("!! Nespravna volba pouzijem standardny limit");
			}
			
			int debug = 1;
		
			/* samotne hladanie */
			ElasticStuff.hladajVelasticu2(zadane_mena_autorov, hladany_subjekt, hladany_obsah,
					date_start, date_end, reakcia, limit_velkosti, 0);
		}
		else if(prikaz.equals("synch"))
		{
			System.out.println("Synchronizacia elasticu a databazy");
			ElasticStuff.massSync2();
			System.out.println("Synchronizacia bola dokoncena");
		}
		/* 
		 * UKONCENIE CELEHO PROGRAMU 
		 */
		else if (prikaz.equals("koniec") || prikaz.equals("quit") || prikaz.equals("exit"))
		{
			priznak = false;	// nastavenie zapor. priznaku pre cyklus
			elastic_client.close();	// zatvorenie klienta
			System.out.println("PROGRAM KONCI...");
		}
		/* 
		 * PRIHLASENIE 
		 */
		else if (prikaz.equals("prihlasenie") || prikaz.equals("prihlas") || prikaz.equals("pr"))
		{
			/* standardne je prihlaseny guest */
			/* pokial som prihlaseny ako guest */
			if (!akt_prihlas.konto.getNick().equals("guest"))
			{
				/* najskor sa musime odhlasit */
				akt_prihlas = new Prihlasenie(new User("guest", "ziadny"));
				
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
		/* 
		 * ODHLASENIE 
		 */
		else if (prikaz.equals("odhlasenie") || prikaz.equals("odhlas"))
		{
			/* zistime ci sme prihlaseny ako non-guest */
			if (akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Nie ste prihlaseny! Nemozte sa odhlasit!");
			}
			/* tu prebieha samotne odhlasenie */
			else
			{
				akt_prihlas = new Prihlasenie(new User("guest", "ziadny"));
				System.out.println("Boli ste odhlaseny!");
			}
		}
		/* 
		 * FILTROVANIE KONT PODLA ZADANYCH PARAMETROV 
		 */
		else if (prikaz.equals("filtrovanie kont") || prikaz.equals("filtruj"))
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
		else if (prikaz.equals("ktosom") || prikaz.equals("info"))
		{
			System.out.println("Akt. prihlaseny uzivatel je: " + akt_prihlas.konto.getNick());
			System.out.println("E-Mail: " + akt_prihlas.konto.getEmail());
		}
		/* REGISTRACIA */
		else if (prikaz.equals("registracia") || prikaz.equals("registruj") || prikaz.equals("reg"))
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
		else if (prikaz.equals("upravenie konta") || prikaz.equals("uprav konto"))
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
		/*
		 * ZMAZANIE PRISPEVKU
		 */
		else if(prikaz.equals("zmazanie prispevku") || prikaz.equals("zp") || prikaz.equals("zmaz prispevok"))
		{
			/* POSTUP - START */
			
			/* zistime ci prispevok existuje */
			/* zistime ci je reakcia al. nie */
			/* ak reakcia -> zmazeme */
			/* ak ne-reakcia -> pozrieme ci ma reakcie al. nie */
				/* ma reakcie - informujeme ze mozeme zmazat iba ak zmazeme vs. reakcie */
				/* nema reakcie - priamo zmazeme */
			
			/* POSTUP - END */
			
			/* Nacitame prispevok ID kt. chceme zmazat */
			int id_prisp_na_zmaz = Readers.nahrajVstupInt("Kt. prisp. chcete zmazat (ID): ");
			
			/* Zistime ci existuje */
			int prisp_existuje = PrispevkyManager.existujeByID("prispevky", "id", id_prisp_na_zmaz);
			
			if (prisp_existuje == 0)
			{
				/* ak neexistuje */
				System.out.println("Zadany prispevok neexistuje.");
			}
			else
			{
				/* ak existuje */
				/* zistime ci je reakcia al. nie */
				boolean je_reakcia = PrispevkyManager.je_reakcia(id_prisp_na_zmaz);
				
				/* ak je reakcia zmazeme */
				if (je_reakcia == true)
				{
					PrispevkyManager.zmazZaznam("prispevky", id_prisp_na_zmaz);
					
					/* zmazeme ho aj s elasticu */
					ElasticStuff.zmazZelasticu("projekt", "prispevky", id_prisp_na_zmaz);
				}
				else
				{
					System.out.println("Nie je reakcia.");
					
					/* zistime ci je osamely */
					int returning_pid = PrispevkyManager.existujeByID("prispevky_reakcie", "pris_id", id_prisp_na_zmaz);
					
					if (returning_pid == 0)
					{
						System.out.println("Dany prispevok je osamely. Mazem ho...");
						PrispevkyManager.zmazZaznam("prispevky", id_prisp_na_zmaz);
					}
					else
					{
						String volba_zmazu = Readers.nahrajVstupString("Dany prispevok nie je osamely. Mam zmazat vs. reakcie? (a/n)");
						
						if(volba_zmazu.equals("a"))
						{
							/* mazeme vs. prispevky */
							List<PrispevokLoaded> idcka_reakcii = new ArrayList<PrispevokLoaded>();
							idcka_reakcii = PrispevkyManager.zistiReakcie(id_prisp_na_zmaz);
							
							for(PrispevokLoaded pris : idcka_reakcii)
							{
								/* debug - idcka reakcii daneho clanku */
								System.out.println(pris.getPID() + " ");
								/* debug - end */
								PrispevkyManager.zmazZaznam("prispevky", pris.getPID());
								ElasticStuff.zmazZelasticu("projekt", "prispevky", pris.getPID());
							}
							
							/* mazeme reakcie */
							
							
							/* mazeme hlavny prispevok */
							PrispevkyManager.zmazZaznam("prispevky", id_prisp_na_zmaz);
							ElasticStuff.zmazZelasticu("projekt", "prispevky", id_prisp_na_zmaz);
						}
						else if (volba_zmazu.equals("n"))
						{
							/* nechame tak */
						}
						else
						{
							System.out.println("Zla volba");
						}
					}
				}
			}
			
		}
		/*
		 * ZMAZANIE KONTA
		 */
		else if (prikaz.equals("vymazanie konta") || prikaz.equals("zmazanie konta") || prikaz.equals("zmaz konto") || prikaz.equals("zk"))
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
		/* 
		 * ZOBRAZENIE KONT UZIVATELOV FORA 
		 */
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
		else if (prikaz.equals("zobrazenie fora") || prikaz.equals("zobf") || prikaz.equals("ls"))
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
					System.out.println("   * " + t.getNazov() + "(" + t.getID() + ")");
					
					List<PrispevokLoaded> vs_prispevky = PrispevkyManager.ziskajvsPrispevkybyTID(t.getID());
					for(PrispevokLoaded p : vs_prispevky)
					{
						System.out.println("     * " + p.getSubjekt() + " " + "(" + p.getPID() + ")");
					}
				}
			}
		}
		/*
		 * PRIDANIE FORA
		 */
		else if (prikaz.equals("pridanie fora") || prikaz.equals("pf"))
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
		/* 
		 * ZMAZANIE FORA 
		 */
		else if (prikaz.equals("zmazanie fora") || prikaz.equals("zf"))
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
					/* najprv mazeme temy suvisiace s forom */
					List<Tema> temy = PrispevkyManager.ziskajvsTemybyFID(forum_id);
					
					for(Tema t : temy)
					{
						zmazanieTemy(t.getID());
					}
					//PrispevkyManger.zistiIDprispPreForum(forum_id);
					/* nakoniec zmazeme forum */
					PrispevkyManager.zmazZaznam("fora", forum_id);
					
					/* mazeme vs. temy suvisiace s forom */
					
					System.out.println("Zaznam bol zmazany.");
				}
				else
				{
					System.out.println("Zadali ste zle id.");
				}
			
			}
		}
		/* 
		 * VYPISANIE PRISPEVKOV PODLA ID TEMY 
		 */
		else if (prikaz.equals("vypisanie prispevkov") || prikaz.equals("vp"))
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
		/* PRIDANIE PRISPEVKU */
		else if (prikaz.equals("pridanie prispevku") || prikaz.equals("pp"))
		{
			/* nacitame do akej temy pridavame */
			int tema_id;
			System.out.print("Zvolte id (id) TEMY: ");
			Scanner tema_id_skener = new Scanner(System.in);
			tema_id = tema_id_skener.nextInt();
			
			/* zistime ci tema existuje */
			Tema vratena_tema = PrispevkyManager.existujeTID(tema_id);
			
			/* zistime ci sme prihlaseny */
			if (akt_prihlas.konto.getNick().equals("guest"))
			{
				System.out.println("Nie ste prihlaseny!");

			}
			else
			{
				if (vratena_tema == null)
				{
					System.out.println("Tema zo zadanym id neexistuje.");
				}
				else
				{
					/* samotne pridanie prispevku */
					String subjekt = Readers.nahrajVstupString("Vloz predmet prispevku: ");
					String obsah = Readers.nahrajVstupString("Vloz obsah clanku: ");
					int aid = KontaManager.zistiAutorID(akt_prihlas.konto.getNick());
				
					int pid = PrispevkyManager.pridajPrispevok(subjekt, aid, obsah, tema_id, false);
					
					/* Novy prispevok obj. pre elastic */
					PrispevokLoaded vytv_prispevok = new PrispevokLoaded();
					vytv_prispevok.setAutor(akt_prihlas.konto.getNick());
					vytv_prispevok.setSubjekt(subjekt);
					vytv_prispevok.setObsah(obsah);
					vytv_prispevok.setPID(pid);
					
					/* ak bolo pridanie uspesne pridame aj do elasticu - toto treba odladit */
					if (pid > 0)
					{
						/* pridame aj do elasticu */
						ElasticStuff.pridajDoelasticu("projekt", "prispevky", vytv_prispevok,0 /* nie je reakcia */);
					}
				}
			}
		}
		/* PRIDANIE REAKCIE */
		else if(prikaz.equals("pridanie reakcie") || prikaz.equals("pr"))
		{
			/* najprv chceme zadat id prispevku na kt. reagujeme */
			int id_prispevku = Readers.nahrajVstupInt("Zadajte ID prisp. na kt. regujete: ");
			
			/* vyhladame ci sa prispevok nachadza v db */
			int pid = PrispevkyManager.existujeByID("prispevky", "id", id_prispevku);
			
			/* debug */
			System.out.println("Najdene pid je: " + pid);
			
			if(pid > 0)
			{
				/* ID prispevku sa naslo */
				/* pridavame reakciu */
				pridajReakciuNaPrisp(id_prispevku, pid);
				
			}
			else
			{
				/* ID prispevku sme nanasli */
				System.out.println("Zadane ID prispevku neexistuje.");
			}
		}
		/* VYPISANIE REAKCII NA PRISPEVOK */
		else if(prikaz.equals("vypisanie reakcii") || prikaz.equals("vr"))
		{
			/* Vlozime ID clanku na kt. chceme reagovat */
			int id_clanku_na_reakcie = Readers.nahrajVstupInt("Vloz ID clanku: ");
			
			/* Overime ci existuje */
			int existuje_clanok = PrispevkyManager.existujeByID("prispevky", "id", id_clanku_na_reakcie);
			if(existuje_clanok == 0)
			{
				System.out.println("Prispevok neexistuje!");
			}
			else
			{
				/* Zistenie vs. reakcii podla ID (PID) clanku */
				List<PrispevokLoaded> reakcie = PrispevkyManager.zistiReakcie(id_clanku_na_reakcie);
			
				if (reakcie.isEmpty())
				{
					System.out.println("Nenasli sa ziadne reakcie.");
				}
				else
				{
					/* Vypisanie clanku */
					Prispevok prisp = PrispevkyManager.vypisPrispevok(id_clanku_na_reakcie);
					System.out.println("Prispevok c. " + id_clanku_na_reakcie);
					System.out.println("Autor: " + prisp.getAutor());
					System.out.println("Subjekt: " + prisp.getSubjekt());
					System.out.println("Obsah: " + prisp.getObsah());
					System.out.println(".. ma nasledovne reakcie ..");
					
					/* Vypisanie reakcii */
					for (PrispevokLoaded pl : reakcie)
					{
						System.out.println("-----------------------");
						System.out.println("Prispevok c. " + pl.getPID() + " | Autor: " + pl.getAutor());
						System.out.println("Predmet: " + pl.getSubjekt());
						System.out.println("Obsah: " + pl.getObsah());
					}
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
		/*
		 * VYTVORENIE TEMY
		 */
		else if(prikaz.equals("vytvorenie temy") || prikaz.equals("vt"))
		{
			/* zistime ci som admin */
			if (akt_prihlas.konto.getJeAdmin() == true)
			{
				/* Vytvor temu */
				int id_fora = Readers.nahrajVstupInt("Zadajte id fora pre vytv. temy: ");
				
				/* Over ci existuje forum */
				Forum zist_forum = PrispevkyManager.existujeFID(id_fora);
				
				if (zist_forum == null)
				{
					System.out.println("Dane forum neexistuje.");
				}
				else
				{
					String tema_meno = Readers.nahrajVstupString("Zadajte nazov temy: ");
					String tema_popis = Readers.nahrajVstupString("Zadajte popis temy: ");
					
					/* zanesieme temu do tem */
					String nick = akt_prihlas.konto.getNick();
					PrispevkyManager.vytvorTemu(tema_meno, tema_popis, nick, id_fora);
				}
				
			}
			else
			{
				System.out.println("Nie ste prihlaseny ako administrator");
			}
		}
		/*
		 * ZMAZANIE TEMY
		 */
		else if(prikaz.equals("zmazanie temy") || prikaz.equals("zt"))
		{
			/* nacitanie id temy */
			int id_temy = Readers.nahrajVstupInt("Zadaj id temy: ");
			
			/* over ci tema existuje */
			Tema tema_exist = PrispevkyManager.existujeTID(id_temy);
			
			if (tema_exist == null)
			{
				System.out.println("Zadana tema neexistuje.");
			}
			else
			{
				
				/* ziskame vs. prispevky z temy */
				//List<PrispevokLoaded> prispevky = PrispevkyManager.ziskajvsPrispevkybyTID(id_temy);
				
				//for (PrispevokLoaded pl : prispevky)
				//{
					/* zistit reakcie */
					//List<PrispevokLoaded> reakcie = PrispevkyManager.zistiReakcie(pl.getPID());
					
					/* zmazat reakcie */
					//for (PrispevokLoaded rl : reakcie)
					//{
						//PrispevkyManager.zmazZaznam("prispevky", rl.getPID());
						//ElasticStuff.zmazZelasticu("projekt", "prispevky", rl.getPID());
					//}
					
					/* zmazat prispevok */
					//PrispevkyManager.zmazZaznam("prispevky", pl.getPID());
					//ElasticStuff.zmazZelasticu("projekt", "prispevky", pl.getPID());
				//}
				
				/* zmazanie samotnej temy */
				//PrispevkyManager.zmazZaznam("temy", id_temy);
				
				zmazanieTemy(id_temy);
			}
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
	static void pridajReakciuNaPrisp(int id_prispevku, int tid) throws SQLException, IOException
	{
		String subjekt = Readers.nahrajVstupString("Vlozte predmet reakcie: ");
		String obsah = Readers.nahrajVstupString("Vlozte text reakcie: ");
		String autor = akt_prihlas.konto.getNick();
		
		/* musime ziskat ID autora */
		int autor_id = VseobManager.existujeByID("konta", "id", "nick", autor);
		
		/* ak autora nenajde, tak niesme prihlaseny */
		if (autor_id == 0)
		{
			System.out.println("Nie ste prihlaseny");
		}
		
		/* vlozime prispevok do tabulkz prispevkov */
		/* vlozime prispevok do tabulky prispevkov + reakcii */
		int id_pridaneho = PrispevkyManager.pridajReakciu(subjekt, autor_id, obsah, tid, id_prispevku);
		
		/* vlozime aj do elasticu */
		ElasticStuff.pridajDoelasticu("projekt", "prispevky", new PrispevokLoaded(akt_prihlas.konto.getNick(), subjekt, obsah, id_pridaneho), id_prispevku);
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
	
	static Boolean nahrajDoElasticu()
	{
		boolean vysledok = false;
		
		return vysledok;
	}
	
	static void hladajFulltext(String hladane_slovo)
	{
		//QueryBuilder qb = QueryBuilders.termQuery("multi", "test");
		
		SearchResponse sResponse = elastic_client.prepareSearch("projekt")				//V indexe artists
		        .setTypes("prispevky")								//Ktory ma typ logs
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)					
		        .setQuery(QueryBuilders.matchPhrasePrefixQuery("obsah", hladane_slovo))                	// name = atribut, m = query 
		        //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     		// Filter, nepotrebne
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
		
		
		// vypis
    	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    	
    	List<String> valuesList= new ArrayList<String>();		
    	for (SearchHit hit : sResponse.getHits()) {                      			//Prechadzanie vysledkom searchy
    		result.add(hit.getSource());
    		valuesList.add(hit.getSource().get("subjekt").toString() + " " + hit.getSource().get("obsah").toString());   			//Chceme atribut "name"	
    		System.out.println("Clanok c. " + hit.getSource().get("id").toString() + " " +
    					hit.getSource().get("subjekt").toString() + 
    				" " +hit.getSource().get("obsah").toString());			//Vypis
    	}
	}
	
	static void zmazanieTemy(int id_temy) throws SQLException
	{
		/* ziskame vs. prispevky z temy */
		List<PrispevokLoaded> prispevky = PrispevkyManager.ziskajvsPrispevkybyTID(id_temy);
		
		for (PrispevokLoaded pl : prispevky)
		{
			/* zistit reakcie */
			List<PrispevokLoaded> reakcie = PrispevkyManager.zistiReakcie(pl.getPID());
			
			/* zmazat reakcie */
			for (PrispevokLoaded rl : reakcie)
			{
				PrispevkyManager.zmazZaznam("prispevky", rl.getPID());
				ElasticStuff.zmazZelasticu("projekt", "prispevky", rl.getPID());
			}
			
			/* zmazat prispevok */
			PrispevkyManager.zmazZaznam("prispevky", pl.getPID());
			ElasticStuff.zmazZelasticu("projekt", "prispevky", pl.getPID());
		}
		
		/* zmazanie samotnej temy */
		PrispevkyManager.zmazZaznam("temy", id_temy);
	}
}
