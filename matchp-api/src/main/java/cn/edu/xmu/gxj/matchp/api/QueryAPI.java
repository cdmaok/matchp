package cn.edu.xmu.gxj.matchp.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import cn.edu.xmu.gxj.matchp.constants.Constants;

@Path("")
public interface QueryAPI {

	@GET
	Response QueryShortText(@QueryParam("q")String queryText);
	
	//TODO: to add long text interface
	@POST
	Response QueryLongText();
}
