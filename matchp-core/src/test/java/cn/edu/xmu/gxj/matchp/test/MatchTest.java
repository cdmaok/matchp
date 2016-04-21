package cn.edu.xmu.gxj.matchp.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchTest {

	public static void main(String[] args) {

		String text = "http://xx http://abc.jpg";
		
		Pattern pattern = Pattern.compile("http://[^(http://)]*jpg");
		
		Matcher matcher = pattern.matcher(text);
		
		while(matcher.find()){
			System.out.println(matcher.group());
		}
		
//		matcher.find(); // ABAbc
//		int start = matcher.start();
//		matcher.find(start + 1);
//		System.out.println(matcher.group());
	}

}
