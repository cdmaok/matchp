package cn.edu.xmu.gxj.matchp.plugins;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Reply;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;
import groovy.lang.MetaClassImpl.Index;

@Component
public class Sar {

	@Autowired
	private MatchpConfig config;
	
	private String api = "sar/";
	private String host;
	private String url_str;
	
	private HashMap<String, Integer> labels = new HashMap<>();
	
	
	@PostConstruct
	public void init(){
		host = config.getSar_server();
		url_str = "http://" + host;
		if(!host.endsWith("/")){
			url_str += "/";
		}
		url_str += api;
		int index = 0;
		labels.put("s", index ++);
		labels.put("c", index ++);
		labels.put("o", index ++);
		labels.put("l", index ++);
		labels.put("t", index ++);
		labels.put("q", index ++);
		labels.put("m", index ++);
		labels.put("p", index ++);
	}
	
	public int getSar(String text){
		if(!config.isSar_enable()){
			return 0;
		}
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url_str);
		httpPost.addHeader("content-type", "application/json");
		CloseableHttpResponse response = null;
		try {
			String jsonText = JsonUtility.newJsonString("text", text);
			StringEntity params = new StringEntity(jsonText, "utf-8");
			httpPost.setEntity(params);
			response = httpclient.execute(httpPost);
			String res = EntityUtils.toString(response.getEntity());
			Gson gson = new Gson();
			Reply sentScore = gson.fromJson(res, Reply.class);
			//{"Message": 0.5, "Code": "200"}
			response.close();
			httpclient.close();
			int label = 0;
			if(sentScore.getCode() == 200){
				label = labels.get(sentScore.getMessage());
			}else{
				label = 0;
			}
			return label;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.close();
				httpclient.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return 0;
		}

	}
	
}
