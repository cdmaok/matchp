package cn.edu.xmu.gxj.matchp.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("improve")
public interface ChooseAPI {

	@GET
	Response Choose();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	Response Prefer(String json);
	
}
