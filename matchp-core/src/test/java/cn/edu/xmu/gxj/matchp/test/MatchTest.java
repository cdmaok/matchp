package cn.edu.xmu.gxj.matchp.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchTest {
	public static void main(String[] args) {

//		String text = "abchttp://ww1.sinaimg.cn/wap128/a033dfefjw1efzm1oluakj20dc0hstae.jpg";
		String text = "htpp://abcdhttp://www1.cn/abc.jpg";
		
		Pattern pattern = Pattern.compile("http://[^:]*.(jpg)");
		
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
