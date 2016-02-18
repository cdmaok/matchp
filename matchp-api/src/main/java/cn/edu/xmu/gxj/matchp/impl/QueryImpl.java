package cn.edu.xmu.gxj.matchp.impl;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.api.QueryAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;

@Component
public class QueryImpl implements QueryAPI{

	@Autowired
	public IndexBuilder indexBuilder;
	
	public Response GetString() {
		indexBuilder.searchDoc();
		return Response.ok().entity("hello").build();
	}

}
