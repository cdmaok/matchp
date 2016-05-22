package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.MPException;

public class DocTest {
	

	@Test
	public void test_unfitable() {
		try {
			String no_doc_id = "{\"text\":\"abc\"}";
			Doc doc = new Doc(no_doc_id);
			fail("wrong doc string");
		} catch (MPException e) {
			e.printStackTrace();
			assertEquals(ErrCode.Empty_Field, e.getCode());
		}
		
		try {
			String no_img = "{\"text\":\"abc\",\"doc_id\":\"123\"}";
			Doc doc = new Doc(no_img);
			fail("wrong doc string");
		} catch (MPException e) {
			e.printStackTrace();
			assertEquals(ErrCode.Empty_Field, e.getCode());
		}
		
		try {
			String notext = "{\"doc_id\":\"abc\",\"img\":\"abc\"}";
			Doc doc = new Doc(notext);
			fail("wrong doc string");
		} catch (MPException e) {
			e.printStackTrace();
			assertEquals(ErrCode.Empty_Field, e.getCode());
		}
		
	}

}
