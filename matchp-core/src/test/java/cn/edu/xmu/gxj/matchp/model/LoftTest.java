package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

public class LoftTest {

	private String jsonText = "{\"comment\": 0, \"img\": \"http://imglf2.nosdn.127.net/img/ejdrSC9GT1BVTUFmRlRSL3FDVFZPUDByOGRVKzh2TDMzeXJtczV5R0JtK0ttblAwTVRPRG9nPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg\", "
			+ "\"author\": \"BMW\", \"text\": \"我希望， 每一个时刻，都象彩色蜡笔那样美丽。 我希望， 能在心爱的白纸上画画，画出笨拙的自由，画下一只永远不会，流泪的眼睛。           ————顾城\", \"hot\": 118, "
			+ "\"tid\": \"1d81a33a_affcc88\", \"ref\": \"http://68844335.lofter.com/post/1d81a33a_affcc88\"}";
	
	@Test
	public void test() {
		Gson gson = new Gson();
		Loft loft = gson.fromJson(jsonText, Loft.class);
		assertEquals(0, loft.getComment());
		assertEquals("http://imglf2.nosdn.127.net/img/ejdrSC9GT1BVTUFmRlRSL3FDVFZPUDByOGRVKzh2TDMzeXJtczV5R0JtK0ttblAwTVRPRG9nPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg",
				loft.getImg());
		assertEquals("BMW",loft.getAuthor());
		assertEquals("我希望， 每一个时刻，都象彩色蜡笔那样美丽。 我希望， 能在心爱的白纸上画画，画出笨拙的自由，画下一只永远不会，流泪的眼睛。           ————顾城", loft.getText());
		assertEquals("1d81a33a_affcc88", loft.getTid());
		assertEquals("http://68844335.lofter.com/post/1d81a33a_affcc88", loft.getRef());
	}

}
