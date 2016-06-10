package cn.edu.xmu.gxj.matchp.l2r;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.mockito.internal.matchers.And;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.features.FeatureManager;
import ciir.umass.edu.features.Normalizer;
import ciir.umass.edu.features.SumNormalizor;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.learning.RankerTrainer;
import ciir.umass.edu.metric.MetricScorerFactory;
import ciir.umass.edu.utilities.MyThreadPool;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class QueryRanker {
	
	
	private static Logger logger = LoggerFactory.getLogger(QueryRanker.class);
	
	private MatchpConfig config;
	
	private Ranker ranker;
	private String modelFile;
	private String trainFile;
	private String metric = "MAP";
	
	@PostConstruct
	public void init(){
		modelFile = "/origin.model";
		loadRanker();
	}
	


	
	public void train(){
		
		if (null == trainFile || !new File(trainFile).exists()) {
			logger.error("trainFile not exists {}",trainFile);
			return;
		}
				
		
		int nThread = Runtime.getRuntime().availableProcessors();
		MyThreadPool.init(nThread);
		
		List<RankList> train = FeatureManager.readInput(trainFile, false, false);	
		int[] features = FeatureManager.getFeatureFromSampleVector(train);
		
		boolean flag = true;
		if (flag) {
			Normalizer normalizer = new SumNormalizor();
			for(int i=0;i<train.size();i++)
				normalizer.normalize(train.get(i), features);
		}
		
		RankerTrainer trainer = new RankerTrainer();
		ranker = trainer.train(RANKER_TYPE.MART, train, null, features, new MetricScorerFactory().createScorer(metric));
		
		ranker.save(modelFile);
		
		MyThreadPool.getInstance().shutdown();
		
	}
	
	public double[] getPro(String[] test){
		int[] features = ranker.getFeatures();
		RankList testList = RankListBuilder.build(test);
		if (true) {
//			evaluator.getno.normalize(testList, features);
			Normalizer normalizer = new SumNormalizor();
			normalizer.normalize(testList, features);
		}
		
		double[] pros = new double[testList.size()];
		
		for (int i = 0; i < testList.size(); i++) {
			double pro = ranker.eval(testList.get(i));
			System.out.println(pro);
			pros[i] = pro;
		}
		
		return pros;
	}
	

	public void loadRanker(){
		String filepath = getPath(modelFile);
		if (!new File(filepath).exists()) {
			logger.error("modelFile not exists {}", filepath);
			return;
		}
		ranker = new RankerFactory().loadRankerFromFile(filepath);
	}

	
	public String getDate(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		return dateFormat.format(date);
	}

	private String getPath(String filename){
		URL url = getClass().getResource(filename);
		if (null != url) {
			logger.debug("the url path is {}", url.getPath());
			return url.getPath();
		}else {
			logger.error("the url path is null, please check the url,{}", filename);
			return "";
		}
	
	}
	


	public String getModelFile() {
		return modelFile;
	}




	public void setModelFile(String modelFile) {
		this.modelFile = modelFile;
	}




	public String getTrainFile() {
		return trainFile;
	}




	public void setTrainFile(String trainFile) {
		this.trainFile = trainFile;
	}
	
	
	
}
