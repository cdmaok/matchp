package cn.edu.xmu.gxj.matchp.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * to help with json operation
 */
public class JsonUtility {
	
	static ObjectMapper mapper = new ObjectMapper();
	
	/*
	 * return if the json string has the key attribute
	 */
	public static boolean hasAttribute(String json,String key) throws MPException{
		ObjectNode node;
		try {
			node = mapper.readValue(json, ObjectNode.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, json + " " + e.getMessage());
		}
		return node.has(key);
	}
	
	/*
	 * get attribute from json string
	 */
	public static String getAttribute(String json, String key) throws MPException{
		ObjectNode node;
		try {
			node = mapper.readValue(json, ObjectNode.class);
			if(node.has(key)){
				return node.get(key).asText();
			}else{
				throw new MPException(ErrCode.Invalid_Request, "not found this field " + key + " in " + json);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, json + " " + e.getMessage());
		}
	}
	
	public static ObjectNode newObjectNode(String key,String value){
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(key, value);
		ObjectNode dataNode = mapper.createObjectNode();
		return dataNode;
	}
	
	/*
	 * make (key,value) into a json string: {key:value}
	 */
	public static String newJsonString(String key,String value) throws MPException{
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(key, value);
		try {
			return mapper.writeValueAsString(hashMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, key + " " + value + " " + e.getMessage());
		}
	}
	
	/*
	 * TODO: same with upper function
	 * make (key,value) into a json string: {key:value}
	 */
	public static String newJsonString(String key1,String value1,String key2,String value2) throws MPException{
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(key1, value1);
		hashMap.put(key2, value2);
		try {
			return mapper.writeValueAsString(hashMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, key1 + " " + value1 + " " + e.getMessage());
		}
	}
	
	/*
	 * convert a json string into a hashmap
	 */
	public static Map<String, Object> json2Map(String json) throws MPException{
		Map<String, Object> hashmap = new HashMap<String, Object>();
		// convert JSON string to Map
		try {
			hashmap = new ObjectMapper().readValue(json, new TypeReference<HashMap<String,Object>>() {});
		} catch (Exception e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_Request, json + " " + e.getMessage());
		}
		return hashmap;
	}
	
}
