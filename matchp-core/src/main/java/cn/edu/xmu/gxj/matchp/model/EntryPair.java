package cn.edu.xmu.gxj.matchp.model;

import java.util.ArrayList;

import com.google.gson.Gson;

import cn.edu.xmu.gxj.matchp.score.EntryUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

public class EntryPair {

	private String query;
	private String id;
	private ArrayList<String> entrys;
	private Integer answer;
	
	public EntryPair(String query, String id, ArrayList<String> entrys) {
		super();
		this.query = query;
		this.entrys = entrys;
		this.id = id;
	}
	
	public EntryPair simpleFormat() throws MPException{
		ArrayList<Entry> entryArray = EntryUtility.buildEntryArray(entrys);
		ArrayList<String> newentrys = new ArrayList<>();
		newentrys.add(new Gson().toJson(entryArray.get(0)));
		newentrys.add(new Gson().toJson(entryArray.get(1)));
		return new EntryPair(query, id, newentrys);
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public ArrayList<String> getEntrys() {
		return entrys;
	}
	public void setEntrys(ArrayList<String> entrys) {
		this.entrys = entrys;
	}

	public Integer getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAnswer(Integer answer) {
		this.answer = answer;
	}
	
}
