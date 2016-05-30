package cn.edu.xmu.gxj.matchp.score;

import java.util.Map;

import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class EntryBuilder {
	
	@Autowired
	private MatchpConfig config;
	
	private final String TypeScore = "TypeScore";
	private final String SentiScore = "SentiScore";
	private final String IrScore = "IrScore";
	private final String FinScore = "FinScore";
	

	public  Entry buildEntry(double querySenti,SearchHit hit){
		Map<String, Object> map = calScore(querySenti, hit);
		String text = (String) map.get(Fields.text);
		String url = (String) map.get(Fields.img);
		return new Entry(text, url, (double) map.get(FinScore));
	}
	
	
	public  String buildJson(double querySenti,SearchHit hit){
		Map<String, Object> map = calScore(querySenti, hit);
		return new Gson().toJson(map);
	}
	
	public Map<String, Object> calScore(double querySenti,SearchHit hit){
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
			break;
		default:
			typeScore = 0;
			break;
		}
		
		
		if (config.isSentiment_enable()) {
			double resultSenti = (double) map.get(Fields.polarity);
			sentiScore = calSentiment(querySenti, resultSenti);
		}
		

		double score = irScore + sentiScore + typeScore;
		
		
		//TODO: may be change to constant field
		map.put(TypeScore, typeScore);
		map.put(IrScore, irScore);
		map.put(SentiScore, sentiScore);
		map.put(FinScore, score);
		
		return map;
	}
	
	public double calSentiment(double query,double result){
		return 1 - Math.abs(query - result);
	}
	
}
