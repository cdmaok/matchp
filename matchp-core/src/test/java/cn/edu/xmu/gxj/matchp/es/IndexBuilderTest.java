package cn.edu.xmu.gxj.matchp.es;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;  

@RunWith(MockitoJUnitRunner.class)
public class IndexBuilderTest {

	static String str = "{\"comment_no\": \"0\", "
			+ "\"text\": \"tomorrow is another day . http://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg\", "
			+ "\"uid\": \"2687754223\", "
			+ "\"like_no\": \"0\", "
			+ "\"rt_no\": \"0\",\"mid\":\"123456\"}";

	
	@Mock
	MatchpConfig config;
	
	@InjectMocks
	IndexBuilder builder;
	
	@Before
	public void setUp(){
		
		when(config.getEsClusterName()).thenReturn("elasticsearch");
		when(config.getEsHostName()).thenReturn("114.215.99.92");
		when(config.getEsTimeout()).thenReturn(5000L);
		builder.init();
	}
	
	@Test
	public void addIndexAndQuery(){

		try {
			builder.addDoc(str);
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
	

}
