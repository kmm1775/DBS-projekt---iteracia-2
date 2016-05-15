package nastroje;

import java.util.*;
import java.sql.*;

public class Defs {
<<<<<<< HEAD
=======
	/* postgres */
>>>>>>> refs/remotes/origin/master
	public static final String db_nazov = "projekt";
	public static final String db_url = "jdbc:postgresql://localhost:5432/projekt";
	public static final String db_user = "postgres";
	public static final String db_pass = "macka";
	
<<<<<<< HEAD
	public static void vytvorVlastSpoj()
=======
	/* elastic */
	public static final String el_moj_index = "projekt";
	public static final String el_moj_typ = "prispevky";
	
	/* debug */
	public static final int debug_vypis = 0;
	
	/* Properties spojenia pre Postgres */
	public static Properties nastavVlastSpoj()
>>>>>>> refs/remotes/origin/master
	{
		Properties vlast_spojenia = new Properties();
	    vlast_spojenia.put("user", "postgres");
	    vlast_spojenia.put("password", "macka");
<<<<<<< HEAD
=======
	    
	    return vlast_spojenia;
>>>>>>> refs/remotes/origin/master
	}
}
