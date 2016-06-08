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
	private final String SizeScore = "SizeScore";
	
	private double[] weights = new double[]{0.99569356, -0.16700651, 0.78396082 , -0.13378303,0.0015323567};
	

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
		double socialScore = 0;

		double[] typeAndsocial = calSocialScore(hit);
		typeScore =  typeAndsocial[0];
		socialScore = typeAndsocial[1];
		
		if (config.isSentiment_enable()) {
			double resultSenti = (double) map.get(Fields.polarity);
			sentiScore = calSentiment(querySenti, resultSenti);
		}

		double sizeScore = (double) map.get(Fields.imgSize);

		//TODO: change the calculation
		double score = weights[0]* sizeScore + weights[1]* sentiScore + weights[2] * irScore + weights[3] * typeScore + weights[4] *socialScore;
		
		
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
	
	public double[] calSocialScore(SearchHit hit){
		double typeScore,socialScore;
		Map<String, Object> map = hit.getSource();
		switch (hit.getType()) {
		case "loft":
			typeScore = 1;
			socialScore = (Integer)map.get(Fields.loft_comments) + (Integer)map.get(Fields.loft_hot);
			break;
		case "weibo":
			typeScore = 0.5;
			socialScore = (Integer)map.get(Fields.weibo_comments) + (Integer)map.get(Fields.weibo_goods) + (Integer)map.get(Fields.weibo_repost);
			break;
		case "tumblr":
			typeScore = 2;
			socialScore = (Integer) map.get(Fields.tumblr_hot);
			break;
		default:
			typeScore = 0;
			socialScore = 0;
			break;
		}
		return new double[]{typeScore,socialScore};
	}
	
}
