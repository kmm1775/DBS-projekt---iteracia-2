package nastroje;

import java.util.Date;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

public class Filter {
	public QueryBuilder vytvorDFilter(String date_start, String date_end)
	{
		QueryBuilder date_query = QueryBuilders.rangeQuery("datum_vzniku");
		// .format("yyyy-MM-dd")
		
		if (date_start.equals(""))
		{
			return null;
		}
		else
		{
			((RangeQueryBuilder) date_query).from(date_start).format("yyyy-mm-dd").includeLower(true);
			
			if (!date_end.equals(""))
			{
				((RangeQueryBuilder) date_query).to(date_end).format("yyyy-mm-dd").includeUpper(true);
			}
		}
		
		return date_query;
	}
}
