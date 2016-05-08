package cn.edu.xmu.gxj.matchp.score;

import java.util.Comparator;

import cn.edu.xmu.gxj.matchp.model.Entry;

public class ScoreComparator implements Comparator<Entry> {

	@Override
	public int compare(Entry arg0, Entry arg1) {
		return (int) (arg1.getScore() - arg0.getScore());
	}

}
