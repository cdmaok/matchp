package cn.edu.xmu.gxj.matchp.score;

import java.util.Map;

import javax.xml.soap.Text;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class EntryBuilder {
	
	@Autowired
	private MatchpConfig config;
	
	@Autowired
	private Sentiment sent;

	public  Entry buildEntry(String query,Map<String, Object> map, float hit){
		
		double sentiScore = 0;
		if (config.isSentiment_enable()) {
			double querySenti = sent.getSentiment(query);
			double resultSenti = (double) map.get(Fields.polarity);
			sentiScore = calSentiment(querySenti, resultSenti);
		}
		String text = (String) map.get(Fields.text);
		String url = (String) map.get(Fields.url);
		double score = hit + sentiScore;
		return new Entry(text, url, score);
	}
	
	public double calSentiment(double query,double result){
		return 1 - Math.abs(query - result);
	}
	
}
