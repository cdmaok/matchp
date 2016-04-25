package cn.edu.xmu.gxj.matchp.es;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.model.Weibo;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class IndexBuilder {

	@Autowired
	private MatchpConfig matchpconfig;
	
	@Autowired
	private Sentiment sent;


	private Settings settings;
	
	public IndexBuilder() {
		// to be noticed that autowired is after this method.
	}

	@PostConstruct
	public void init(){
		settings = Settings.settingsBuilder()
		        .put("cluster.name", matchpconfig.getEsClusterName()).build();
	}

	private Client getClient(){
		Client client;
		try {
			client = TransportClient.builder().settings(settings).build().
					addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(matchpconfig.getEsHostName()), 9300));
			return client;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}
	
	
	static String indexName = "matchp";
	static String documentType = "weibo";
	static String idField = "mid";

	static final Logger logger = LoggerFactory.getLogger(IndexBuilder.class);


	public void addDoc(String json) throws IOException, CloneNotSupportedException, MPException {

		Client client = getClient();
		// build json object
		// XContentBuilder contentBuilder =
		// jsonBuilder().startObject().prettyPrint();
		// contentBuilder.field("name", "jai");
		// contentBuilder.endObject();
		// indexRequestBuilder.setSource(contentBuilder);

		Gson gson = new Gson();
		Weibo weibo = gson.fromJson(json, Weibo.class);
		Weibo newWeibo = Weibo.build(weibo);
		newWeibo.setPolarity(sent.getSentiment(newWeibo.getText()));
		
		if(matchpconfig.isCheck_img() && newWeibo.isNotFound()){
			client.close();
			throw new MPException(ErrCode.Image_Not_Found, "Image Not found. url:" + newWeibo.getImg_url());
		}
		
		if(matchpconfig.isCheck_chatter() && newWeibo.isChatter()){
			client.close();
			throw new MPException(ErrCode.Text_Chatter, "text is chatter. text:" + newWeibo.getText());
		}
		
		IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType,newWeibo.getMid());
		indexRequestBuilder.setSource(newWeibo.toMap());
		IndexResponse response = indexRequestBuilder.execute().actionGet();
		
		client.close();
	}





	public void readDoc() {
		// Get document according to id
		Client client = getClient();
		GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, documentType, null);
		// getRequestBuilder.setFields(new String[]{"name"});
		GetResponse response = getRequestBuilder.execute().actionGet();
		String name = response.getField("name").getValue().toString();
		System.out.println(name);
		client.close();
	}

	public String searchDoc(String query) {
		Client client = getClient();
		SearchResponse response = client.prepareSearch(indexName).setTypes(documentType).setSearchType(SearchType.QUERY_AND_FETCH)
//				.setQuery(QueryBuilders.fieldQuery("like_no", "0")).setFrom(0).setSize(60).setExplain(true).execute().actionGet();
		.setQuery(QueryBuilders.matchQuery("text", query)).setFrom(0).setSize(60).setExplain(true).execute().actionGet();

		float queryPolarity = sent.getSentiment(query);
		
		SearchHit[] results = response.getHits().getHits();
		logger.info("Current results: {}" , results.length );
		ArrayList<Entry> resultList = new ArrayList<Entry>();
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			
			Weibo weibo = new Weibo(result);
			Entry entry = new Entry(weibo,hit.getScore());
			resultList.add(entry);
			logger.debug(result + "," + hit.getScore());

		}
		client.close();
		return new Gson().toJson(resultList);
	}

}
