package cn.edu.xmu.gxj.matchp.score;

import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class EntryBuilder {
	
	@Autowired
	private MatchpConfig config;
	

	public  Entry buildEntry(double querySenti,SearchHit hit){
		Map<String, Object> map = hit.getSource();
		double irScore = hit.getScore();
		double sentiScore = 0;
		double typeScore = 0;
		switch (hit.getType()) {
		case "loft":
			typeScore = 1;
			break;
		case "weibo":
			typeScore = 0.5;
		default:
			typeScore = 0;
			break;
		}
		
		if (config.isSentiment_enable()) {
			double resultSenti = (double) map.get(Fields.polarity);
			sentiScore = calSentiment(querySenti, resultSenti);
		}
		
		String text = (String) map.get(Fields.text);
		String url = (String) map.get(Fields.img);
		double score = irScore + sentiScore + typeScore;
		return new Entry(text, url, score);
	}
	
	public double calSentiment(double query,double result){
		return 1 - Math.abs(query - result);
	}
	
}
