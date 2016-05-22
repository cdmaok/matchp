package cn.edu.xmu.gxj.matchp.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Deprecated
public class Weibo implements Cloneable{
	
	private Weibo() {}
	
	public Weibo(Map<String, Object> map){
		this.comment_no =  (Integer) map.get("comment_no");
		this.text = (String) map.get("text");
		this.uid = (String) map.get("uid");
		this.like_no = (Integer) map.get("like_no");
		this.rt_no = (Integer) map.get("rt_no");
		this.img_url = (String) map.get("img_url");
		this.mid = (String) map.get("mid");
		this.polarity = (Float) map.get("polarity");
		this.size = (Float) map.get("size");
	}
	
	private int comment_no;
	private String text;
	private String uid;
	private int like_no;
	private int rt_no;
	private String img_url;
	private String mid;
	private float polarity;
	
	private String imgSignature;
	
	// pic's size
	private float size;
	
	
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
			
			String img_url = origin.getImg_url();
			
			return (Weibo)origin.clone();
		}else{
			Weibo newWeibo = (Weibo) origin.clone();
			String text = newWeibo.getText();
			
			// find the image'url and replace text
			//TODO: this is so low we need to improve it later.
			Pattern p = Pattern.compile("http://[^:]*.(jpg|gif|png|jpeg)");
			Matcher m = p.matcher(text);
			while(m.find()){
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
		jsonDoc.put("polarity",polarity);
		jsonDoc.put("signature", imgSignature);
		return jsonDoc;
	}
	
	public boolean isNotFound(){
		//TODO: so ugly. change it.
		if (img_url == null) {
			return true;
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(img_url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			httpclient.close();
			int code = response.getStatusLine().getStatusCode();
			response.close();
			Pic pic = new Pic(img_url);
			size = pic.getWidth() / pic.getHeight();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			size = 0;
			return true;
		}
		return false;
	}
	
	public boolean isChatter(){
		//TODO: make it chatter
		return false;
	}

	public float getPolarity() {
		return polarity;
	}

	public void setPolarity(float polarity) {
		this.polarity = polarity;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
	}

	public String getImgSignature() {
		return imgSignature;
	}

	public void setImgSignature(String imgSignature) {
		this.imgSignature = imgSignature;
	}
		
	
	
}
