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
	
	private final String esPath;
	private final String esClusterName;
	
	public MatchpConfig() {
		try {
			String filepath = getPath();
			File configFile = new File(filepath);
			if(!configFile.exists()){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.esPath = configOrDefault("ES_PATH_HOME", "./");
		this.esClusterName = configOrDefault("ES_CLUSTER_NAME", "locales");
	}
	
	public String getPath(){
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	System.out.println(url.getFile());
        }
		URL url = getClass().getResource(configFileName);
		logger.debug("the url path is {}",url.getPath());
		return url.getPath();
	
	}
	
	public static String configOrDefault(String key, String defaultValue){
		if(properties != null && properties.containsKey(key)){
			return properties.getProperty(key);
		}
		return defaultValue;
	}

	public String getEsPath() {
		return esPath;
	}

	public String getEsClusterName() {
		return esClusterName;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		MatchpConfig config = new MatchpConfig();
		System.out.println(config.getEsClusterName());
		
	}
}
