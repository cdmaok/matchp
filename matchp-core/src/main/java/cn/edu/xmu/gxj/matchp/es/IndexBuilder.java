package cn.edu.xmu.gxj.matchp.es;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
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
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.model.Weibo;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class IndexBuilder {
	
	@Autowired
	private MatchpConfig matchpconfig;
	
	public IndexBuilder() {
		// to be notice that autowired is after this method.
	}
	
	@PostConstruct
	public void init(){
//		try {
//			addDoc();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
	}
	
	static String indexName = "matchp";
	static String documentType = "weibo";
	
	static final Logger logger = LoggerFactory.getLogger(IndexBuilder.class);
	
	public static String str = "{\"comment_no\": \"0\", "
			+ "\"text\": \"今天起来得早吧～我都出去一趟回来老。早起的鸟儿有虫吃[鼓掌][鼓掌]懒猪们 ，早安 [呲牙][太阳]http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\"}";

	public Client geteasyClient() {
		// //Create Client
		// Settings settings =
		// ImmutableSettings.settingsBuilder().put("cluster.name",
		// "locales").build();
		// TransportClient transportClient = new TransportClient(settings);
		// transportClient = transportClient.addTransportAddress(new
		// InetSocketTransportAddress("localhost", 9300));
		// return (Client) transportClient;
		return getNode().client();
	}

	public Node getNode() {
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder().put("cluster.name",
				matchpconfig.getEsClusterName()).put("path.home",matchpconfig.getEsPath());
		Node node = nodeBuilder().local(true).settings(elasticsearchSettings.build()).node();

		return node;
	}

	public void addDoc() throws IOException, CloneNotSupportedException {

		// Add documents
		Client c = geteasyClient();
		IndexRequestBuilder indexRequestBuilder = c.prepareIndex(indexName, documentType);
		// build json object
		// XContentBuilder contentBuilder =
		// jsonBuilder().startObject().prettyPrint();
		// contentBuilder.field("name", "jai");
		// contentBuilder.endObject();
		// indexRequestBuilder.setSource(contentBuilder);

		Gson gson = new Gson();
		Weibo weibo = gson.fromJson(str, Weibo.class);
		Weibo newWeibo = Weibo.build(weibo);

		indexRequestBuilder.setSource(newWeibo.toMap());
		IndexResponse response = indexRequestBuilder.execute().actionGet();
		c.close();
	}

	
	public void addDoc(String json) throws IOException, CloneNotSupportedException {

		// Add documents
		Client c = geteasyClient();
		IndexRequestBuilder indexRequestBuilder = c.prepareIndex(indexName, documentType);
		// build json object
		// XContentBuilder contentBuilder =
		// jsonBuilder().startObject().prettyPrint();
		// contentBuilder.field("name", "jai");
		// contentBuilder.endObject();
		// indexRequestBuilder.setSource(contentBuilder);

		Gson gson = new Gson();
		Weibo weibo = gson.fromJson(json, Weibo.class);
		Weibo newWeibo = Weibo.build(weibo);

		indexRequestBuilder.setSource(newWeibo.toMap());
		IndexResponse response = indexRequestBuilder.execute().actionGet();
		c.close();
	}
	
	
	
	public void readDoc() {
		// Get document according to id

		GetRequestBuilder getRequestBuilder = geteasyClient().prepareGet(indexName, documentType, null);
		// getRequestBuilder.setFields(new String[]{"name"});
		GetResponse response = getRequestBuilder.execute().actionGet();
		String name = response.getField("name").getValue().toString();
		System.out.println(name);
	}

	public String searchDoc(String query) {
		Client c = geteasyClient();
		SearchResponse response = c.prepareSearch(indexName).setTypes(documentType).setSearchType(SearchType.QUERY_AND_FETCH)
//				.setQuery(QueryBuilders.fieldQuery("like_no", "0")).setFrom(0).setSize(60).setExplain(true).execute().actionGet();
		.setQuery(QueryBuilders.matchQuery("text", query)).setFrom(0).setSize(60).setExplain(true).execute().actionGet();

		SearchHit[] results = response.getHits().getHits();
		logger.info("Current results: {}" + results.length );
		ArrayList<Entry> resultList = new ArrayList<Entry>();
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			Weibo weibo = new Weibo(result);
			Entry entry = new Entry(weibo);
			resultList.add(entry);
			logger.debug(result + "," + hit.getScore());

		}
		c.close();
		return new Gson().toJson(resultList);
	}

	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		IndexBuilder index = new IndexBuilder();
//		index.addDoc();
//		readDoc();
		index.searchDoc("今天");
		// check the test case.
	}

}
