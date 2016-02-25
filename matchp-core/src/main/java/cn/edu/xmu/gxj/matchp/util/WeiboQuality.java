package cn.edu.xmu.gxj.matchp.util;

import cn.edu.xmu.gxj.matchp.model.Weibo;

public class WeiboQuality {
	public static boolean highRT(Weibo weibo){
		return weibo.getRt_no() > 0;
	}
	
	public static boolean highLike(Weibo weibo){
		return weibo.getLike_no() > 0;
	}
	
	public static boolean highComent(Weibo weibo){
		return weibo.getComment_no() > 0;
	}
}
