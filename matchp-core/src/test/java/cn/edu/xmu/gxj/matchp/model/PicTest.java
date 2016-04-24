package cn.edu.xmu.gxj.matchp.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

public class PicTest {

	@Test
	public void testfail() {
		try {
			Pic pic = new Pic("wtf");
			fail("improper url");
		} catch (Exception e) {
			return;
		} 
	}
	
	@Test
	public void testsucc() {
		try {
			Pic pic = new Pic("http://7xpv97.com1.z0.glb.clouddn.com/break_the_wall_01.jpg");
			assertEquals(241, pic.getHeight(), 1);
			assertEquals(338, pic.getWidth(), 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}

}
