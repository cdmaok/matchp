package cn.edu.xmu.gxj.matchp.es;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthAction;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthStatus;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
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
	
	private Node node;
	
	public IndexBuilder() {
		// to be noticed that autowired is after this method.
	}
	
	@PostConstruct
	public void init(){
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder().put("cluster.name",
				matchpconfig.getEsClusterName()).put("path.home",matchpconfig.getEsPath());
		node = nodeBuilder().local(true).settings(elasticsearchSettings.build()).node();
		try {
			Reindex();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	static String indexName = "matchp";
	static String documentType = "weibo";
	static String idField = "mid";
	
	static final Logger logger = LoggerFactory.getLogger(IndexBuilder.class);
	

	private Client geteasyClient() {
		// //Create Client
		// Settings settings =
		// ImmutableSettings.settingsBuilder().put("cluster.name",
		// "locales").build();
		// TransportClient transportClient = new TransportClient(settings);
		// transportClient = transportClient.addTransportAddress(new
		// InetSocketTransportAddress("localhost", 9300));
		// return (Client) transportClient;
		Client client = node.client();
		TimeValue timeout = new TimeValue(1000);
        ClusterHealthResponse healthResponse =
                client.execute(ClusterHealthAction.INSTANCE, new ClusterHealthRequest().waitForStatus(ClusterHealthStatus.GREEN).timeout(timeout)).actionGet();
		System.out.println(healthResponse.getStatus().name());
        return client;
	}


	private void Reindex() throws IOException, CloneNotSupportedException{
		File dir = new File(matchpconfig.getEsPath());
		if (dir.exists() && dir.isDirectory()) {
			return;
		}else {
			logger.info("reconstruct index ...");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(matchpconfig.getEsInput())), "utf-8"));
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				addDoc(line);
			}
			bufferedReader.close();
		}
	}
	
	public void addDoc(String json) throws IOException, CloneNotSupportedException {

		// Add documents
		Client c = geteasyClient();

		// build json object
		// XContentBuilder contentBuilder =
		// jsonBuilder().startObject().prettyPrint();
		// contentBuilder.field("name", "jai");
		// contentBuilder.endObject();
		// indexRequestBuilder.setSource(contentBuilder);

		Gson gson = new Gson();
		Weibo weibo = gson.fromJson(json, Weibo.class);
		Weibo newWeibo = Weibo.build(weibo);
		IndexRequestBuilder indexRequestBuilder = c.prepareIndex(indexName, documentType,newWeibo.getMid());
		indexRequestBuilder.setSource(newWeibo.toMap());
		IndexResponse response = indexRequestBuilder.execute().actionGet();
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(matchpconfig.getEsBackup()),true), "utf-8"));
		bufferedWriter.write(json + "\r\n");
		bufferedWriter.close();
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
		logger.info("Current results: {}" , results.length );
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
		// check the test case.
	}

}
