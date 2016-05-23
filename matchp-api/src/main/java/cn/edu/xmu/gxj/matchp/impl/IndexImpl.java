package cn.edu.xmu.gxj.matchp.impl;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.api.IndexAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;
import cn.edu.xmu.gxj.matchp.model.Reply;
import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.MPException;

@Component
public class IndexImpl implements IndexAPI {

	@Autowired
	private IndexBuilder indexBuilder;
	
	public Response AddIndex(String type,String text) {
		if(type == null){
			Reply notype = new Reply("you need to specify the doc type, loft or weibo", ErrCode.Invalid_Request);
			return Response.ok(new Gson().toJson(notype),MediaType.APPLICATION_JSON).build();
		}
		try {
			indexBuilder.addDoc(type,text);
		}  catch (MPException e1) {
			e1.printStackTrace();
			Reply reply = new Reply(e1.getMessage(),e1.getCode());
			return Response.ok(new Gson().toJson(reply), MediaType.APPLICATION_JSON).build();
		}
		Reply reply = new Reply("ok", ErrCode.Index_Success);
		return Response.ok(new Gson().toJson(reply)).build();
	}

}
