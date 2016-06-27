package cn.edu.xmu.gxj.matchp.util;

public class Fields {
	/*
	 * 
	 * text: just text as you know    
     * polarity: text's sentiment  
     * img: image's url  
     * imgSign: image's signature  
     * doc_id: text's id  
	 * 
	 */
	public static final String queryField = "text";
	public static final String text = "text";
	public static final String img = "img";
	public static final String polarity = "polarity";	
	public static final String imgSign = "imgSign";
	public static final String doc_id = "doc_id";
	public static final String imgSize = "imgSize";
	public static final String hist = "hist";
	public static final String type = "type";
	
	// for entry
	public static final String score = "Finscore";
	
	// some field for social score
	public static final String weibo_goods = "goods";
	public static final String weibo_repost = "repost";
	public static final String weibo_comments = "comments";
	public static final String loft_comments = "comment";
	public static final String loft_hot = "hot";
	public static final String tumblr_hot = "hot";
	
	public static final String SOSCORE_FIELD = "socialScore";
	public static final String SAR_FIELD = "sar"; //use
	public static final String OCR_FIELD = "ocr"; 
	public static final String HIST_FIELD = "hist";
	
	public static final String FeatureVector = "feature";
}
