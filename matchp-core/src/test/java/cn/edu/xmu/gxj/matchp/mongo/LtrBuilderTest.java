package cn.edu.xmu.gxj.matchp.mongo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@RunWith(MockitoJUnitRunner.class)
public class LtrBuilderTest {
	
	@Mock
	MatchpConfig config;
	
	@InjectMocks
	LtrBuilder ltrbuilder;
	
	@Before
	public void setUp(){
		when(config.getLtr_mongo_url()).thenReturn("mongodb://matchp:Matchp123456@114.215.99.92:27017/matchp");
		ltrbuilder.init();
	}
	
	@After
	public void destroy(){
		ltrbuilder.destroy();
	}

//	@Ignore
	@Test
	public void testquery() {
		String[] query  = ltrbuilder.randomQuery();
		assertTrue(!query[0].trim().equals(""));;
		assertTrue(!query[1].trim().equals(""));;
	}
	
//	@Ignore
	@Test
	public void testloadAnno(){
		String[] trainData = ltrbuilder.dumpRecord();
		System.out.println(trainData);
	}
	
	@Deprecated
	@Ignore
	@Test
	public void testdoc2pair(){
		String docjson = "{ \"_id\" : \"57563ba7d59373472a2b955f\",\"query\" : \"杭州今天是要下雪了吗？感觉打到车上的是小冰粒。\", \"entrys\" : [\"{\\\"uid\\\":0,"
				+ "\\\"imgSize\\\":1.3333333333333333,\\\"text\\\":\\\"杭州下着雪，胜利河美食街吃杭州莱！\\\",\\\"imgSign\\\":\\\"fef18b37f82649e2\\\","
				+ "\\\"img\\\":\\\"http://ww1.sinaimg.cn/large/795c4322jw1e0gkyp2phfj.jpg\\\",\\\"goods\\\":0,\\\"SentiScore\\\":0.6108107106369018,"
				+ "\\\"IrScore\\\":0.5429583787918091,\\\"TypeScore\\\":0.5,\\\"polarity\\\":0.9882807,\\\"doc_id\\\":\\\"3530461063969015\\\","
				+ "\\\"FinScore\\\":1.653769089428711,\\\"repost\\\":1,\\\"comments\\\":9}\", "
				+ "\"{\\\"uid\\\":0,\\\"imgSize\\\":0.7515625,\\\"text\\\":\\\"路上跟老妈通电话，老妈说杭州昨晚又下雪了，我说北京晴天\\\",\\\"imgSign\\\":\\\"fffb070f102060c0\\\","
				+ "\\\"img\\\":\\\"http://ww3.sinaimg.cn/large/59379692jw1dqfrrv6cduj.jpg\\\",\\\"goods\\\":0,\\\"SentiScore\\\":0.41392044836309816,\\\"IrScore\\\":0.5328543782234192,"
				+ "\\\"TypeScore\\\":0.5,\\\"polarity\\\":0.013011859,\\\"doc_id\\\":\\\"3417362508690757\\\",\\\"FinScore\\\":1.4467748265865175,\\\"repost\\\":1,\\\"comments\\\":5}\"], "
				+ "\"answer\" : 1 }";
		System.out.println(docjson.substring(630, 650));
		Document document = Document.parse(docjson);
		String pair = ltrbuilder.doc2pair(0,document);
		assertTrue(pair.equals("2 qid:0 1:1.3333333333333333 2:0.6108107106369018 3:0.5429583787918091 4:0.5 5:10.0\n1 qid:0 1:0.7515625 2:0.41392044836309816 3:0.5328543782234192 4:0.5 5:6.0"));
	}

}
