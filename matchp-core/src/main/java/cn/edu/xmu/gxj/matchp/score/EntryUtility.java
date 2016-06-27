package cn.edu.xmu.gxj.matchp.score;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

@Component
public class EntryUtility {
	
	
	private final String SentiScore = "SentiScore";
	private final String IrScore = "IrScore";

	
	

	public  Entry buildEntry(double querySenti,int sar, SearchHit hit){
		Map<String, Object> map = calScore(querySenti, sar , hit);
		String text = (String) map.get(Fields.text);
		String url = (String) map.get(Fields.img);
		return new Entry(text, url, MapUtils.getDoubleValue(map, Fields.score, 0));
	}
	
	public static ArrayList<Entry> buildEntryArray(ArrayList<String> stringArray) throws MPException{
		ArrayList<Entry> entrys = new ArrayList<>();
		for(int i = 0; i < stringArray.size(); i ++){
			String string = stringArray.get(i);
			
			String text = JsonUtility.getAttributeasStr(string, Fields.text);
			String url = JsonUtility.getAttributeasStr(string, Fields.img);
			double score = JsonUtility.getAttributeasDouble(string, Fields.score);
			
			//TODO : may improve speed here.
			Entry entry = new Entry(text, url, score);
			entrys.add(entry);
		}
		return entrys;
	}
	
	public  String buildJson(double querySenti,int sarLabel,SearchHit hit){
		Map<String, Object> map = calScore(querySenti,sarLabel, hit);
		return new Gson().toJson(map);
	}
	
	public Map<String, Object> calScore(double querySenti,int sarLabel,SearchHit hit){
		Map<String, Object> map = hit.getSource();
		double irScore = hit.getScore();
		double sentiScore = 0;

		
		StringBuffer vector = new StringBuffer();

		double sizeScore = MapUtils.getDoubleValue(map, Fields.imgSize, 0);

		double resultSenti = MapUtils.getDoubleValue(map, Fields.polarity, 0);
		sentiScore = calSentiment(querySenti, resultSenti);
		
		int type = MapUtils.getIntValue(map, Fields.type, 0);
		
		int socialScore = MapUtils.getIntValue(map, Fields.SOSCORE_FIELD, 0);
		
		int resultSAR = MapUtils.getIntValue(map, Fields.SAR_FIELD,0);
		int sar = sar(sarLabel, resultSAR);
		
		String[] ocrs = MapUtils.getString(map, Fields.OCR_FIELD, "0,0").split(",");
		
		String[] hists = MapUtils.getString(map, Fields.HIST_FIELD,"0,0,0").split(",");
		
		vector.append(" 1:");
		vector.append(sizeScore);
		vector.append(" 2:");
		vector.append(sentiScore);
		vector.append(" 3:");
		vector.append(irScore);
		vector.append(" 4:");
		vector.append(type);
		vector.append(" 5:");
		vector.append(socialScore);
		vector.append(" 6:");
		vector.append(sar);
		vector.append(" 7:");
		vector.append(ocrs[0]);
		vector.append(" 8:");
		vector.append(ocrs[1]);
		vector.append(" 9:");
		vector.append(hists[0]);
		vector.append(" 10:");
		vector.append(hists[1]);
		vector.append(" 11:");
		vector.append(hists[2]);
		
//		vector = "1 qid:1 1:" + sizeScore + " 2:" + sentiScore + " 3:" + irScore + " 4:" + type + " 5:" + socialScore + " 6:" + ;
		
		//TODO: may be change to constant field
		map.put(IrScore, irScore);
		map.put(Fields.FeatureVector, vector.toString());
		
		return map;
	}
	
	public double calSentiment(double query,double result){
		return 1 - Math.abs(query - result);
	}
	
	public int sar(int query,int result){
		return Math.abs(query - result);
	}
	
}
