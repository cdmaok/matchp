package cn.edu.xmu.gxj.matchp.impl;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.api.QueryAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;

@Component
public class QueryImpl implements QueryAPI{

	@Autowired
	public IndexBuilder indexBuilder;
	
	
	private static final Logger logger = LoggerFactory.getLogger(QueryImpl.class);

	public Response QueryLongText() {
		return null;
	}

	public Response QueryShortText(String queryText) {
		logger.info("query is {}",queryText);
		String ret = indexBuilder.searchDoc(queryText);
		// cors
		return Response.ok().entity(ret).header("Access-Control-Allow-Origin", "*").build();
	}

}
