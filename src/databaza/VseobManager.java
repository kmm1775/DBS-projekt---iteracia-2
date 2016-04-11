package databaza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class VseobManager {
	 /* OVERENIE EXISTENCIE PODLA ID */
	/*
	 public static <T> existujeByVAL(String tabulka, String co_stlpec, String kde_stlpec, <T> val) throws SQLException
		{
		 	T val;
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "SELECT " + co_stlpec + " FROM " + tabulka + " WHERE " +
				kde_stlpec + " = " + "?";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				if (val instanceof Integer)
				{
					prikaz.setInt(1, (int) val);
				}
				else
				{
					prikaz.setString(1, (String) val);
				}
				
				prikaz.execute();
				ResultSet rs = prikaz.getResultSet();
				
				val = rs.next();
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
			
		}
	 */
	
	public static int existujeByID(String tabulka, String co_stlpec, String kde_stlpec, String val) throws SQLException
	{
		 	int ziskane_id=0;
			Connection spojenie = null;
			PreparedStatement prikaz = null;
			Properties vlast_spojenia = new Properties();
		    vlast_spojenia.put("user", "postgres");
		    vlast_spojenia.put("password", "macka");
		    String url = "jdbc:postgresql://localhost:5432/projekt";
		    try {
				spojenie = DriverManager.getConnection(url, vlast_spojenia);
				spojenie.setAutoCommit(false);
				
				String sql = "SELECT " + co_stlpec + " FROM " + tabulka + " WHERE " +
				kde_stlpec + " = " + "?";
				prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
				
				prikaz.setString(1, val);
				
				prikaz.execute();
				
				ResultSet rs = prikaz.getResultSet();
				
				while(rs.next())
				{
					ziskane_id = rs.getInt("id");
				}
				
				spojenie.commit();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				prikaz.close();
				spojenie.close();
			}
			return ziskane_id;
		}
	
	public static int zistiPocty(String tabulka) throws SQLException
	{
		int pocet=0;
		Connection spojenie = null;
		PreparedStatement prikaz = null;
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	    String url = "jdbc:postgresql://localhost:5432/projekt";
	    try {
			spojenie = DriverManager.getConnection(url, vlast_spojenia);
			spojenie.setAutoCommit(false);
			// select count(konta.nick) as pocet_registr from konta;
			String sql = "SELECT count(" + tabulka + ") as pocet FROM " + tabulka;
			prikaz = (PreparedStatement) spojenie.prepareStatement(sql);
			
			prikaz.execute();
			
			ResultSet rs = prikaz.getResultSet();
			
			while(rs.next())
			{
				pocet = rs.getInt("pocet");
			}
			
			spojenie.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			prikaz.close();
			spojenie.close();
		}	
		
		
		return pocet;
	}
}