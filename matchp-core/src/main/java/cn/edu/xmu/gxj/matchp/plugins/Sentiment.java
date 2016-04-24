package cn.edu.xmu.gxj.matchp.plugins;

import javax.annotation.PostConstruct;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class Sentiment {

	@Autowired
	private MatchpConfig config;
	
	private String api = "snow/?text=";
	private String host;
	private String url_str;
	
	@PostConstruct
	public void init(){
		host = config.getSentiment_server();
		url_str = "http://" + host;
		if(!host.endsWith("/")){
			url_str += "/";
		}
		url_str += api;
	}
	
	public float getSentiment(String text){
		if(!config.isSentiment_enable()){
			return 0;
		}
		String final_url = url_str + text;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(final_url);
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			return 0.5f;
		}
		return 0f;
	}
	
}
