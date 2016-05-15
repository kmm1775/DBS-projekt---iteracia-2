package databaza;

import java.sql.*;
import java.util.*;

import model.*;
import nastroje.Defs;

public class KontaManager {

	/* OVERIT EXISTENCIU KONTA */
	/* Pouziva pri prihlaseny, registracii */
	public static User existujeKonto(String nick, String heslo) throws SQLException
	{
		User zisteny_user = null;
		
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", Defs.db_user);
	    vlast_spojenia.put("password", Defs.db_pass);
	    String url = Defs.db_url;
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "SELECT * FROM konta WHERE nick = ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setString(1, nick);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			while(rs.next())
			{
				zisteny_user = new User(rs.getString("nick"), rs.getString("email"), rs.getBoolean("je_admin"));
			}
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		return zisteny_user;	
	}
	
	/* VYTVORENIE KONTA */
	/* Pouziva sa pri registracii */
	public static User vytvorKonto(String nick, String heslo, String email) throws SQLException
	{
		User zisteny_user = null;
		
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "INSERT INTO konta (nick, heslo, email) VALUES (?, ?, ?)";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setString(1, nick);
			prikaz.setString(2, heslo);
			prikaz.setString(3, email);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			
			/*while(rs.next())
			{
				zisteny_user = new User(rs.getString("nick"));
			}*/
			/**/
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		//return vsetky_temy;
		return zisteny_user;
	}
	
	/* UPRAVENIE KONTA */
	public static void upravKonto(String nick, String heslo, String email) throws SQLException
	{
		// TODO - cele tieto data na spojenie do jednej triedy treba dat
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");	// TODO - tieto hesla do vlastnej stat. triedy nastroje
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "UPDATE konta SET (email, heslo) = (?, ?) WHERE nick = ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setString(1, email);
			prikaz.setString(2, heslo);
			prikaz.setString(3, nick);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
	
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		//return nieco;
	}
	
	/* ZMAZANIE KONTA */
	public static void zmazKonto(String nick) throws SQLException
	{
		// TODO - cele tieto data na spojenie do jednej triedy treba dat
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");	// TODO - tieto hesla do vlastnej stat. triedy nastroje
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "DELETE FROM konta WHERE nick = ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setString(1, nick);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
	
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		//return nieco;
	}
	/* ZISKANIE PREHLADU O VSETKYCH UZIVATELOCH */
	public static List<UserLoaded> ziskajInfoKonta() throws SQLException{
		List<UserLoaded> stat_vs_konta = new LinkedList<UserLoaded>();
		
		Connection conn = null;
		Statement stmt = null;
		Properties connectionProps = new Properties();
	    connectionProps.put("user", "postgres");
	    connectionProps.put("password", "macka");
	    String connectionString = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			conn = DriverManager.getConnection(connectionString, connectionProps);
			stmt = conn.createStatement();
			String dotaz =
					"select konta.nick as autor, konta.datum_reg as datum_reg, konta.email as email, count(prispevky.id) as pocet_prispevkov "
					+ "from prispevky full outer join konta on konta.id = prispevky.autor_id "
					+ "group by autor, datum_reg, email order by datum_reg ASC";

			ResultSet rs = stmt.executeQuery(dotaz);
			while(rs.next()){
				stat_vs_konta.add(new UserLoaded(rs.getString("autor"),rs.getString("datum_reg"),rs.getString("email"), rs.getInt("pocet_prispevkov")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}
	    return stat_vs_konta;
	}
	/* FILTRUJ KONTA PODLA POZIADAVIEK UZIVATELA */
	public static List<VyslFiltra> filtrujKonta(String cast_mena, String datum_zac, String datum_kon, Boolean je_admin, Boolean je_mod, int min_pp) throws SQLException
	{
		// TODO - cele tieto data na spojenie do jednej triedy treba dat
		List<VyslFiltra> vysl_filtra = new LinkedList<VyslFiltra>();
		
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");	// TODO - tieto hesla do vlastnej stat. triedy nastroje
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "select konta.nick, count(autor_id) as pp from konta "
						+ " inner join prispevky on konta.id = prispevky.autor_id "
						+ " where datum_reg > (?::date) and datum_reg < (?::date) "
						//+ " and nick LIKE '%"+ "?" + "%'" 
						//+ " and je_moderator = ? and je_admin = ? "
						+ " group by konta.id"
						+ " having count(autor_id) > ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setString(1, datum_zac);
			prikaz.setString(2, datum_kon);
			//prikaz.setString(3, cast_mena);
			//prikaz.setBoolean(4, je_mod);
			//prikaz.setBoolean(5, je_admin);
			prikaz.setInt(3, min_pp);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			while(rs.next())
			{
				vysl_filtra.add(new VyslFiltra(rs.getString("nick"), rs.getInt("pp")));
			}
	
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		return vysl_filtra;
	}
	
	public static int zistiAutorID(String nick) throws SQLException
	{
		int autorid = 0;
		
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		
	    Properties vlast_spojenia = Defs.nastavVlastSpoj();
		String url = "jdbc:postgresql://localhost:5432/projekt";
		
	    spojenie = DriverManager.getConnection(url, vlast_spojenia);
	    
		spojenie.setAutoCommit(false);	// nastavenie transakcie
	    
	    String sql = "SELECT id FROM konta WHERE nick = ?";
	    
	    /* nastavenie SQL prikazu */
	    prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
		prikaz.setString(1, nick);
		prikaz.execute();
		
		/* vysledky prikazu */
		ResultSet rs = prikaz.getResultSet();
		
		/* zistujeme vysledky - malo by vratit iba jedno id, kedze PK */
		while(rs.next())
		{
			autorid = rs.getInt("id");
		}

		spojenie.commit(); // transakcia vykonaj
		
		prikaz.close();
		spojenie.close();
		
	    return autorid;
	}

}
