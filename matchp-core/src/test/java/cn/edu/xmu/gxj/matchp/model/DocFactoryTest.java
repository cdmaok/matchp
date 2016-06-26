package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cn.edu.xmu.gxj.matchp.plugins.ImageSign;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class DocFactoryTest {
	
	public static String case1 = "{\"comment\": 0, \"img\": \"http://imglf0.nosdn.127.net/img/ejdrSC9GT1BVTUFjbkpGRXg0SmFqYUl4QmdnRnp2T2wzc3RCZkZqU0ZIWU4wR3ArbkU5ejhRPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg\", "
			+ "\"author\": \"BMW\", "
			+ "\"text\": \"When the day approaches to its end,  i come close to you. You will notice my scars, so you will be acquainted that i've been wounded——《Say Birds》\", \"hot\": 87, "
			+ "\"doc_id\": \"1d81a33a_b001329\", "
			+ "\"ref\": \"http://68844335.lofter.com/post/1d81a33a_b001329\",\"Type\":\"tumblr\"}";
	
	
	public static String case2 = "{\"comment\": 45, \"img\": \"http://imglf0.nosdn.127.net/img/ejdrSC9GT1BVTUFjbkpGRXg0SmFqYUl4QmdnRnp2T2wzc3RCZkZqU0ZIWU4wR3ArbkU5ejhRPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg\", "
			+ "\"author\": \"BMW\", "
			+ "\"text\": \"会哭的孩子有人哄，因为他敢用最直白的方式提要求。大家都喜欢直性子，因为简单直白的表述，是表达信任的方式。拐弯抹角的软语，反而刻画了距离。含蓄，也是一种疏远。摄影师：如歌zy\", \"hot\": 87, "
			+ "\"doc_id\": \"1d81a33a_b001339\", "
			+ "\"ref\": \"http://chenzhiw.lofter.com/post/1ccd4f42_96275eb\",\"Type\":\"tumblr\"}";

	@Mock
	MatchpConfig config;
	
	@Mock
	ImageSign signServer;
	
	@Mock
	Sentiment senServer;
	
	@InjectMocks
	DocFactory factory;
	
	@Before
	public void setup(){
		when(config.getEsClusterName()).thenReturn("elasticsearch");
		when(config.getEsHostName()).thenReturn("121.192.180.198");
		when(config.getEsTimeout()).thenReturn(5000L);
		when(config.isSentiment_enable()).thenReturn(true);
		when(config.isCheck_img()).thenReturn(true);
		when(senServer.getSentiment(any(String.class))).thenReturn(0.5f);
		when(signServer.getSignature(any(String.class))).thenReturn("signature");
	}
	
	@Test
	public void test() {
		Doc doc;
		try {
			doc = factory.Build(case1);
			assertEquals("When the day approaches to its end,  i come close to you. You will notice my scars, so you will be acquainted that i've been wounded——《Say Birds》", doc.getText());
			assertEquals("http://imglf0.nosdn.127.net/img/ejdrSC9GT1BVTUFjbkpGRXg0SmFqYUl4QmdnRnp2T2wzc3RCZkZqU0ZIWU4wR3ArbkU5ejhRPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg", doc.getImg());
			assertEquals("1d81a33a_b001329", doc.getDoc_id());
			Map<String, Object> content = doc.getContent();
			assertTrue(content.containsKey("ref"));
			assertTrue(content.containsKey("author"));
			assertTrue(content.containsKey(Fields.imgSize));
			assertTrue(content.containsKey(Fields.imgSign));
			assertTrue(content.containsKey(Fields.polarity));
		} catch (MPException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testChinese(){
		Doc doc;
		try {
			doc = factory.Build(case2);
			assertEquals("会哭的孩子有人哄，因为他敢用最直白的方式提要求。大家都喜欢直性子，因为简单直白的表述，是表达信任的方式。拐弯抹角的软语，反而刻画了距离。含蓄，也是一种疏远。摄影师：如歌zy", doc.getText());
			assertEquals("http://imglf0.nosdn.127.net/img/ejdrSC9GT1BVTUFjbkpGRXg0SmFqYUl4QmdnRnp2T2wzc3RCZkZqU0ZIWU4wR3ArbkU5ejhRPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg", doc.getImg());
			assertEquals("1d81a33a_b001339", doc.getDoc_id());
			Map<String, Object> content = doc.getContent();
			assertTrue(content.containsKey("ref"));
			assertTrue(content.containsKey("author"));
		} catch (MPException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
