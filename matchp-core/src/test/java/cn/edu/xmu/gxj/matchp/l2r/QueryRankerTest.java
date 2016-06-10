package cn.edu.xmu.gxj.matchp.l2r;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class QueryRankerTest {

	private static QueryRanker qranker;
	
	@Before
	public void setUp(){
		qranker = new QueryRanker();
		qranker.init();
	}
	
	@Test
	public void testloadRanker() {
		qranker.loadRanker();
	}
	
	@Ignore
	@Test
	public void testtrain(){
		String trainFile = "../2016-06-09.dat";
		String modelFile = "../2016-06-09.model";
		qranker.setTrainFile(trainFile);
		qranker.setModelFile(modelFile);
		qranker.train();
		assertTrue(new File(modelFile).exists());
	}
	
	
	@Test
	public void testtest(){
		double[] pros =  qranker.getPro(RankListBuilderTest.paras);
		assertTrue(pros.length == 2);
		assertTrue(pros[0] > 0);
		assertTrue(pros[1] > 0);
	}
	

}
