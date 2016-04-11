package nastroje;

import java.util.*;
import java.sql.*;

public class Defs {
	public static final String db_nazov = "projekt";
	public static final String db_url = "jdbc:postgresql://localhost:5432/projekt";
	public static final String db_user = "postgres";
	public static final String db_pass = "macka";
	
	public static void vytvorVlastSpoj()
	{
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
	}
}
