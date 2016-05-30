package cn.edu.xmu.gxj.matchp.model;

import java.util.ArrayList;

public class EntryPair {

	private String query;
	private ArrayList<String> entrys;
	private Integer answer;
	
	public EntryPair(String query, ArrayList<String> entrys) {
		super();
		this.query = query;
		this.entrys = entrys;
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
	
	
	
}
