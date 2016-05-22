package cn.edu.xmu.gxj.matchp.model;

import java.util.HashMap;
import java.util.Map;

import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

public class Doc {
	
	private HashMap<String, Object> content;

	public Doc(String json) throws MPException{
		this(JsonUtility.json2Map(json));
	}
	
	
	public Doc(Map<String, Object> map) throws MPException{
		if(checkMap(map)){
			this.content = (HashMap<String, Object>) map;
		}
		
	}
	
	/*
	 * check the input has the necessary field or not
	 */
	private static boolean checkMap(Map<String, Object> map) throws MPException{
		String[] checkList = new String[]{Fields.text,Fields.img,Fields.doc_id};
		for (int i = 0; i < checkList.length; i++) {
			String field = checkList[i];
			if (!map.containsKey(field)) {
				throw new MPException(ErrCode.Empty_Field, "no " + field + " founded ");
			} 
		}
		return true;
	}
	
}
