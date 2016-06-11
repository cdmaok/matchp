package cn.edu.xmu.gxj.matchp.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class LtrBuilder {

	@Autowired
	private MatchpConfig config;
	
	private MongoClient client;

	private final String dbName = "matchp";
	private final String queryCollection = "querySet";
	private final String annoCollection = "labelSet";
	private MongoDatabase ltrDb;
	private BasicDBObject sample;
	
	//specific fields
	private static final String ANSWER = "answer";
	
	@PostConstruct
	public void init(){
		String url = config.getLtr_mongo_url();
		client = new MongoClient(new MongoClientURI(url));
		ltrDb =  client.getDatabase(dbName);
		sample = new BasicDBObject("$sample", new BasicDBObject("size", 1));
	}
	
	@PreDestroy
	public void destroy(){
		if (client != null) {
			client.close();
		}
	}	
	
	public  String[] randomQuery(){
		
		MongoCollection<Document> querySet = ltrDb.getCollection(queryCollection);
		AggregateIterable<Document> outputs = querySet.aggregate(Arrays.asList(sample));
		Document doc =  outputs.iterator().next();
		String text = doc.getString("text");
		String id = doc.getObjectId("_id").toString();
		return new String[]{text,id};
	}
	
	public void insetRecord(String jsonString){
		Document document = Document.parse(jsonString);
		String id = document.getString("id");
		document.remove("id");
		document.put("_id", id);
		MongoCollection<Document> annoSet = ltrDb.getCollection(annoCollection);
		annoSet.insertOne(document);
	}
	
	
	public  int AddAnnotation(String id,String json) throws MPException{
		//TODO: change this answer field and id field.
		try {
			Integer answer = Integer.parseInt(JsonUtility.getAttribute(json, ANSWER));
			if (answer != 1 && answer != -1) {
				throw new NumberFormatException();
			}
			MongoCollection<Document> annoSet = ltrDb.getCollection(annoCollection);
			Bson filter = new Document("_id", id);
			Bson update = new Document("$set", new Document(ANSWER, answer));
			annoSet.findOneAndUpdate(filter, update);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, "answer field should be 1 or -1");
		} catch (MPException e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, "no answer field.");
		}
		return 1;
	}
	
	public String[] dumpRecord(){
		MongoCollection<Document> annoSet = ltrDb.getCollection(annoCollection);
		FindIterable<Document> documents =  annoSet.find(new Document(ANSWER, new Document("$exists", true)));
		// no available method to get document's size.
		ArrayList<String> trainSet = new ArrayList<>();
		int index = 0;
		for (Document document : documents) {
			String pair = doc2pair(index,document);
			trainSet.add(pair);
//			System.out.println(document.toJson());
			index ++;
		}
		String[] pairs = new String[trainSet.size()];
		return trainSet.toArray(pairs);
	}
	
	
	public String doc2pair(int index, Document document){
		StringBuffer pair = new StringBuffer();
		int answer = document.getInteger(ANSWER);
		String first = "";
		String second = "";
		if (answer == 1) {
			// better
			first = "2 qid:" + index;
			second = "1 qid:" + index;
		}else if (answer == -1) {
			//worse
			first = "1 qid:" + index;
			second = "2 qid:" + index;
		}
		List<String> entrys = document.get("entrys", ArrayList.class);
		Document firstDoc = Document.parse(entrys.get(0));
		Document secondDoc = Document.parse(entrys.get(1));

		first = addFeature(first, firstDoc, 1, Fields.imgSize);
		first = addFeature(first, firstDoc, 2, "SentiScore");
		first = addFeature(first, firstDoc, 3, "IrScore");
		first = addFeature(first, firstDoc, 4, "TypeScore");
		first = addFeature(first, firstDoc, 5, "social");
		
		
		second = addFeature(second, secondDoc, 1, Fields.imgSize);
		second = addFeature(second, secondDoc, 2, "SentiScore");
		second = addFeature(second, secondDoc, 3, "IrScore");
		second = addFeature(second, secondDoc, 4, "TypeScore");
		second = addFeature(second, secondDoc, 5, "social");
		
		pair.append(first + "\n" + second);
		return pair.toString();
	}
	
	//TODO: to merge with entrybuilder social score
	public double getSocialScore(Document map){
		double socialScore = 0;
		double type = map.getDouble("TypeScore");
		if (type == 1) {
			socialScore = map.getInteger(Fields.loft_comments) + map.getInteger(Fields.loft_hot);
		}else if (type == 0.5) {
			socialScore = map.getInteger(Fields.weibo_comments) + map.getInteger(Fields.weibo_goods) + map.getInteger(Fields.weibo_repost);
		}else if (type == 2) {
			socialScore = map.getInteger(Fields.tumblr_hot);
		}else {
			socialScore = 0;
		}
		return socialScore;
	}
	
	public String addFeature(String str,Document document, int index ,String field){
		if (field.equals("social")) {
			return str + " " + index + ":" + getSocialScore(document);
		}else {
			return str + " " + index + ":" + document.getDouble(field);
		}
	}
	
}
