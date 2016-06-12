package cn.edu.xmu.gxj.matchp.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonUtilityTest {

	@Test
	public void testSetAttribute() {
		String json = "{\"abc\":123}";
		String jsonafter = "{\"abc\":123,\"def\":123.0}";
		
		String newJson;
		try {
			newJson = JsonUtility.setAttribute(json, "def", 123.0f);		
			System.out.println(newJson);
			assertTrue(newJson.equals(jsonafter));
			assertEquals(newJson, jsonafter);
		} catch (MPException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

}
