package databaza;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.lucene.queryparser.xml.FilterBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;

import aplikacia.Spracovanie;
import model.*;
import nastroje.Defs;
import nastroje.Filter;
import nastroje.Readers;

public class ElasticStuff {
	static Client el_klient = Spracovanie.elastic_client;
	
	/* FULLTEXT VYHLADAVANIE V ELASTICU */
	public static void hladajVelasticu2(String autor, String subjekt, String obsah,
			String date_start, String date_end, int reakcia, int limit_velkosti, int debug)
	{
		/* DEBUG */
		if (debug == 1 || Defs.debug_vypis == 1)
		{
			System.out.println(
					"Autor: " + autor
					+ " |Subjekt: " + subjekt
					+ " |Obsah: " + obsah
					+ " |date_start: " + date_start
					+ " |date_end: "  + date_end
					+ " |reakcia: " + reakcia
					+ " |limit v. " + limit_velkosti
					);
		}
		/* cele to zaobalime do boolQuery */
		BoolQueryBuilder hlavna_query = QueryBuilders.boolQuery();
		
		/* wildcard query pre autora */
		//QueryBuilder wild_autor = QueryBuilders.wildcardQuery("autor", autor);
		//QueryBuilder qsq_autor = QueryBuilders.queryStringQuery(autor).field("autor");
		QueryBuilder match_autor = QueryBuilders.matchQuery("autor", autor).operator(MatchQueryBuilder.Operator.OR);
		
		/* match query pre subjekt */
		QueryBuilder match_subjekt = QueryBuilders.matchQuery("subjekt", subjekt).operator(MatchQueryBuilder.Operator.OR);
		
		/* query string pre obsah */
		QueryBuilder obsah_qsq = QueryBuilders.queryStringQuery(obsah).field("obsah");
		
		/* filter pre max. pocet vypisov */
		@SuppressWarnings("deprecation")
		QueryBuilder filt_limit = QueryBuilders.limitQuery(10);
		
		/* filter pre rozsah datumu */
		QueryBuilder filt_datum = new Filter().vytvorDFilter(date_start, date_end);
		
		/* filter pre reakcie */
		QueryBuilder filt_reakcie = QueryBuilders.rangeQuery("reakcia");
		
		/* teraz do toho dame zopar podqueries */
		hlavna_query
		.must(filt_limit)
		;
		
		/* hladame autora ? */
		if (!autor.equals(""))
		{
			hlavna_query.must(match_autor);
		}
		
		/* hladame subjekt ? */
		if (!subjekt.equals(""))
		{
			hlavna_query.must(match_subjekt);
		}
		
		/* hladame obsah ? */
		if (!obsah.equals(""))
		{
			hlavna_query.must(obsah_qsq);
		}
		
		/* spracovanie hlad. reakcii */
		if(reakcia > 0)
		{
			((RangeQueryBuilder) filt_reakcie).lte(reakcia).gte(reakcia); // lte ^ gte == equals
		}
		else
		{
			((RangeQueryBuilder) filt_reakcie).lte(0);
		}
		
		/* pridanie hladanych reakcii */
		hlavna_query.must(filt_reakcie);
		
		/* pridanie filtrovania datumov */
		if (filt_datum != null)
		{
			hlavna_query.must(filt_datum);
		}
		
		/* odosleme ako SearchResponse */
		SearchResponse el_vysl_vyhl = el_klient.prepareSearch("projekt").setTypes("prispevky").
				setQuery(hlavna_query).setSize(limit_velkosti).
				execute().actionGet();
		
		/* prehladavame vysledky */
		for (SearchHit hit : el_vysl_vyhl.getHits())
		{
			System.out.print("ID: " + hit.getSource().get("id") + " |");
			System.out.print("Autor: " + hit.getSource().get("autor") + " |");
			System.out.print("Subjekt: " + hit.getSource().get("subjekt") + " |");
			System.out.print("Datum: " + hit.getSource().get("datum_vzniku") + " |");
			
			/* skratenie vypisu obsahu */
			String obsah_vyp = (String) hit.getSource().get("obsah").toString();
			Readers.vypisObsahuHitu(obsah_vyp);
		}
	}
	
	/* PRIDANIE DO ELASTICU */
	public static void pridajDoelasticu(String index, String typ, PrispevokLoaded prispevok, int cislo_reakcie) throws IOException
	{
		/* vytvorenie noveho datumu */
		String sformat_datum  = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		/* samotne pridanie do indexu */
		 IndexResponse iResponse = el_klient.prepareIndex(index, typ)
	                .setSource(jsonBuilder()
	                            .startObject()
	                            	.field("id", new String("" + prispevok.getPID()))
	                            	.field("autor", prispevok.getAutor())
	                                .field("subjekt", prispevok.getSubjekt())
	                                .field("obsah", prispevok.getObsah())
	                                .field("datum_vzniku", sformat_datum)
	                                .field("reakcia", cislo_reakcie)
	                            .endObject()
	                          )
	                .get();
	}
	
	/* ZMAZANIE Z ELASTICU */
	public static void zmazZelasticu(String index, String typ, int pid)
	{
		String interne_id = "";
		
		/* najprv ziskame interne IDCKO pre zmazanie */
		SearchResponse sResponse = el_klient.prepareSearch("projekt")							//z indexu artist
				.setTypes("prispevky")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.matchQuery("id", new Integer(pid).toString()))               	// Query    		
		        .execute()
		        .actionGet();
		
		/* tu si ho preulozime */
		/* prefiltrujeme vysledky vyhladavania */
        for (SearchHit hit : sResponse.getHits())
        {   
    		interne_id = hit.getId();
    	}
		
		/* potom ho podla neho zmazeme */
        /* toto ale maze podla interneho ID(_id), nie id ("id") v dokumente */
		DeleteResponse dResponse = el_klient.prepareDelete(index, typ, interne_id).get();
	}
	
	/* TEST HLADANIA - nepouziva sa */
	public static void hladajVelasticu(String autor, String[] obsah, int pocet_prisp)
	{
		BoolQueryBuilder hlavny_qb = QueryBuilders.boolQuery();
		hlavny_qb.
		must(QueryBuilders.matchPhrasePrefixQuery("obsah", obsah)
				.type(MatchQueryBuilder.Type.PHRASE_PREFIX));
		
		if (autor.equals("*"))
		{
			// chceme vs. autorov
		}
		else
		{
			hlavny_qb.must(QueryBuilders.matchQuery("autor", autor));
		}
		
		SearchResponse el_vysl_vyhl = el_klient.prepareSearch("projekt").
				setTypes("prispevky").
				setQuery(hlavny_qb).
				setSize(20).
				execute().actionGet();
		
		for (SearchHit hit : el_vysl_vyhl.getHits())
		{
			System.out.println(hit.getSource());
		}
		
	}
	
	/*
	 * MASOVE NAHADZANIE DO ELASTICU
	 */
	
	/* SYNCHRONIZOVANIE ELASTICU A POSTGRESU */
	@SuppressWarnings("deprecation")
	public static
	void massSync2() throws SQLException, IOException
	{
		List<PrispevokLoaded> vs_p = PrispevkyManager.ziskajvsPrispevky();
		
		for (PrispevokLoaded pl : vs_p)
		{
			/* hladame ci je take id v elasticu */
			QueryBuilder qb = QueryBuilders.termQuery("id", new Integer(pl.getPID()).toString());
			SearchResponse vyhl_prisp = el_klient.prepareSearch("projekt").
					setTypes("prispevky").
					setQuery(qb).
					setSize(1).
					setSearchType(SearchType.COUNT).
					execute().actionGet();
			
			/* ak je tak skaceme */
			if (vyhl_prisp.getHits().getTotalHits() == 0)
			{
				/* pridaj do elasticu */
				pridajDoelasticu("projekt", "prispevky", pl, pl.getReakcia());
			}
		}
		
	}
	
}
