package cn.edu.xmu.gxj.matchp.score;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@RunWith(MockitoJUnitRunner.class)
public class EntryBuilderTest {
	
	@Mock
	private MatchpConfig config;
	
	@Mock
	private Sentiment sent;
	
	@InjectMocks
	private EntryBuilder builder;
	
	public static String test = "abc";

	@Before
	public void SetUp() {
		when(config.isSentiment_enable()).thenReturn(true);
		when(sent.getSentiment(test)).thenReturn(0.5f);
	}
	
	@Test
	public void test(){
		Map<String, Object> map = new HashMap<>();
		map.put(Fields.polarity, 0.34);
		map.put(Fields.text, "abc");
		map.put(Fields.url, "abc");
		float hit = 0.45f;
		Entry entry = builder.buildEntry(0.5,	 map, hit);
		assertEquals( 1 - (0.5-0.34) + hit, entry.getScore(), 0.01);
	}

}
