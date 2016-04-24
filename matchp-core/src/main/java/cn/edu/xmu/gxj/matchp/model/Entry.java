package cn.edu.xmu.gxj.matchp.model;

public class Entry {
	private String text;
	private String url;
	private float score;
	
	
	public Entry(String text, String url,float score) {
		this.text = text;
		this.url = url;
		this.score = score;
	}
	
	public Entry(Weibo weibo,float score){
		this(weibo.getText(),weibo.getImg_url(),score);
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
	
}
