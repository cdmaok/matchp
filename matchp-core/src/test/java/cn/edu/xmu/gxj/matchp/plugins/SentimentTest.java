package cn.edu.xmu.gxj.matchp.plugins;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@RunWith(MockitoJUnitRunner.class)
public class SentimentTest {

	@Mock
	MatchpConfig config;
	
	@InjectMocks
	Sentiment sent;
	
	@Before
	public void setUp(){
		when(config.getSentiment_server()).thenReturn("121.192.180.198:8000");
		when(config.isSentiment_enable()).thenReturn(true);
		sent.init();
	}
	
	@Test
	public void test() {
//		assertEquals(0.5, sent.getSentiment("abc"), 0);
		assertTrue(sent.getSentiment("好丑") < 0.5);
		assertTrue(sent.getSentiment("好漂亮") > 0.5);
		assertTrue(sent.getSentiment("好漂亮..") > 0.5);
	}

}
