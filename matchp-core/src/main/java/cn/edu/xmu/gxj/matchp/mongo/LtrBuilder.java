package cn.edu.xmu.gxj.matchp.mongo;

import java.util.Arrays;

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
	
	@PostConstruct
	public void init(){
		String url = config.getLtr_mongo_url();
		client = new MongoClient(new MongoClientURI("mongodb://matchp:Matchp123456@114.215.99.92:27017/matchp"));
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
			Integer answer = Integer.parseInt(JsonUtility.getAttribute(json, "answer"));
			if (answer != 1 && answer != -1) {
				throw new NumberFormatException();
			}
			MongoCollection<Document> annoSet = ltrDb.getCollection(annoCollection);
			Bson filter = new Document("_id", id);
			Bson update = new Document("$set", new Document("answer", answer));
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
	
	public void dumpRecord(){
		MongoCollection<Document> annoSet = ltrDb.getCollection(annoCollection);
		FindIterable<Document> documents =  annoSet.find(new Document("answer", new Document("$exists", true)));
		
		for (Document document : documents) {
			System.out.println(document.toJson());
		}
	}
	
}
