package cn.edu.xmu.gxj.matchp.model;

import java.util.List;

public class Page<T> {
	private int size;
	private int current;
	private int totalPage;
	private int total;
	private List<T> entrys;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getEntrys() {
		return entrys;
	}
	public void setEntrys(List<T> entrys) {
		this.entrys = entrys;
	}
	public Page(int size, int current, int totalPage, int total, List<T> entrys) {
		super();
		this.size = size;
		this.current = current;
		this.totalPage = totalPage;
		this.total = total;
		this.entrys = entrys;
	}
	
	
	
	
}
