package cn.edu.xmu.gxj.matchp.l2r;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;
import cn.edu.xmu.gxj.matchp.model.Doc;
import cn.edu.xmu.gxj.matchp.model.DocFactory;

public class RankListBuilderTest {
	
	static String[] paras = new String[]{"1 qid:1 1:0.746791131855 2:0.720241038715 3:4.95131587982 4:0.5 5:22",
			"2 qid:1 1:1.76923076923 2:0.829843408715 3:4.91391134262 4:0.5 5:4"};

	@Test
	public void test() {
		RankList rl = RankListBuilder.build(paras);
		assertEquals("1",rl.getID());
		assertTrue(rl.size() == 2);
		DataPoint dp =  rl.get(0);
		assertEquals(dp.getLabel(), 1, 0.0);
		assertEquals(dp.getFeatureValue(1), 0.746791131855, 0.001);
		assertEquals(dp.getFeatureValue(2), 0.720241038715, 0.001);
		assertEquals(dp.getFeatureValue(3), 4.95131587982, 0.001);
		assertEquals(dp.getFeatureValue(4), 0.5, 0.001);
		assertEquals(dp.getFeatureValue(5), 22, 0.001);
		assertEquals(dp.getFeatureCount(), 5);
	}

	
}
