package cn.edu.xmu.gxj.matchp.plugins;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.Module.SetupContext;

import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@RunWith(MockitoJUnitRunner.class)
public class ImageSignTest {
	
	@Mock
	MatchpConfig config;
	
	@InjectMocks
	ImageSign sign;
	
	@Before
	public void setUp(){
		when(config.isImage_sign_enable()).thenReturn(true);
		when(config.getSentiment_server()).thenReturn("121.192.180.198:8000");
		sign.init();
	}

	@Test
	public void test() {
		String signature = sign.getSignature("https://www.baidu.com/img/bd_logo1.png");
		assertTrue(signature.equals("000008c2b061debf"));
	}

}
