package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import com.google.gson.Gson;

public class WeiboTest {

	public static String normal_weibo = "{\"comment_no\": \"0\", "
			+ "\"text\": \"今天起来得早吧～我都出去一趟回来老。早起的鸟儿有虫吃[鼓掌][鼓掌]懒猪们 ，早安 [呲牙][太阳]http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\"}";
	
	
	public static String url_weibo = "{\"rt_no\": \"0\", \"like_no\": \"0\", "
			+ "\"text\": \"从今天开始，写下我的 #小红书购物笔记# ，从今天开始，不遗忘妆点自己最好年华的点点滴滴，从今天>开始，和地球某处的朋友一起探索世间好物。2014，和我一起，点击 http://t.cn/8FnP0Mnhttp://ww1.sinaimg.cn/wap128/6b63135ajw1edtthpmwb9j20z00z0kbx.jpg\", "
			+ "\"mid\": \"3681307966339621\", \"comment_no\": \"0\", \"uid\": \"1801655130\"}";
	
	@Test
	public void str2objecttest(){
		
		try {
			Gson gson = new Gson();
			Weibo weibo = gson.fromJson(normal_weibo, Weibo.class);
			Weibo newWeibo =  Weibo.build(weibo);
			assertTrue("The new weibo is not null", newWeibo != null);
			assertEquals(newWeibo.getImg_url(), "http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg");
			assertEquals(newWeibo.getText(), "今天起来得早吧～我都出去一趟回来老。早起的鸟儿有虫吃[鼓掌][鼓掌]懒猪们 ，早安 [呲牙][太阳]");
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
			assertEquals(newWeibo.getText(), "从今天开始，写下我的 #小红书购物笔记# ，从今天开始，不遗忘妆点自己最好年华的点点滴滴，从今天>开始，和地球某处的朋友一起探索世间好物。2014，和我一起，点击 http://t.cn/8FnP0Mn");
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
