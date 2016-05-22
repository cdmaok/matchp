package cn.edu.xmu.gxj.matchp.es;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.score.EntryBuilder;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;  

@RunWith(MockitoJUnitRunner.class)
public class IndexBuilderTest {

	static String str = "{\"comment_no\": \"0\", "
			+ "\"text\": \"tomorrow is another day . http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\",\"mid\":\"123456\"}";
	
	static String chineStr = "{\"rt_no\": \"0\", "
			+ "\"like_no\": \"0\", \"text\": "
			+ "\"好久没有主持过全场婚礼了>，虚脱http://ww1.sinaimg.cn/wap128/83a4f8cdjw1eg09yxmlrjj20x718gn9k.jpg\","
			+ " \"mid\": \"3705901208373296\", \"comment_no\": \"0\", \"uid\": \"2208626893\"}";

	
	@Mock
	MatchpConfig config;
	
	@Mock
	Sentiment sent;
	
	@InjectMocks
	EntryBuilder entryBuilder;
	
	@InjectMocks
	IndexBuilder builder;
	
	@Before
	public void setUp(){
		
		when(config.getEsClusterName()).thenReturn("elasticsearch");
		when(config.getEsHostName()).thenReturn("121.192.180.198");
		when(config.getEsTimeout()).thenReturn(5000L);
		when(config.isSentiment_enable()).thenReturn(false);
		when(sent.getSentiment(any(String.class))).thenReturn(0.5f);
		builder.init();
		builder.setEntryBuilder(entryBuilder);
	}
	
	@Test
	public void addIndexAndQuery(){

		try {
			builder.addDoc("weibo",str);
			String ret = builder.searchDoc("another");
			TypeToken<List<Entry>> token = new TypeToken<List<Entry>>() {};
			ArrayList<Entry> results = new Gson().fromJson(ret, token.getType());
			assertTrue(results.size() >= 1);
			assertEquals(1, results.size(), 0);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}
	
	
	@Test
	public void addChineseIndexAndQuery(){

		try {
			builder.addDoc("weibo",chineStr);
			String ret = builder.searchDoc("好久 主持");
			TypeToken<List<Entry>> token = new TypeToken<List<Entry>>() {};
			ArrayList<Entry> results = new Gson().fromJson(ret, token.getType());
			assertTrue(results.size() >= 1);
			System.out.println(results.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}
	

}
