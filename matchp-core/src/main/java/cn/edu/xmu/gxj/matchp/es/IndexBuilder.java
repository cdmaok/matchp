package cn.edu.xmu.gxj.matchp.es;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
import java.util.Map;

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

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Weibo;

public class IndexBuilder {

	static String indexName = "matchp";
	static String documentType = "weibo";
	
	public static String str = "{\"comment_no\": \"0\", "
			+ "\"text\": \"今天起来得早吧～我都出去一趟回来老。早起的鸟儿有虫吃[鼓掌][鼓掌]懒猪们 ，早安 [呲牙][太阳]http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\"}";

	public static Client geteasyClient() {
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

	public static Node getNode() {
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder().put("cluster.name",
				"locales").put("path.home","E:\\workspace\\matchp\\matchp-core");
		Node node = nodeBuilder().local(true).settings(elasticsearchSettings.build()).node();

		return node;
	}

	public static void addDoc() throws IOException, CloneNotSupportedException {

		// Add documents
		IndexRequestBuilder indexRequestBuilder = geteasyClient().prepareIndex(indexName, documentType);
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
	}

	public static void readDoc() {
		// Get document according to id

		GetRequestBuilder getRequestBuilder = geteasyClient().prepareGet(indexName, documentType, null);
		// getRequestBuilder.setFields(new String[]{"name"});
		GetResponse response = getRequestBuilder.execute().actionGet();
		String name = response.getField("name").getValue().toString();
		System.out.println(name);
	}

	public static void searchDoc() {
		
		SearchResponse response = geteasyClient().prepareSearch(indexName).setTypes(documentType).setSearchType(SearchType.QUERY_AND_FETCH)
//				.setQuery(QueryBuilders.fieldQuery("like_no", "0")).setFrom(0).setSize(60).setExplain(true).execute().actionGet();
		.setQuery(QueryBuilders.matchQuery("text", "今天")).setFrom(0).setSize(60).setExplain(true).execute().actionGet();

		SearchHit[] results = response.getHits().getHits();

		System.out.println("Current results: " + results.length);
		for (SearchHit hit : results) {
			System.out.println("------------------------------");
			Map<String, Object> result = hit.getSource();
			System.out.println(result + "," + hit.getScore());
		}
	}

	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		String indexName = "";
		// CreateIndexRequestBuilder builder =
		// geteasyClient().admin().indices().prepareCreate(indexName);
		addDoc();
//		readDoc();
		searchDoc();

	}

}
