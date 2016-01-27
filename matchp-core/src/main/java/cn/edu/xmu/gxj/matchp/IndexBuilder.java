package cn.edu.xmu.gxj.matchp;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.node.NodeBuilder.*; 
import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.node.Node;

public class IndexBuilder {

	public static Client getClient(){
//		//Create Client
//		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "locales").build();
//		TransportClient transportClient = new TransportClient(settings);
//		transportClient = transportClient.addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
//		return (Client) transportClient;
		return getNode().client();
	}
	
	public static Node getNode(){
		ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder();
        Node node = nodeBuilder()
                .local(true)
                .settings(elasticsearchSettings.build())
                .node();
		
		return node;
	}
	
	public static void addDoc() throws IOException{
		String indexName = null;
		String documentType = null;
		//Add documents
		IndexRequestBuilder indexRequestBuilder = getClient().prepareIndex(indexName, documentType);
		//build json object
		XContentBuilder contentBuilder = jsonBuilder().startObject().prettyPrint();
		contentBuilder.field("name", "jai");
		contentBuilder.endObject();
		indexRequestBuilder.setSource(contentBuilder);
		IndexResponse response = indexRequestBuilder.execute().actionGet();
	}
	
	
	public static void readDoc(){
		//Get document
		String indexName = null;
		String type = null;
		GetRequestBuilder getRequestBuilder = getClient().prepareGet(indexName, type, null);
		getRequestBuilder.setFields(new String[]{"name"});
		GetResponse response = getRequestBuilder.execute().actionGet();
		String name = response.field("name").getValue().toString();
		System.out.println(name);
	}
	
	public static void main(String[] args) throws IOException {
		String indexName = "";
		CreateIndexRequestBuilder builder = getClient().admin().indices().prepareCreate(indexName);
		addDoc();
		
	}

}
