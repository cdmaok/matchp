package cn.edu.xmu.gxj.matchp.es;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.l2r.QueryRanker;
import cn.edu.xmu.gxj.matchp.model.Doc;
import cn.edu.xmu.gxj.matchp.model.DocFactory;
import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.plugins.Sar;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.score.EntryUtility;
import cn.edu.xmu.gxj.matchp.score.ScoreComparator;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class IndexBuilder {

	@Autowired
	private MatchpConfig matchpconfig;
	
	@Autowired
	private Sentiment sent;
	
	@Autowired
	private Sar sar;
	
	@Autowired
	private DocFactory docfactory;

	
	@Autowired
	private EntryUtility entryBuilder;

	@Autowired
	private QueryRanker queryRanker;

	private Settings settings;
	
	public IndexBuilder() {
		// to be noticed that autowired is after this method.
	}

	@PostConstruct
	public void init(){
		settings = Settings.settingsBuilder()
		        .put("cluster.name", matchpconfig.getEsClusterName()).build();
		
		createIndex();
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
	static String idField = "doc_id";

	static final Logger logger = LoggerFactory.getLogger(IndexBuilder.class);

	private void createIndex(){
		Client client = getClient();
		boolean exist = client.admin().indices().prepareExists(indexName).execute().actionGet().isExists();
		if (!exist) {
			CreateIndexRequestBuilder builder = client.admin().indices().prepareCreate(indexName).setSettings(getIndexSetting());
			builder.execute().actionGet();
		}else {
			logger.info("{} already exist, skip create index",indexName);
			client.close();
		}
	}
	
	/*
	 * generate the index settings
	 */
	private XContentBuilder getIndexSetting(){
		XContentBuilder settingbuilder = null;
		//TODO: fix the document type here.
		try {
			settingbuilder = XContentFactory.jsonBuilder()
					.startObject()
					.field("index", indexName)
//					.field("type","weibo")
//						.startObject("body")
//							.startObject("weibo")
//								.startObject("properties")
//									.startObject("text")
//									.field("type","string")
//									.field("analyzer", "ik_syno_smart")
//									.field("search_analyzer", "ik_syno_smart")
//									.endObject()
//								.endObject()
//							.endObject()
//						.endObject()
						.field("type","loft")
						.startObject("body")
							.startObject("loft")
								.startObject("properties")
									.startObject("text")
									.field("type","string")
									.field("analyzer", "ik_syno_smart")
									.field("search_analyzer", "ik_syno_smart")
									.endObject()
								.endObject()
							.endObject()
						.endObject()
					.endObject();
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("create index setting failure, reason:{}",e.getMessage());
		}
		
		return settingbuilder;
				
	}
	

	public void addDoc(String json) throws MPException{

		Client client = getClient();

		String type = JsonUtility.getAttributeasStr(json, Fields.type);
		try {
			Doc document = docfactory.Build(json);
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, type , document.getDoc_id());
			indexRequestBuilder.setSource(document.getContent());
			indexRequestBuilder.execute().actionGet();
			//TODO: check response here
		} catch (MPException e) {
			e.printStackTrace();
			throw e;
		} finally {
			client.close();
		}
		
	}





	public void readDoc() {
		// Get document according to id
		Client client = getClient();
		GetRequestBuilder getRequestBuilder = client.prepareGet(indexName, null, null);
		// getRequestBuilder.setFields(new String[]{"name"});
		GetResponse response = getRequestBuilder.execute().actionGet();
		String name = response.getField("name").getValue().toString();
		System.out.println(name);
		client.close();
	}

	public String searchDoc(String query) throws MPException {
		ArrayList<String> resultList = searchDoc(query, 60);
		ArrayList<Entry> entryList = EntryUtility.buildEntryArray(resultList);
		return new Gson().toJson(entryList);
	}
	
	
	public ArrayList<String> searchDoc(String query,int size) throws MPException {
		Client client = getClient();
		
		long queryStart = System.currentTimeMillis();
		// we change search type to 'query then fetch' from 'query and fetch',otherwise it will ignore the limit size.
		SearchResponse response = client.prepareSearch(indexName).setSearchType(SearchType.QUERY_THEN_FETCH)
		.setQuery(QueryBuilders.matchQuery("text", query).analyzer("ik_syno")).setFrom(0).setSize(size).setExplain(true).execute().actionGet();


		double querySenti = sent.getSentiment(query);
		int sarLabel = sar.getSar(query);
		long mergeStart = System.currentTimeMillis();
		SearchHit[] results = response.getHits().getHits();
		ArrayList<String> resultList = new ArrayList<String>();
		HashSet<String> signs = new HashSet<>();
		
		ArrayList<String> vectors = new ArrayList<>();
		
		for (SearchHit hit : results) {
			Map<String, Object> result = hit.getSource();
//			Entry entry = entryBuilder.buildEntry(querySenti, hit);
			String entryString = entryBuilder.buildJson(querySenti,sarLabel, hit);
			//TODO: this de-duplication is ugly.
			
			String sign = MapUtils.getString(result, Fields.imgSign, "");
			
			if (!signs.contains(sign)) {
				resultList.add(entryString);
				signs.add(sign);
				String vector = MapUtils.getString(result, "feature","");
				vectors.add(vector);
			}else{
				logger.debug("find the duplicated one. sign is {}, url is {}", sign, JsonUtility.getAttributeasStr(entryString, Fields.img));
			}
			logger.debug(result + "," + hit.getScore());

		}
		
		if (resultList.size() == 0) {
			return resultList;
		}
		long mergeEnd = System.currentTimeMillis();
		
		long l2rStart = System.currentTimeMillis();
		
		double[] pros = queryRanker.getPro(vectors);
		
		long l2rmid = System.currentTimeMillis();
		
		for (int i = 0; i < pros.length; i++) {
//			resultList.get(i).setScore((float) pros[i]);
			resultList.set(i, JsonUtility.setAttribute(resultList.get(i), Fields.score, pros[i]));
		}
		
		long l2rEnd = System.currentTimeMillis();
		
		long sortStart = System.currentTimeMillis();
		Collections.sort(resultList, new ScoreComparator());
		long sortEnd = System.currentTimeMillis();
		long queryEnd = System.currentTimeMillis();
		logger.info("query:{}, using time: query = {}ms, merge = {}ms,rank = {} / {}ms, sort = {}ms, total = {}ms, result:{}",
				query, queryEnd - queryStart, mergeEnd - mergeStart, l2rmid - l2rStart ,l2rEnd - l2rStart ,sortEnd - sortStart, queryEnd - queryStart, resultList.size());
		client.close();
		return resultList;
	}
	
	
	public ArrayList<String> randomDoc(String query) throws MPException {
		ArrayList<String> resultList = searchDoc(query, 10);
		ArrayList<String> entrys = new ArrayList<>();
		int size = resultList.size();
		Random random = new Random();
		if(size < 2){
			return new ArrayList<>();
		}else{
			int firstId = random.nextInt(size);
			int secondId = firstId;
			while(secondId == firstId){
				secondId = random.nextInt(size);
			}
			entrys.add(resultList.get(firstId));
			entrys.add(resultList.get(secondId));
			return entrys;
		}
	}

	/*
	 * useless, only for test case.
	 */
	public EntryUtility getEntryBuilder() {
		return entryBuilder;
	}

	public void setEntryBuilder(EntryUtility entryBuilder) {
		this.entryBuilder = entryBuilder;
	}

	public DocFactory getDocfactory() {
		return docfactory;
	}

	public void setDocfactory(DocFactory docfactory) {
		this.docfactory = docfactory;
	}

	
	
}
