package cn.edu.xmu.gxj.matchp.l2r;

import java.util.ArrayList;
import java.util.List;

import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.DenseDataPoint;
import ciir.umass.edu.learning.RankList;

public class RankListBuilder {

	public static RankList  build(String[] strings){
		List<DataPoint> dps = new ArrayList<DataPoint>();
		for(String dpstr: strings){
			DataPoint dp = new DenseDataPoint(dpstr);
			dps.add(dp);
		}
		return new RankList(dps);
	}
}
