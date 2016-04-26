package cn.edu.xmu.gxj.matchp.model;

import java.util.Map;

public class Entry {
	private String text;
	private String url;
	private float score;
	
	
	public Entry(String text, String url,float score) {
		this.text = text;
		this.url = url;
		this.score = score;
	}
	
	public Entry(Map<String, Object> map,float score){
		this.text = (String) map.get("text");
		this.url = (String) map.get("img_url");
		this.score = score;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "Entry [text=" + text + ", url=" + url + "]";
	}
	
	public float calSentiScore(float origin,float hit){
		return 1 - ( Math.abs(origin - hit) );
	}
	
}
