package cn.edu.xmu.gxj.matchp.score;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.SearchHit;
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
	
	@Mock
	private SearchHit hit;
	
	@InjectMocks
	private EntryBuilder builder;
	
	public static String test = "abc";

	@Before
	public void SetUp() {
		Map<String, Object> map = new HashMap<>();
		map.put(Fields.polarity, 0.34);
		map.put(Fields.text, "abc");
		map.put(Fields.img, "abc");
		map.put(Fields.loft_comments, 30);
		map.put(Fields.loft_hot, 10);
		map.put(Fields.imgSize, 1.0);
		float ir_score = 0.45f;
		when(config.isSentiment_enable()).thenReturn(true);
		when(sent.getSentiment(test)).thenReturn(0.5f);
		when(hit.getScore()).thenReturn(ir_score);
		when(hit.getSource()).thenReturn(map);
		when(hit.getType()).thenReturn("loft");
	}
	
	@Test
	public void test(){

		Entry entry = builder.buildEntry(0.5,hit);
//		assertEquals( 1 - (0.5-0.34) + 0.45f + 1 + 40 + 1, entry.getScore(), 0.01);
	}

}
