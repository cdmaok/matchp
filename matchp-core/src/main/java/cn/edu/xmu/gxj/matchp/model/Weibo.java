package cn.edu.xmu.gxj.matchp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Weibo implements Cloneable{
	
	private Weibo() {
	}
	
	
	private String comment_no;
	private String text;
	private String uid;
	private String like_no;
	private String rt_no;
	private String img_url;
	public String getComment_no() {
		return comment_no;
	}
	public void setComment_no(String comment_no) {
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
	public String getLike_no() {
		return like_no;
	}
	public void setLike_no(String like_no) {
		this.like_no = like_no;
	}
	public String getRt_no() {
		return rt_no;
	}
	public void setRt_no(String rt_no) {
		this.rt_no = rt_no;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
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
			Pattern p = Pattern.compile("http://.*[(.jpg)|(.png)]");
			Matcher m = p.matcher(text);
			if(m.find()){
				String img_url = m.group(0);
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
		return jsonDoc;
	}
	
}
