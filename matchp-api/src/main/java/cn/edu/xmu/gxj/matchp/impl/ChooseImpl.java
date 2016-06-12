package cn.edu.xmu.gxj.matchp.impl;

import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.api.ChooseAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;
import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.model.EntryPair;
import cn.edu.xmu.gxj.matchp.model.Reply;
import cn.edu.xmu.gxj.matchp.mongo.LtrBuilder;
import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class ChooseImpl implements ChooseAPI{
	
	@Autowired
	private MatchpConfig config;
	
	@Autowired
	private IndexBuilder indexBuilder;
	

	@Autowired
	private LtrBuilder ltrBuilder; 
		
	@Override
	public Response Choose() {
		try {
			String query = null;
			String id = null;
			ArrayList<String> entrys = new ArrayList<>();
			while(entrys.size() != 2){
				String[] queryPair = ltrBuilder.randomQuery();
				query = queryPair[0];
				id = queryPair[1];
				entrys = indexBuilder.randomDoc(query);
			}
			
			EntryPair pair = new EntryPair(query, id, entrys);
			EntryPair simplePair = pair.simpleFormat();
			String jsonString = new Gson().toJson(pair);
			ltrBuilder.insetRecord(jsonString);
			return Response.ok(new Gson().toJson(simplePair), MediaType.APPLICATION_JSON).build();
		} catch (MPException e) {
			e.printStackTrace();
			Reply reply = new Reply(e);
			return Response.ok(new Gson().toJson(reply),MediaType.APPLICATION_JSON).build();
		}
	}

	@Override
	public Response Prefer(String json) {
		try {
			String id = JsonUtility.getAttributeasStr(json, "id");
			ltrBuilder.AddAnnotation(id, json);
			Reply reply = new Reply("Thanks for your help", ErrCode.Index_Success);
			return Response.ok(new Gson().toJson(reply),MediaType.APPLICATION_JSON).build();
		} catch (MPException e) {
			e.printStackTrace();
			Reply reply = new Reply(e);
			return Response.ok(new Gson().toJson(reply),MediaType.APPLICATION_JSON).build();
		}
	}

}
