package model;

public class VyslFiltra {
	public String nick;
	public int pp;
	
	public VyslFiltra(String nick, int pp)
	{
		this.nick = nick;
		this.pp = pp;
	}
	
	public int getPP()
	{
		return pp;
	}
	
	public String getNick()
	{
		return nick;
	}
}
