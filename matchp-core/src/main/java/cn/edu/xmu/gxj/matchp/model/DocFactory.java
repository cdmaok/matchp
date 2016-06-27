package cn.edu.xmu.gxj.matchp.model;

import java.util.Map;

import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

@Component
public class DocFactory {
	
	public Doc Build(String json) throws MPException {
		Map<String, Object> map = JsonUtility.json2Map(json);
		return Build(map);
	}

	public Doc Build(Map<String, Object> map) throws MPException {
		checkMap(map);
		return new Doc(map);
	}

	/*
	 * check the input has the necessary field or not
	 */
	private  boolean checkMap(Map<String, Object> map) throws MPException {
		String[] checkList = new String[] { Fields.text, Fields.img, Fields.doc_id ,
				Fields.imgSize,Fields.imgSign,Fields.type,Fields.SOSCORE_FIELD,Fields.SAR_FIELD,Fields.OCR_FIELD,Fields.HIST_FIELD};
		for (int i = 0; i < checkList.length; i++) {
			String field = checkList[i];
			if (!map.containsKey(field)) {
				throw new MPException(ErrCode.Empty_Field, "no " + field + " founded ");
			}
		}
		return true;
	}
	
}
