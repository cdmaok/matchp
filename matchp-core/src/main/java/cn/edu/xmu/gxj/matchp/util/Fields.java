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
	
	public static enum Type{
		WEIBO("weibo"),
		LOFT("loft");
		private final String name;
		private Type(String s){
			name = s;
		}
	}
}
