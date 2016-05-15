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
<<<<<<< HEAD
				reakcie.add(new PrispevokLoaded(rs.getString("nick"), rs.getString("subject"), rs.getString("obsah"), pid));
=======
				reakcie.add(new PrispevokLoaded(rs.getString("nick"), rs.getString("subject"), rs.getString("obsah"), rs.getInt("reakcia_id")/*pid*/));
>>>>>>> refs/remotes/origin/master
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
	
<<<<<<< HEAD
=======
	/* ZISKAJ VS. PRISPEVKY */
	public static List<PrispevokLoaded> ziskajvsPrispevky() throws SQLException
	{
		List<PrispevokLoaded> vsetky_prispevky = new LinkedList<PrispevokLoaded>();
		Connection spojenie = null;
		Statement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			
			String sql = "select *, prispevky.id as pid from prispevky inner join konta on prispevky.autor_id = konta.id";
			prikaz = spojenie.createStatement();
			ResultSet rs = prikaz.executeQuery(sql);
			while(rs.next()){
				vsetky_prispevky.add(new PrispevokLoaded(rs.getString("nick"),rs.getString("subject"),rs.getString("obsah"), rs.getInt("pid")));
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
	public static User vytvorTemu(String nazov, String popis, String nick, int forum_id) throws SQLException
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
			String sql_vytvor_temu= "INSERT INTO temy (nazov, autor_id, forum_id) VALUES (?,?,?)";
			prikaz1 = (PreparedStatement) spojenie.prepareStatement(sql_ziskaj_autora);
			prikaz2 = (PreparedStatement) spojenie.prepareStatement(sql_vytvor_temu);
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
			prikaz2.setInt(2, autor_id);
			prikaz2.setInt(3, forum_id);
			
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
	
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
	 public static void pridajPrispevok(String subjekt, int aid, String obsah, int tid) throws SQLException
=======
	 public static int pridajPrispevok(String subjekt, int aid, String obsah, int tid, boolean je_reakcia) throws SQLException
>>>>>>> refs/remotes/origin/master
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
					
<<<<<<< HEAD
				String sql_vloz_do_prispevkov = "INSERT INTO prispevky (obsah, autor_id, subjekt) "
						+ " values (?, ?, ?)";
				String sql_napln_temy_prispevky = "INSERT INTO temy_prispevky (t_id, p_id) VALUES (?,?)";
				//String sql_napln_prisp_reakcie = "INSERT INTO prispevky_reakcie (pris_id, reakcia_id) VALUES(?,?)";
				
				prikaz1 = (PreparedStatement) spojenie.prepareStatement(sql_vloz_do_prispevkov);
				prikaz2 = (PreparedStatement) spojenie.prepareStatement(sql_napln_temy_prispevky);
				//prikaz3 = (PreparedStatement) spojenie.prepareStatement(sql_napln_prisp_reakcie);
=======
				String sql_vloz_do_prispevkov = "INSERT INTO prispevky (id, obsah, autor_id, subject, je_reakcia) "
						+ " values (DEFAULT, ?, ?, ?, ?) RETURNING id";
				String sql_napln_temy_prispevky;
				sql_napln_temy_prispevky = "INSERT INTO temy_prispevky (t_id, p_id) VALUES (?,?)";

				
				prikaz1 = (PreparedStatement) spojenie.prepareStatement(sql_vloz_do_prispevkov);
				prikaz2 = (PreparedStatement) spojenie.prepareStatement(sql_napln_temy_prispevky);
>>>>>>> refs/remotes/origin/master
				
				/* prikaz 1 - vloz do prispevok do <<prispevky>> */
				prikaz1.setString(1, obsah);
				prikaz1.setInt(2, aid);
				prikaz1.setString(3, subjekt);
<<<<<<< HEAD
=======
				prikaz1.setBoolean(4, je_reakcia);
>>>>>>> refs/remotes/origin/master
				prikaz1.execute();
				
				ResultSet rs = prikaz1.getResultSet();
				
<<<<<<< HEAD
=======
				/* debug */
				
				
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
					//System.out.println("OK");
				}
				//return vsetky_temy;
=======
					System.out.println("pid bolo: " + pid);
					return pid;
				}
	 }
	 
	 public static int pridajReakciu(String subjekt, int aid, String obsah, int pid, int prispevok) throws SQLException
	 {	
		 	//int pid = 0;
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
					
				String sql_vloz_do_prispevkov = "INSERT INTO prispevky (id, obsah, autor_id, subject, je_reakcia) "
						+ " values (DEFAULT, ?, ?, ?, true) RETURNING id";
				String sql_napln_reakcie_prispevky;
		
				sql_napln_reakcie_prispevky = "INSERT INTO prispevky_reakcie (pris_id, reakcia_id) VALUES (?,?)";
		
				
				prikaz1 = (PreparedStatement) spojenie.prepareStatement(sql_vloz_do_prispevkov);
				prikaz2 = (PreparedStatement) spojenie.prepareStatement(sql_napln_reakcie_prispevky);
				
				/* prikaz 1 - vloz do prispevok do <<prispevky>> */
				prikaz1.setString(1, obsah);
				prikaz1.setInt(2, aid);
				prikaz1.setString(3, subjekt);
				//prikaz1.setBoolean(4, je_reakcia);
				prikaz1.execute();
				
				ResultSet rs = prikaz1.getResultSet();
				
				/* debug */
				
				
				while(rs.next())
				{
					pid = rs.getInt("id");
				}
				
				/* prikaz 2 - vloz prispevok do <<temy_prispevky>> */
				prikaz2.setInt(1, prispevok);
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
				return pid;
>>>>>>> refs/remotes/origin/master
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
				
<<<<<<< HEAD
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
=======
				prikaz.setInt(1, ftp_id);
				
				prikaz.execute();
				
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
	 
	 /* ZISTI IDCKA PRISPEVKOV PATRIACE PRE FORUM */
	 public static List<Integer> zistiIDprispPreForum(int forum_id) throws SQLException
	 {
		 	List<Integer> zistene_id = new ArrayList<Integer>();
		 	
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				prikaz.setInt(1, forum_id);
				
				prikaz.execute();
				ResultSet rs = prikaz.getResultSet();
				
				while(rs.next())
				{
					zistene_id.add(rs.getInt("id"));
				}
				
				/* este musime zistit reakcie pre jednot. prispevky */
>>>>>>> refs/remotes/origin/master
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
<<<<<<< HEAD
			//return zistene_forum;	
		}
	 
=======
			return zistene_id;	
		    
	 }
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
	 public static int existujeByID(String typ, int id) throws SQLException
=======
	 public static int existujeByID(String typ, String col_typ, int id) throws SQLException
>>>>>>> refs/remotes/origin/master
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
				
<<<<<<< HEAD
				String sql = "SELECT * FROM " + typ + " WHERE id = ?";
=======
				String sql = "SELECT * FROM " + typ + " WHERE " + col_typ + " = ?";
>>>>>>> refs/remotes/origin/master
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				prikaz.setInt(1, id);
				
				prikaz.execute();
				ResultSet rs = prikaz.getResultSet();
				
				while(rs.next())
				{
<<<<<<< HEAD
					tid = rs.getInt("t_id");
=======
					tid = rs.getInt(col_typ); // tu bolo id keby chyba
>>>>>>> refs/remotes/origin/master
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
	 
<<<<<<< HEAD
	
=======
	/* ZISTI INFO O ZAZNAME */
	 public static boolean je_reakcia(int id_prispevku) throws SQLException
	 {
		 boolean je_reakcia = false;
		 
		 Connection spojenie = null;
		 PreparedStatement prikaz = null;
		 Properties vlast_spojenia = new Properties();
		 vlast_spojenia.put("user", "postgres");
		 vlast_spojenia.put("password", "macka");
		 String url = "jdbc:postgresql://localhost:5432/projekt";
		 
		 spojenie = DriverManager.getConnection(url, vlast_spojenia);
	     spojenie.setAutoCommit(false);
		 
	     String sql = "SELECT je_reakcia FROM prispevky WHERE id = ?";
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.setInt(1, id_prispevku);
			
			prikaz.execute();
			
			ResultSet rs = prikaz.getResultSet();
			
			while(rs.next())
			{
				je_reakcia = rs.getBoolean("je_reakcia");
			}
			
			spojenie.commit();
			
			spojenie.close();
			prikaz.close();
		 
		 return je_reakcia;
	 }
>>>>>>> refs/remotes/origin/master
}
