package model;

/* Uzivatel s kt. pracuje aplikacia */
public class UserLoaded extends User
{
	int pocet_prispevkov;
	
	public UserLoaded(String nick, String datum, String email, int pocet_prispevkov)
	{
		setNick(nick);
		setEmail(email);
		this.pocet_prispevkov = pocet_prispevkov;
	}
	
	public int ziskajPocetPrisp()
	{
		return pocet_prispevkov;
	}
}