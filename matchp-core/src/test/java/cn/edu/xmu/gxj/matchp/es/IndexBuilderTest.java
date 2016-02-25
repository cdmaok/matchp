package cn.edu.xmu.gxj.matchp.es;

import static org.junit.Assert.fail;
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

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;  

@RunWith(MockitoJUnitRunner.class)
public class IndexBuilderTest {

	@Mock
	MatchpConfig config;
	
	@InjectMocks
	IndexBuilder builder;
	
	
	@Before
	public void setUp(){
		
		when(config.getEsPath()).thenReturn("E:\\workspace\\matchp\\matchp-core");
		when(config.getEsClusterName()).thenReturn("locales");
	}
	
	@Test
	public void test() {
		try {
			builder.addDoc();
			String ret = builder.searchDoc("今天");
			TypeToken<List<Entry>> token = new TypeToken<List<Entry>>() {};
			ArrayList<Entry> results = new Gson().fromJson(ret, token.getType());
			System.out.println(results.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}

}
