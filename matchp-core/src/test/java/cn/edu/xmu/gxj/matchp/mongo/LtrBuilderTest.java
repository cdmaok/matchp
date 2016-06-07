package cn.edu.xmu.gxj.matchp.mongo;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
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

	@Test
	public void testquery() {
		String[] query  = ltrbuilder.randomQuery();
		assertTrue(!query[0].trim().equals(""));;
		assertTrue(!query[1].trim().equals(""));;
	}
	
	@Test
	public void testInsertQuery(){
		int answer = 1;
		
	}

}
