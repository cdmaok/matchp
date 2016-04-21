package cn.edu.xmu.gxj.matchp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class Weibo implements Cloneable{
	
	private Weibo() {
	}
	
	public Weibo(Map<String, Object> map){
		this.comment_no =  (Integer) map.get("comment_no");
		this.text = (String) map.get("text");
		this.uid = (String) map.get("uid");
		this.like_no = (Integer) map.get("like_no");
		this.rt_no = (Integer) map.get("rt_no");
		this.img_url = (String) map.get("img_url");
		this.mid = (String) map.get("mid");
	}
	
	private int comment_no;
	private String text;
	private String uid;
	private int like_no;
	private int rt_no;
	private String img_url;
	private String mid;
	
	
	public int getComment_no() {
		return comment_no;
	}

	public void setComment_no(int comment_no) {
		this.comment_no = comment_no;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getLike_no() {
		return like_no;
	}

	public void setLike_no(int like_no) {
		this.like_no = like_no;
	}

	public int getRt_no() {
		return rt_no;
	}

	public void setRt_no(int rt_no) {
		this.rt_no = rt_no;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	@Override
	public String toString() {
		return "Weibo [comment_no=" + comment_no + ", text=" + text + ", uid=" + uid + ", like_no=" + like_no
				+ ", rt_no=" + rt_no + ", img_url=" + img_url + "]";
	}

	public static Weibo build(Weibo origin) throws CloneNotSupportedException{
		if( origin.getImg_url() != null){
			return (Weibo)origin.clone();
		}else{
			Weibo newWeibo = (Weibo) origin.clone();
			String text = newWeibo.getText();
			Pattern p = Pattern.compile("http://(.*?).(jpg|gif|png|jpeg)");
			Matcher m = p.matcher(text);
			if(m.find()){
				String img_url = m.group(0);
				// somethimes gets wrong case like "http://t.cn/8FnP0Mnhttp://ww1.sinaimg.cn/wap128/6b63135ajw1edtthpmwb9j20z00z0kbx.jpg"
				String short_text = text.replace(img_url, "");
				newWeibo.setImg_url(img_url);
				newWeibo.setText(short_text);
			}
			return newWeibo;
		}
			
	}
	
	//TODO: make it elegant
	public Map<String, Object> toMap(){
		Map<String, Object> jsonDoc = new HashMap<String, Object>();
		jsonDoc.put("like_no", like_no);
		jsonDoc.put("comment_no", comment_no);
		jsonDoc.put("img_url", img_url);
		jsonDoc.put("rt_no", rt_no);
		jsonDoc.put("text", text);
		jsonDoc.put("uid", uid);
		jsonDoc.put("mid",mid);
		return jsonDoc;
	}
	
	public boolean isNotFound(){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(img_url);
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			System.out.println(code);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
