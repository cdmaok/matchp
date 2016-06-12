package cn.edu.xmu.gxj.matchp.score;

import java.util.Comparator;

import cn.edu.xmu.gxj.matchp.model.Entry;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;

public class ScoreComparator implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		double one;
		double other;
		try {
			one = JsonUtility.getAttributeasDouble(arg0, Fields.score);
			other = JsonUtility.getAttributeasDouble(arg1, Fields.score);
			if(other > one){
				return 1;
			}else if (other == one) {
				return 0;
			}else {
				return -1;
			}
			
		} catch (MPException e) {
			e.printStackTrace();
			return 0;
		}

	}

}
