package cn.edu.xmu.gxj.matchp.util;

import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * to help with json operation
 */
public class JsonUtility {

	public static String getAttribute(String json, String key) throws MPException{
		ObjectNode node;
		try {
			node = new ObjectMapper().readValue(json, ObjectNode.class);
			return node.get(key).asText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_request, json + " " + e.getMessage());
		}
	}
	
	public static ObjectNode newObjectNode(String key,String value){
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(key, value);
		ObjectNode dataNode = mapper.createObjectNode();
		return dataNode;
	}
	
	public static String newJsonString(String key,String value) throws MPException{
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(key, value);
		try {
			return mapper.writeValueAsString(hashMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_request, key + " " + value + " " + e.getMessage());
		}
	}
}
