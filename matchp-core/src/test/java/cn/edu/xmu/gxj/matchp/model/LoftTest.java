package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.Gson;

public class LoftTest {

	private String jsonText = "{\"comment\": 0, \"img\": \"http://imglf2.nosdn.127.net/img/ejdrSC9GT1BVTUFmRlRSL3FDVFZPUDByOGRVKzh2TDMzeXJtczV5R0JtK0ttblAwTVRPRG9nPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg\", "
			+ "\"author\": \"BMW\", \"text\": \"鎴戝笇鏈涳紝聽姣忎竴涓椂鍒伙紝閮借薄褰╄壊铚＄瑪閭ｆ牱缇庝附銆偮犳垜甯屾湜锛屄犺兘鍦ㄥ績鐖辩殑鐧界焊涓婄敾鐢伙紝鐢诲嚭绗ㄦ嫏鐨勮嚜鐢憋紝鐢讳笅涓�鍙案杩滀笉浼氾紝娴佹唱鐨勭溂鐫涖�偮犅犅犅犅犅犅犅犅犅犅犫�斺�斺�斺�旈【鍩嶾\", \"hot\": 118, "
			+ "\"tid\": \"1d81a33a_affcc88\", \"ref\": \"http://68844335.lofter.com/post/1d81a33a_affcc88\"}";
	
	@Test
	public void test() {
		Gson gson = new Gson();
		Loft loft = gson.fromJson(jsonText, Loft.class);
		assertEquals(0, loft.getComment());
		assertEquals("http://imglf2.nosdn.127.net/img/ejdrSC9GT1BVTUFmRlRSL3FDVFZPUDByOGRVKzh2TDMzeXJtczV5R0JtK0ttblAwTVRPRG9nPT0.jpg?imageView&thumbnail=500x0&quality=96&stripmeta=0&type=jpg",
				loft.getImg());
		assertEquals("BMW",loft.getAuthor());
		assertEquals("鎴戝笇鏈涳紝聽姣忎竴涓椂鍒伙紝閮借薄褰╄壊铚＄瑪閭ｆ牱缇庝附銆偮犳垜甯屾湜锛屄犺兘鍦ㄥ績鐖辩殑鐧界焊涓婄敾鐢伙紝鐢诲嚭绗ㄦ嫏鐨勮嚜鐢憋紝鐢讳笅涓�鍙案杩滀笉浼氾紝娴佹唱鐨勭溂鐫涖�偮犅犅犅犅犅犅犅犅犅犅犫�斺�斺�斺�旈【鍩�", loft.getText());
		assertEquals("1d81a33a_affcc88", loft.getTid());
		assertEquals("http://68844335.lofter.com/post/1d81a33a_affcc88", loft.getRef());
	}

}
