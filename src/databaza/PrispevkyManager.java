package databaza;

import java.sql.*;
import java.util.*;

import javafx.collections.*;
import model.Forum;
import model.Prispevok;
import model.PrispevokLoaded;
import model.Tema;
import model.User;

public class PrispevkyManager {

	/* ZISTENIE PRISPEVKOV - REAKCII PODLA ID */
	public static List<PrispevokLoaded> zistiReakcie(int pid) throws SQLException
	{
		List<PrispevokLoaded> reakcie = new LinkedList<PrispevokLoaded>();
		
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "select * from prispevky_reakcie "
					+ " inner join prispevky on reakcia_id = prispevky.id"
					+ " inner join konta on autor_id = konta.id"
					+ " where pris_id = ?";
			
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setInt(1, pid);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			/* je len jeden */
			while(rs.next())
			{
				reakcie.add(new PrispevokLoaded(rs.getString("nick"), rs.getString("subject"), rs.getString("obsah"), pid));
			}
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		return reakcie;		
	}
	
	/* VYPISANIE PRISPEVKU BY ID */
	public static Prispevok vypisPrispevok(int pid) throws SQLException
	{
		Prispevok prispevok = null;
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "select * from prispevky inner join konta on prispevky.autor_id = konta.id where prispevky.id = ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setInt(1, pid);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			/* je len jeden */
			while(rs.next())
			{
				prispevok = new Prispevok(rs.getString("nick"), rs.getString("subject"), rs.getString("obsah"));
			}
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		return prispevok;
	}
	
	public static List<Forum> ziskajVsFora() throws SQLException
	{
		List<Forum> vsetky_fora = new LinkedList<Forum>();
		Connection spojenie = null;
		Statement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			prikaz = spojenie.createStatement();
			ResultSet rs = prikaz.executeQuery("SELECT *, fora.id as fid FROM fora JOIN konta ON fora.autor_id = konta.id");
			while(rs.next()){
				vsetky_fora.add(new Forum(rs.getString("nick"),rs.getString("popis"),rs.getString("nazov"), rs.getInt("fid")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
	    return vsetky_fora;
	}
	
	public static List<Tema> ziskajvsTemybyFID(int fid) throws SQLException
	{
		List<Tema> vsetky_temy = new LinkedList<Tema>();
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "SELECT * FROM temy WHERE forum_id = ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setInt(1, fid);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			while(rs.next())
			{
				vsetky_temy.add(new Tema(rs.getString("nazov"),rs.getInt("id")));
			}
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		return vsetky_temy;
	}
	
	public static List<PrispevokLoaded> ziskajvsPrispevkybyTID(int tid) throws SQLException
	{
		List<PrispevokLoaded> vsetky_prispevky = new LinkedList<PrispevokLoaded>();
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "select * from temy_prispevky inner join prispevky on prispevky.id = p_id join konta on autor_id = konta.id where t_id = ? and je_reakcia = false order by datum_vytvorenia DESC;";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setInt(1, tid);
			
			prikaz.execute();
			ResultSet rs = prikaz.getResultSet();
			
			while(rs.next())
			{
				vsetky_prispevky.add(new PrispevokLoaded(rs.getString("nick"),rs.getString("subject"),rs.getString("obsah"),rs.getInt("p_id")));
			}
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}
		return vsetky_prispevky;
	}
	
	/* VYTVORENIE FORA */
	public static User vytvorForum(String nazov, String popis, String nick) throws SQLException
	{
		User zisteny_user = null;
		
		Connection spojenie = null;
		PreparedStatement prikaz1 = null;
		PreparedStatement prikaz2 = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql_ziskaj_autora = "SELECT id FROM konta WHERE nick = ?";
			String sql_vytvor_forum = "INSERT INTO fora (nazov, popis, autor_id) VALUES (?,?,?)";
			prikaz1 = (PreparedStatement) spojenie.prepareStatement(sql_ziskaj_autora);
			prikaz2 = (PreparedStatement) spojenie.prepareStatement(sql_vytvor_forum);
			prikaz1.setString(1, nick);
			
			prikaz1.execute();
			
			ResultSet rs = prikaz1.getResultSet();
			
			int autor_id = 0;
			
			if(rs.next())
			{
			      autor_id = rs.getInt(1);
			}
			
			// teraz ho insertneme
			prikaz2.setString(1, nazov);
			prikaz2.setString(2, popis);
			prikaz2.setInt(3, autor_id);
			
			prikaz2.execute();
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz1.close();
			prikaz2.close();
			spojenie.close();
			//System.out.println("OK");
		}
		//return vsetky_temy;
		return zisteny_user;
	}

	
	/* PRIDANIE PRISPEVKU / REAKCIE */
	 public static void pridajPrispevok(String subjekt, int aid, String obsah, int tid) throws SQLException
	 {	
		 	int pid = 0;
			Connection spojenie = null;
			PreparedStatement prikaz1 = null;
			PreparedStatement prikaz2 = null;
			Properties vlast_spojenia = new Properties();
			vlast_spojenia.put("user", "postgres");
			vlast_spojenia.put("password", "macka");
			String url = "jdbc:postgresql://localhost:5432/projekt";
			    
			try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
					
				String sql_vloz_do_prispevkov = "INSERT INTO prispevky (obsah, autor_id, subjekt) "
						+ " values (?, ?, ?)";
				String sql_napln_temy_prispevky = "INSERT INTO temy_prispevky (t_id, p_id) VALUES (?,?)";
				//String sql_napln_prisp_reakcie = "INSERT INTO prispevky_reakcie (pris_id, reakcia_id) VALUES(?,?)";
				
				prikaz1 = (PreparedStatement) spojenie.prepareStatement(sql_vloz_do_prispevkov);
				prikaz2 = (PreparedStatement) spojenie.prepareStatement(sql_napln_temy_prispevky);
				//prikaz3 = (PreparedStatement) spojenie.prepareStatement(sql_napln_prisp_reakcie);
				
				/* prikaz 1 - vloz do prispevok do <<prispevky>> */
				prikaz1.setString(1, obsah);
				prikaz1.setInt(2, aid);
				prikaz1.setString(3, subjekt);
				prikaz1.execute();
				
				ResultSet rs = prikaz1.getResultSet();
				
				while(rs.next())
				{
					pid = rs.getInt("id");
				}
				
				/* prikaz 2 - vloz prispevok do <<temy_prispevky>> */
				prikaz2.setInt(1, tid);
				prikaz2.setInt(2, pid);
				prikaz2.execute();
					
				spojenie.commit();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					prikaz1.close();
					prikaz2.close();
					spojenie.close();
					//System.out.println("OK");
				}
				//return vsetky_temy;
	 }
	 
	 /* OVERENIE EXISTENCIE FORA */
	 public static Forum existujeFID(int fid) throws SQLException
		{
			Forum zistene_forum = null;
			
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "SELECT * FROM fora WHERE id = ?";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				prikaz.setInt(1, fid);
				
				prikaz.execute();
				ResultSet rs = prikaz.getResultSet();
				
				while(rs.next())
				{
					zistene_forum = new Forum(/*rs.getString("autor")*/"null", rs.getString("popis"), rs.getString("nazov"), rs.getInt("id"));
				}
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
			return zistene_forum;	
		}
	 
	 /* ZMAZANIE FORA */
	 public static void zmazZaznam(String skadial, int ftp_id) throws SQLException
		{
			//Forum zistene_forum = null;
			
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "delete from " + skadial + " where id = ?";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				//prikaz.setString(1, skadial);
				prikaz.setInt(1, ftp_id);
				
				prikaz.execute();
				//ResultSet rs = prikaz.getResultSet();
				
				/*
				while(rs.next())
				{
					zistene_forum = new Forum(rs.getString("autor"), rs.getString("popis"), rs.getString("nazov"), rs.getInt("id"));
				}
				*/
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
			//return zistene_forum;	
		}
	 
	 /* OVERENIE EXISTENCIE TEMY */
	 public static Tema existujeTID(int tid) throws SQLException
		{
			Tema zistena_tema = null;
			
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "SELECT * FROM temy WHERE id = ?";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				prikaz.setInt(1, tid);
				
				prikaz.execute();
				ResultSet rs = prikaz.getResultSet();
				
				while(rs.next())
				{
					zistena_tema = new Tema(rs.getString("nazov"),  rs.getInt("id"));
				}
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
			return zistena_tema;	
		}
	 
	 /* OVERENIE EXISTENCIE PODLA ID */
	 public static int existujeByID(String typ, int id) throws SQLException
		{
		 	int tid = 0;
		 	boolean najdene = false;
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "SELECT * FROM " + typ + " WHERE id = ?";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				prikaz.setInt(1, id);
				
				prikaz.execute();
				ResultSet rs = prikaz.getResultSet();
				
				while(rs.next())
				{
					tid = rs.getInt("t_id");
				}
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
			return tid;	
		}
	 
	
}
