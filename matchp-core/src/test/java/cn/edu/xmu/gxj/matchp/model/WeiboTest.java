package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

public class WeiboTest {

	public static String normal_weibo = "{\"comment_no\": \"0\", "
			+ "\"text\": \"浠婂ぉ璧锋潵寰楁棭鍚э綖鎴戦兘鍑哄幓涓�瓒熷洖鏉ヨ�併�傛棭璧风殑楦熷効鏈夎櫕鍚僛榧撴帉][榧撴帉]鎳掔尓浠� 锛屾棭瀹� [鍛茬墮][澶槼]http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\"}";
	
	
	public static String url_weibo = "{\"rt_no\": \"0\", \"like_no\": \"0\", "
			+ "\"text\": \"浠庝粖澶╁紑濮嬶紝鍐欎笅鎴戠殑 #灏忕孩涔﹁喘鐗╃瑪璁�# 锛屼粠浠婂ぉ寮�濮嬶紝涓嶉仐蹇樺鐐硅嚜宸辨渶濂藉勾鍗庣殑鐐圭偣婊存淮锛屼粠浠婂ぉ>寮�濮嬶紝鍜屽湴鐞冩煇澶勭殑鏈嬪弸涓�璧锋帰绱笘闂村ソ鐗┿��2014锛屽拰鎴戜竴璧凤紝鐐瑰嚮 http://t.cn/8FnP0Mnhttp://ww1.sinaimg.cn/wap128/6b63135ajw1edtthpmwb9j20z00z0kbx.jpg\", "
			+ "\"mid\": \"3681307966339621\", \"comment_no\": \"0\", \"uid\": \"1801655130\"}";
	
	@Test
	public void str2objecttest(){
		
		try {
			Gson gson = new Gson();
			Weibo weibo = gson.fromJson(normal_weibo, Weibo.class);
			Weibo newWeibo =  Weibo.build(weibo);
			assertTrue("The new weibo is not null", newWeibo != null);
			assertEquals(newWeibo.getImg_url(), "http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg");
			assertEquals(newWeibo.getText(), "浠婂ぉ璧锋潵寰楁棭鍚э綖鎴戦兘鍑哄幓涓�瓒熷洖鏉ヨ�併�傛棭璧风殑楦熷効鏈夎櫕鍚僛榧撴帉][榧撴帉]鎳掔尓浠� 锛屾棭瀹� [鍛茬墮][澶槼]");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}
	
	@Test
	public void urlstr2objecttest(){
		
		try {
			Gson gson = new Gson();
			Weibo weibo = gson.fromJson(url_weibo, Weibo.class);
			Weibo newWeibo =  Weibo.build(weibo);
			assertTrue("The new weibo is not null", newWeibo != null);
			assertEquals(newWeibo.getImg_url(), "http://ww1.sinaimg.cn/wap128/6b63135ajw1edtthpmwb9j20z00z0kbx.jpg");
			assertEquals(newWeibo.getText(), "浠庝粖澶╁紑濮嬶紝鍐欎笅鎴戠殑 #灏忕孩涔﹁喘鐗╃瑪璁�# 锛屼粠浠婂ぉ寮�濮嬶紝涓嶉仐蹇樺鐐硅嚜宸辨渶濂藉勾鍗庣殑鐐圭偣婊存淮锛屼粠浠婂ぉ>寮�濮嬶紝鍜屽湴鐞冩煇澶勭殑鏈嬪弸涓�璧锋帰绱笘闂村ソ鐗┿��2014锛屽拰鎴戜竴璧凤紝鐐瑰嚮 http://t.cn/8FnP0Mn");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}
	
	@Ignore
//	@Test
	public void testNotFound(){
		
		try {
			Gson gson = new Gson();
			Weibo weibo = gson.fromJson(normal_weibo, Weibo.class);
			Weibo newWeibo =  Weibo.build(weibo);
			newWeibo.setImg_url("http://wtf");
			assertTrue(newWeibo.isNotFound());
			
					
		} catch (Exception e) {
			return;
		} 
	}
	
	@Ignore
//	@Test
	public void testFound(){
		
		try {
			Gson gson = new Gson();
			Weibo weibo = gson.fromJson(normal_weibo, Weibo.class);
			Weibo newWeibo =  Weibo.build(weibo);
			
			newWeibo.setImg_url("http://ww1.sinaimg.cn/wap128/83a4f8cdjw1eg09yxmlrjj20x718gn9k.jpg");
			assertTrue(!newWeibo.isNotFound());
					
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}
	

	
}
