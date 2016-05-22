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

import cn.edu.xmu.gxj.matchp.model.DocFactory;
import cn.edu.xmu.gxj.matchp.model.DocFactoryTest;
import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.plugins.ImageSign;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.score.EntryBuilder;
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
	
	@InjectMocks
	IndexBuilder builder;
	
	@InjectMocks
	DocFactory factory;
	
	@Before
	public void setUp(){
		
		when(config.getEsClusterName()).thenReturn("elasticsearch");
		when(config.getEsHostName()).thenReturn("121.192.180.198");
		when(config.getEsTimeout()).thenReturn(5000L);
		when(config.isSentiment_enable()).thenReturn(false);
		when(sent.getSentiment(any(String.class))).thenReturn(0.5f);
		when(signServer.getSignature(any(String.class))).thenReturn("abcdefg");
		builder.init();
		builder.setEntryBuilder(entryBuilder);
		builder.setDocfactory(factory);
	}
	
	@Test
	public void testaddLoft(){
		String type = "loft";
		builder.addDoc(type, DocFactoryTest.case1);
	}
	

}
