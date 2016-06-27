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
	
	public static String case1 = "{\"text\":\"SPACE 03 , Antwerp , Belgium ,Lanren  JUNYUAN\",\"imgSign\":\"30824421520e00f0\","
			+ "\"img\":\"http://imglf.nosdn.127.net/img/K2lnZGdYdHRweU1GeU1PR2tVODJoWERzckJZNDR5YkRpamd6NUp3UzUxOTJSR0lidHJqbFV3PT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg%7Cwatermark&type=2&text=wqkg5oeS5Lq6IC8gbGFucmVuanVueXVhbi5sb2Z0ZXIuY29t&font=bXN5aA==&gravity=southwest&dissolve=30&fontsize=240&dx=8&dy=10&stripmeta=0\","
			+ "\"type\":\"lofter\",\"ocr\":\"0,0\",\"author\":\"Emperor or 神经病患者\","
			+ "\"ref\":\"http://drinkingsakiyamamotoboringshit.lofter.com/post/1cfca106_b4b9dd4\",\"socialScore\":1,\"polarity\":\"0.37140729529840966\","
			+ "\"hist\":\"39.0625,24.0,46.2458472235\",\"sar\":\"c\",\"doc_id\":\"1cfca106_b4b9dd4\",\"hot\":1,\"comment\":0,\"imgSize\":0.618}";
	public static String text1 = "SPACE 03 , Antwerp , Belgium ,Lanren  JUNYUAN";
	public static String img1 = "http://imglf.nosdn.127.net/img/K2lnZGdYdHRweU1GeU1PR2tVODJoWERzckJZNDR5YkRpamd6NUp3UzUxOTJSR0lidHJqbFV3PT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg%7Cwatermark&type=2&text=wqkg5oeS5Lq6IC8gbGFucmVuanVueXVhbi5sb2Z0ZXIuY29t&font=bXN5aA==&gravity=southwest&dissolve=30&fontsize=240&dx=8&dy=10&stripmeta=0";
	public static String doc1 = "1cfca106_b4b9dd4";
	//{"text":"SPACE 03 , Antwerp , Belgium ,Lanren  JUNYUAN","imgSign":"30824421520e00f0","img":"http://imglf.nosdn.127.net/img/K2lnZGdYdHRweU1GeU1PR2tVODJoWERzckJZNDR5YkRpamd6NUp3UzUxOTJSR0lidHJqbFV3PT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg%7Cwatermark&type=2&text=wqkg5oeS5Lq6IC8gbGFucmVuanVueXVhbi5sb2Z0ZXIuY29t&font=bXN5aA==&gravity=southwest&dissolve=30&fontsize=240&dx=8&dy=10&stripmeta=0","type":"lofter","ocr":"0,0","author":"Emperor or 神经病患者","ref":"http://drinkingsakiyamamotoboringshit.lofter.com/post/1cfca106_b4b9dd4","socialScore":1,"polarity":"0.37140729529840966","hist":"39.0625,24.0,46.2458472235","sar":"c","doc_id":"1cfca106_b4b9dd4","hot":1,"comment":0,"imgSize":0.618}
	//{"text":"风起-云涌","imgSign":"c30ff68f072000ff","img":"http://imglf1.ph.126.net/GD81G-v7-8rh_COkgSntZQ==/782781910332393652.jpg","type":"lofter","ocr":"0,0","author":"摄影酱ߓ¸","ref":"http://598826423.lofter.com/post/1d8907f0_b4b10b7","socialScore":22,"polarity":"0.4737150425347657","hist":"39.0625,29.0,70.1062716375","sar":"c","doc_id":"1d8907f0_b4b10b7","hot":22,"comment":0,"imgSize":0.618}
	public static String case2 = "{\"text\":\"风起-云涌\",\"imgSign\":\"c30ff68f072000ff\","
			+ "\"img\":\"http://imglf1.ph.126.net/GD81G-v7-8rh_COkgSntZQ==/782781910332393652.jpg\","
			+ "\"type\":\"lofter\",\"ocr\":\"0,0\",\"author\":\"摄影酱ߓ¸\",\"ref\":\"http://598826423.lofter.com/post/1d8907f0_b4b10b7\",\"socialScore\":22,"
			+ "\"polarity\":\"0.4737150425347657\",\"hist\":\"39.0625,29.0,70.1062716375\",\"sar\":\"c\",\"doc_id\":\"1d8907f0_b4b10b7\",\"hot\":22,\"comment\":0,\"imgSize\":0.618}";
	public static String text2 = "风起-云涌";
	public static String img2 = "http://imglf1.ph.126.net/GD81G-v7-8rh_COkgSntZQ==/782781910332393652.jpg";
	public static String doc2 = "1d8907f0_b4b10b7";

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
			assertEquals(text1, doc.getText());
			assertEquals(img1, doc.getImg());
			assertEquals(doc1, doc.getDoc_id());
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
			assertEquals(text2, doc.getText());
			assertEquals(img2, doc.getImg());
			assertEquals(doc2, doc.getDoc_id());
			Map<String, Object> content = doc.getContent();
			assertTrue(content.containsKey("ref"));
			assertTrue(content.containsKey("author"));
		} catch (MPException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
