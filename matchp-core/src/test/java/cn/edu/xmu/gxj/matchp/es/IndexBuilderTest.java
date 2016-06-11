package cn.edu.xmu.gxj.matchp.es;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.l2r.QueryRanker;
import cn.edu.xmu.gxj.matchp.model.DocFactory;
import cn.edu.xmu.gxj.matchp.model.DocFactoryTest;
import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.plugins.ImageSign;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.score.EntryBuilder;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;  

@RunWith(MockitoJUnitRunner.class)
public class IndexBuilderTest {

	
	@Mock
	MatchpConfig config;
	
	@Mock
	Sentiment sent;
	
	@Mock
	ImageSign signServer;
	
	@InjectMocks
	EntryBuilder entryBuilder;
	
	@Mock
	QueryRanker queryRanker;
	
	@InjectMocks
	IndexBuilder builder;
	
	@InjectMocks
	DocFactory factory;
	
	@Before
	public void setUp(){
		
		when(config.getEsClusterName()).thenReturn("cdmaok");
		when(config.getEsHostName()).thenReturn("121.192.180.198");
		when(config.getEsTimeout()).thenReturn(5000L);
		when(config.isSentiment_enable()).thenReturn(false);
		when(sent.getSentiment(any(String.class))).thenReturn(0.5f);
		when(signServer.getSignature(any(String.class))).thenReturn("abcdefg");
		when(queryRanker.getPro(any(ArrayList.class))).thenReturn(new double[]{1.1,1.2,1.3});
		builder.init();
		builder.setEntryBuilder(entryBuilder);
		builder.setDocfactory(factory);
	}
	
	@Ignore
	@Test
	public void testLoft(){
		String type = "loft";
		try {
			builder.addDoc(type, DocFactoryTest.case1);
			String query = "scars scars ";
			String result = builder.searchDoc(query);
			
			TypeToken<List<Entry>> token = new TypeToken<List<Entry>>() {};
			ArrayList<Entry> resultList = new Gson().fromJson(result, token.getType());
			assertTrue(resultList.size() >= 1);
			
		} catch (MPException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Ignore
	@Test
	public void testLoftChinese(){
		String type = "loft";
		try {
			builder.addDoc(type, DocFactoryTest.case2);
			String query = "拐弯抹角  软语";
			String result = builder.searchDoc(query);
			
			TypeToken<List<Entry>> token = new TypeToken<List<Entry>>() {};
			ArrayList<Entry> resultList = new Gson().fromJson(result, token.getType());
			assertTrue(resultList.size() >= 1);
			
		} catch (MPException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	

	@Ignore
	@Test
	public void testRamDomDoc(){
		ArrayList<String> which = builder.randomDoc("好饿");
		assertTrue(which.size() == 2);
	}
	
}
