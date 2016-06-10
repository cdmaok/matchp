package cn.edu.xmu.gxj.matchp.l2r;

import java.util.List;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.features.FeatureManager;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.learning.RankerTrainer;
import ciir.umass.edu.metric.ERRScorer;
import ciir.umass.edu.metric.MetricScorerFactory;
import ciir.umass.edu.utilities.MyThreadPool;
import groovy.util.Eval;

public class QueryRanker {
	
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
//		loadModel();
		debugTrain("","");
		System.out.println("hello");
	}
	
	public static void debugTrain(String trainFile,String modelFile){
		String metric = "MAP";
		Evaluator evaluator = new Evaluator(RANKER_TYPE.MART, metric, metric);
		
		int nThread = -1;
		if(nThread == -1)
			nThread = Runtime.getRuntime().availableProcessors();
		MyThreadPool.init(nThread);
		
		RankerFactory rFactory = new RankerFactory();
		rFactory.createRanker(RANKER_TYPE.MART).printParameters();
		
		trainFile = "../2016-06-09.dat";
		modelFile = "../2016-06-09.model";
		
		List<RankList> train = evaluator.readInput(trainFile);
		
		int[] features = FeatureManager.getFeatureFromSampleVector(train);
		
		boolean flag = true;
		if (flag) {
			evaluator.normalize(train,features);
		}
		
		RankerTrainer trainer = new RankerTrainer();
		Ranker ranker = trainer.train(RANKER_TYPE.MART, train, null, features, new MetricScorerFactory().createScorer(metric));
		
		ranker.save(modelFile);
		
		MyThreadPool.getInstance().shutdown();
		
	}
	
}
