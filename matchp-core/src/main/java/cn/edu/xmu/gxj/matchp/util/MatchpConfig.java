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

	public static void main(String[] args) throws FileNotFoundException, IOException{
		MatchpConfig config = new MatchpConfig();
		System.out.println(config.getEsClusterName());
		
	}
}
