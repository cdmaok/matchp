package cn.edu.xmu.gxj.matchp.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/*
 * to help with json operation
 */
public class JsonUtility {

	public static String getString(String json, String key) throws MPException{
		ObjectNode node;
		try {
			node = new ObjectMapper().readValue(json, ObjectNode.class);
			return node.get(key).asText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MPException(ErrCode.Invalid_request, json + " " + e.getMessage());
		}
	}
}
