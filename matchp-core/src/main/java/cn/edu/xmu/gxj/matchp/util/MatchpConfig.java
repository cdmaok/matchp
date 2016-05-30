package cn.edu.xmu.gxj.matchp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MatchpConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(MatchpConfig.class);
	private static Properties properties;
	private static final String configFileName = "/config.properties";
	
	private final String esClusterName;
	private final String esHostName;
	private final long esTimeout;
	
	private final boolean check_img;
	private final boolean check_chatter;
	
	private final boolean sentiment_enable;
	private final String sentiment_server;
	
	private final boolean image_sign_enable;
	private final String image_sign_server;
	
	private final String querySet;
	private final String labelFile;
	
	public MatchpConfig() {
		try {
			String filepath = getPath();
			File configFile = new File(filepath);
			if(!(configFile.exists()&&configFile.isFile())){
				logger.info("reading config file {} not exists. using the default property",filepath);
			}else{
				logger.info("reading file from {}", filepath);
				Properties properties = new Properties();
				InputStream configStream = new FileInputStream(configFile);
				properties.load(configStream);
				this.properties = properties;
				configStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.esClusterName = configOrDefault("ES_CLUSTER_NAME", "locales");
		this.esTimeout = Long.parseLong(configOrDefault("ES_TIMEOUT", "5000"));
		this.esHostName = configOrDefault("ES_HOESTNAME", "localhost");
		
		// add check_img and check_chatter
		this.check_img = Boolean.parseBoolean(configOrDefault("WEIBO_CHECK_IMG", "true"));
		this.check_chatter = Boolean.parseBoolean(configOrDefault("WEIBO_CHECH_CHATTER", "true"));
		
		this.sentiment_enable = Boolean.parseBoolean(configOrDefault("SENTIMENT_ENABLE", "true"));
		this.sentiment_server = configOrDefault("SENTIMENT_IP", "localhost:8000");
		
		this.image_sign_enable = Boolean.parseBoolean(configOrDefault("IMAGE_SIGN_ENABLE", "true"));
		this.image_sign_server = configOrDefault("IMAGE_SIGN_IP", "localhost:8000");
		
		this.querySet = configOrDefault("QUERY_SET_FILE", "~/querySet");
		this.labelFile = configOrDefault("LABELED_PAIR_FILE", "~/labelFIle");
	}
	
	private String getPath(){
		URL url = getClass().getResource(configFileName);
		if (null != url) {
			logger.debug("the url path is {}", url.getPath());
			return url.getPath();
		}else {
			logger.error("the url path is null, please check the url");
			return "";
		}
	
	}
	
	private static String configOrDefault(String key, String defaultValue){
		if(properties != null && properties.containsKey(key)){
			return properties.getProperty(key);
		}
		return defaultValue;
	}
	


	public String getEsClusterName() {
		return esClusterName;
	}
	

	
	public long getEsTimeout(){
		return esTimeout;
	}
	
	

	public String getEsHostName() {
		return esHostName;
	}

	public boolean isCheck_img() {
		return check_img;
	}

	public boolean isCheck_chatter() {
		return check_chatter;
	}

	public boolean isSentiment_enable() {
		return sentiment_enable;
	}

	public String getSentiment_server() {
		return sentiment_server;
	}

	public boolean isImage_sign_enable() {
		return image_sign_enable;
	}

	public String getImage_sign_server() {
		return image_sign_server;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException{
		MatchpConfig config = new MatchpConfig();
		System.out.println(config.getEsClusterName());
		
	}

	public String getQuerySet() {
		return querySet;
	}

	public String getLabelFile() {
		return labelFile;
	}
	
	
}
