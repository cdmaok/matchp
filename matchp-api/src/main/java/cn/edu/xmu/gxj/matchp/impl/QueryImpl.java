package cn.edu.xmu.gxj.matchp.impl;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.api.QueryAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;
import cn.edu.xmu.gxj.matchp.model.Reply;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

@Component
public class QueryImpl implements QueryAPI{

	@Autowired
	public IndexBuilder indexBuilder;
	
	
	private static final Logger logger = LoggerFactory.getLogger(QueryImpl.class);

	public Response QueryLongText(String query) {
		logger.info("query is {}",query);
		String ret;
		try {
			String queryText = JsonUtility.getAttribute(query, Fields.queryField);
			ret = indexBuilder.searchDoc(queryText);

			// cors
			return Response.ok().entity(ret).header("Access-Control-Allow-Origin", "*").build();
		} catch (MPException e) {
			e.printStackTrace();
			// cors
			Reply reply = new Reply(e);
			return Response.ok(new Gson().toJson(reply),MediaType.APPLICATION_JSON).header("Access-Control-Allow-Origin", "*").build();
		}

	}

}
