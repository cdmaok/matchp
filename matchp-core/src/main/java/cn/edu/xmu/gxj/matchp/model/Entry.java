package cn.edu.xmu.gxj.matchp.model;

public class Entry {
	private String text;
	private String url;
	
	public Entry(String text, String url) {
		this.text = text;
		this.url = url;
	}
	
	public Entry(Weibo weibo){
		this(weibo.getText(),weibo.getImg_url());
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

	@Override
	public String toString() {
		return "Entry [text=" + text + ", url=" + url + "]";
	}
	
}
