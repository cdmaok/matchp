package cn.edu.xmu.gxj.matchp.util;

public class Fields {
	public static final String queryField = "text";
	public static final String polarity = "polarity";
	public static final String text = "text";
	public static final String url = "img_url";
	public static enum Type{
		WEIBO("weibo"),
		LOFT("loft");
		private final String name;
		private Type(String s){
			name = s;
		}
	}
}
