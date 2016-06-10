package cn.edu.xmu.gxj.matchp.l2r;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.utilities.MyThreadPool;

public class L2R {
	
	public static void loadModel(){
		RankerFactory rFact = new RankerFactory();
		rFact.loadRankerFromFile("../2016-06-09.model");
		
	}
	
	public static void train(String file){
		String[] paras = new String[]{"-train","../2016-06-09.dat","-ranker","0","-save","../2016-06-09.model"};
		Evaluator.main(paras);
//		Evaluator e = new Evaluator(rt, metric, k)
	}
	
	public static void test(String file){
		String[] paras = new String[]{"-load","../2016-06-09.model","-rank","../2016-06-09.test","-score","../2016-06-09.score"};
		Evaluator.main(paras);
	}

	public static void main(String[] args){
//		train("");
//		test("");
		loadModel();
		System.out.println("hello");
	}
	
	public void debug(){
		String modelFile = "xxx";
		String rankFIle = "xx";
		String scoreFile = "xx";
		int nThread = -1;
		if(nThread == -1)
			nThread = Runtime.getRuntime().availableProcessors();
		MyThreadPool.init(nThread);
		
	}
	
}
