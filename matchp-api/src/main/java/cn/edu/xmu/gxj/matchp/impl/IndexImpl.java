package cn.edu.xmu.gxj.matchp.impl;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.api.IndexAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;

@Component
public class IndexImpl implements IndexAPI {

	@Autowired
	private IndexBuilder indexBuilder;
	
	public Response AddIndex(String text) {
		try {
			indexBuilder.addDoc(text);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.ok(e.getMessage()).build();
		}
		return Response.ok().build();
	}

}
