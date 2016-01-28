package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.gson.Gson;

public class WeiboTest {

	public static String str = "{\"comment_no\": \"0\", "
			+ "\"text\": \"今天起来得早吧～我都出去一趟回来老。早起的鸟儿有虫吃[鼓掌][鼓掌]懒猪们 ，早安 [呲牙][太阳]http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\"}";
	
	@Test
	public void str2objecttest(){
		
		try {
			Gson gson = new Gson();
			Weibo weibo = gson.fromJson(str, Weibo.class);
			Weibo newWeibo =  Weibo.build(weibo);
			assertTrue("The new weibo is not null", newWeibo != null);
			assertEquals(newWeibo.getImg_url(), "http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg");
			assertEquals(newWeibo.getText(), "今天起来得早吧～我都出去一趟回来老。早起的鸟儿有虫吃[鼓掌][鼓掌]懒猪们 ，早安 [呲牙][太阳]");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}
	
}
