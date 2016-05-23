package cn.edu.xmu.gxj.matchp.model;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.percolate.PercolateSourceBuilder.DocBuilder;

import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

public class Doc {
	
	
	private HashMap<String, Object> content; // include all the field
	private String text;
	private String img;
	private String doc_id;

	public String getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(String doc_id) {
		this.doc_id = doc_id;
	}

	/*
	 * use DocFactory to init doc.
	 */
	private Doc(){}
	
	protected Doc(Map<String, Object> map) throws MPException{
		this.content = (HashMap<String, Object>) map;
		this.text = (String) map.get(Fields.text);
		this.img = (String) map.get(Fields.img);
		this.doc_id = (String) map.get(Fields.doc_id);
	}


	public HashMap<String, Object> getContent() {
		return content;
	}


	public void setContent(HashMap<String, Object> content) {
		this.content = content;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getImg() {
		return img;
	}


	public void setImg(String img) {
		this.img = img;
	}
	
	
}
