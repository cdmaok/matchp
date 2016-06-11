package cn.edu.xmu.gxj.matchp.l2r;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cn.edu.xmu.gxj.matchp.mongo.LtrBuilder;

@RunWith(MockitoJUnitRunner.class)
public class QueryRankerTest {

	@Mock
	LtrBuilder ltrbuilder;

	@InjectMocks
	private static QueryRanker qranker;
	
	@Before
	public void setUp(){
//		qranker.setDirectory("../");
		qranker.setTrainFile("../2016-06-10.dat");
		qranker.setModelFile("../2016-06-10.model");
		when(ltrbuilder.dumpRecord()).thenReturn(new String[]{
				"2 qid:0 1:1.1299435028248588 2:0.9873555676002502 3:0.3520396947860718 4:0.5 5:7.0\n1 qid:0 1:0.5625 2:0.9271111676002503 3:0.2742762565612793 4:0.5 5:16.0",
				"1 qid:1 1:0.7467911318553092 2:0.7202410387146759 3:4.951315879821777 4:0.5 5:22.0\n2 qid:1 1:1.7692307692307692 2:0.8298434087146759 3:4.91391134262085 4:0.5 5:4.0",
				"2 qid:2 1:0.75 2:0.7111179516477966 3:1.2434462308883667 4:0.5 5:3.0\n1 qid:2 1:0.6659877800407332 2:0.5817125416477966 3:0.9821234941482544 4:0.5 5:0.0",
				"2 qid:3 1:1.4814814814814814 2:0.9999580768371582 3:0.8479887843132019 4:0.5 5:1.0\n1 qid:3 1:1.25 2:0.9999995231628418 3:0.6827282905578613 4:0.5 5:347.0",
				"2 qid:4 1:1.486988847583643 2:0.9991694611854554 3:0.8840274810791016 4:0.5 5:0.0\n1 qid:4 1:0.6666666666666666 2:0.9991374611854553 3:0.47188687324523926 4:0.5 5:217.0", 
				"2 qid:5 1:1.3333333333333333 2:0.5668776958303833 3:4.48736047744751 4:0.5 5:7.0\n1 qid:5 1:1.0 2:0.9071733441696167 3:2.513148784637451 4:0.5 5:4.0",
				"2 qid:6 1:1.3333333333333333 2:0.9809682227615356 3:1.7570226192474365 4:0.5 5:467.0\n1 qid:6 1:0.6812278630460449 2:0.9415592527615356 3:1.4384756088256836 4:0.5 5:37.0", 
				"1 qid:7 1:0.75 2:0.9998174925544738 3:0.806220293045044 4:0.5 5:247.0\n2 qid:7 1:1.2222222222222223 2:0.09792159744552609 3:1.0005971193313599 4:0.5 5:1.0", 
				"1 qid:8 1:0.6666666666666666 2:0.949626773602295 3:0.8884510397911072 4:0.5 5:130.0\n2 qid:8 1:0.7489795918367347 2:0.8850178736022949 3:0.8577324748039246 4:0.5 5:2.0"
				 });
//		qranker = new QueryRanker();
//		qranker.init();
	}
	
	@Ignore
	@Test
	public void testloadRanker() {
		qranker.init();
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
	
	
	@Ignore
	@Test
	public void testdump(){
		qranker.dumptrainData();
	}
	
	
	@Ignore
	@Test
	public void testtest(){
		double[] pros =  qranker.getPro(RankListBuilderTest.paras);
		assertTrue(pros.length == 2);
		assertTrue(pros[0] > 0);
		assertTrue(pros[1] > 0);
	}
	

}
