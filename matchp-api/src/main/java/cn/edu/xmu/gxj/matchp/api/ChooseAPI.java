package cn.edu.xmu.gxj.matchp.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("improve")
public interface ChooseAPI {

	@GET
	Response Choose();
	
	@POST
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	Response Prefer(@PathParam("id") String id,String json);
	
}
