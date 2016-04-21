package cn.edu.xmu.gxj.matchp.model;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchTest {
	
	public static void main(String[] args) {
		
		Pattern pattern;
		Matcher matcher;
		
		String text = "http://t.cn/8FnP0Mnhttp://ww1.sinaimg.cn/wap128/6b63135ajw1edtthpmwb9j20z00z0kbx.jpg";
		
		// Greedy quantifiers
		
		pattern = Pattern.compile("http.*jpg"); // Greedy
		matcher = pattern.matcher(text);
		
		// attempt to find the next subsequence of the input sequence that matches the pattern
		matcher.find();
		System.out.println(matcher.group());
		
		pattern = Pattern.compile("http.+jpg");  // Greedy
		matcher = pattern.matcher(text);
		
		// attempt to find the next subsequence of the input sequence that matches the pattern
		matcher.find();
		System.out.println(matcher.group());
		
		// Nongreedy quantifiers
		System.out.println("=====aaa====");
		pattern = Pattern.compile("http://(.*?).jpg");  // Non-Greedy
		matcher = pattern.matcher(text);
		
		while (matcher.find()) {
			// attempt to find the next subsequence of the input sequence that matches the pattern
			System.out.println(matcher.group());
		}
		
		System.out.println("====aa======");
		
		pattern = Pattern.compile("http.+?jpg");  // Non-Greedy
		matcher = pattern.matcher(text);

		// attempt to find the next subsequence of the input sequence that matches the pattern
		matcher.find();
		System.out.println(matcher.group());
		
		// Nongreedy quantifiers
		
		pattern = Pattern.compile("A(.*?)c");  // Non-Greedy
		matcher = pattern.matcher("ABcAbc");
		
		// attempt to find the next subsequence of the input sequence that matches the pattern
//		matcher.find();
//		System.out.println(matcher.groupCount());
//		System.out.println(matcher.group());
//		int start = matcher.start();
//		matcher.find(start + 1);
//		System.out.println(matcher.group());
		System.out.println("==============");
		while(matcher.find()){
			System.out.println(matcher.group());
		}
		
		
	}
		
}