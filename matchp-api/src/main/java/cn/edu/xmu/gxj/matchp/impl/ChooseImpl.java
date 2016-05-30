package cn.edu.xmu.gxj.matchp.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.api.ChooseAPI;
import cn.edu.xmu.gxj.matchp.es.IndexBuilder;
import cn.edu.xmu.gxj.matchp.model.EntryPair;
import cn.edu.xmu.gxj.matchp.model.Reply;
import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class ChooseImpl implements ChooseAPI{
	
	@Autowired
	private MatchpConfig config;
	
	@Autowired
	private IndexBuilder builder;
	
	private int status = 0;
	
	private List<String> contents;
	
	private BufferedWriter writer;

	@PostConstruct
	public void init() throws IOException{
		String filepath = config.getQuerySet();
		File file = new File(filepath);
		if (!file.exists()) {
			status = -1;
		}
		contents = FileUtils.readLines(file);
		String labelFilepath = config.getLabelFile();
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(labelFilepath),true), "utf-8"));
	}
	
	@PreDestroy
	public void destroy() throws IOException{
		if (writer != null) {
			writer.close();
		}
	}
	
	@Override
	public Response Choose() {
		if (status == -1) {
			Reply reply = new Reply("No query set file,cannot provide query and open this function", ErrCode.No_Such_File);
			return Response.ok(new Gson().toJson(reply), MediaType.APPLICATION_JSON).build();
		}
		Random random = new Random();
		ArrayList<String> result = null;
		String query = null;
		do {
			int index = random.nextInt(contents.size());
			query = contents.get(index);
			result = builder.randomDoc(query);
		} while (result.size() != 2);
		
		EntryPair pair = new EntryPair(query,result);
		
		return Response.ok(new Gson().toJson(pair), MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response Prefer(String json) {
		EntryPair pair = new Gson().fromJson(json, EntryPair.class);
		Reply reply;
		Integer answer = pair.getAnswer();
		if ( answer == null) {
			reply = new Reply("you submit no choose", ErrCode.Invalid_Request);
		}else if (answer == 1 || answer == -1) {
			reply = new Reply("thanks for your improvement", ErrCode.Index_Success);
			try {
				writer.write(json + "\n");
				writer.flush();
			} catch (IOException e) {
				reply = new Reply(e.getMessage(), ErrCode.System_Failed);
			}
		}else {
			reply = new Reply("you submit invalid choose", ErrCode.Invalid_Request);
		}
		return Response.ok(new Gson().toJson(reply), MediaType.APPLICATION_JSON).build();
	}

}
