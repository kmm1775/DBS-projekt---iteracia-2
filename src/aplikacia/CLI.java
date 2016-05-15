package aplikacia;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import static org.elasticsearch.common.xcontent.XContentFactory.*;	// JSON builder

import nastroje.Readers;

public class CLI {
	void spusti(Prihlasenie akt_prihlas) throws SQLException, IOException
	{
		boolean loop = true;	// premenna pre loop
		String prikaz = "null";	// startovaci prikaz
		Spracovanie.akt_prihlas = akt_prihlas;	// vytvorenie guesta
		
		/* vytvorenie elastic klienta */
		Spracovanie.elastic_client = TransportClient
				.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	
		/* tu prebieha samotne nacitavanie prikazov */
		while(loop)
		{			
			prikaz = Readers.nacitajPrikaz();	// nacitanie prikazu
			loop = Spracovanie.spracujPrikaz(prikaz);	// spracovanie prikazu
		}
	}
}
