package aplikacia;

import java.text.*;
import java.util.*;

import model.*;

public class Prihlasenie {
	User konto;
	Date datum_prihlasenia;
	
	
	Prihlasenie()
	{
		this.konto = new User("guest");
		this.datum_prihlasenia = new Date();
<<<<<<< HEAD
=======
		this.konto.setEmail("nenastaveny");
>>>>>>> refs/remotes/origin/master
	}
	
	Prihlasenie(User konto)
	{
		this.konto = konto;
		this.datum_prihlasenia = new Date();
	}
}
