package cn.edu.xmu.gxj.matchp.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import cn.edu.xmu.gxj.matchp.constants.Constants;

@Path(Constants.query)
public interface QueryAPI {

	@GET
	Response GetString();
}
