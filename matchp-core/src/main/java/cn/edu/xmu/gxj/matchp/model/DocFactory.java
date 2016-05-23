package cn.edu.xmu.gxj.matchp.model;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.plugins.ImageSign;
import cn.edu.xmu.gxj.matchp.plugins.Sentiment;
import cn.edu.xmu.gxj.matchp.util.ErrCode;
import cn.edu.xmu.gxj.matchp.util.Fields;
import cn.edu.xmu.gxj.matchp.util.JsonUtility;
import cn.edu.xmu.gxj.matchp.util.MPException;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class DocFactory {

	@Autowired
	private MatchpConfig config;
	
	@Autowired
	private ImageSign signSever;
	
	@Autowired
	private Sentiment sentServer;
	
	public Doc Build(String json) throws MPException {
		Map<String, Object> map = JsonUtility.json2Map(json);
		return Build(map);
	}

	public Doc Build(Map<String, Object> map) throws MPException {
		checkMap(map);
		String text = (String) map.get(Fields.text);
		String img = (String) map.get(Fields.img);
		if (config.isCheck_img()) {
			checkImgExistAddSize(map);
		}
		if (config.isCheck_chatter()) {
			checkTextChatter(text);
		}
		addSentiment(map);
		addImgSignature(map);
		return new Doc(map);
	}

	/*
	 * check the input has the necessary field or not
	 */
	private  boolean checkMap(Map<String, Object> map) throws MPException {
		String[] checkList = new String[] { Fields.text, Fields.img, Fields.doc_id };
		for (int i = 0; i < checkList.length; i++) {
			String field = checkList[i];
			if (!map.containsKey(field)) {
				throw new MPException(ErrCode.Empty_Field, "no " + field + " founded ");
			}
		}
		return true;
	}

	private  boolean checkImgExistAddSize(Map<String, Object> map) throws MPException {
		String url = (String) map.get(Fields.img);
		if (url.trim().equals("")) {
			throw new MPException(ErrCode.Image_Not_Found, "Image Not found. url:" + url);
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			httpclient.close();
			int code = response.getStatusLine().getStatusCode();
			response.close();
			Pic pic = new Pic(url);
			double size = pic.getWidth() / pic.getHeight();
			map.put(Fields.imgSize, size);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			map.put(Fields.imgSign, 0);
			throw new MPException(ErrCode.Image_Not_Found, "Image Not found. url:" + url);
		}
	}
	
	public boolean checkTextChatter(String text) throws MPException{
//		if(false){
//			throw new MPException(ErrCode.Text_Chatter, "text is chatter. text:" + text);
//		}
		return false;
	}
	
	/*
	 * default is 0.5, range is [0,1]
	 */
	public void addSentiment(Map<String, Object> map){
		String text = (String) map.get(Fields.text);
		float polarity = sentServer.getSentiment(text);
		map.put(Fields.polarity, polarity);
	}
	
	/*
	 *  add img signature
	 *  wrong is "",otherwise is 8bit
	 */
	public void addImgSignature(Map<String, Object> map){
		String url = (String) map.get(Fields.img);
		String sign = signSever.getSignature(url);
		map.put(Fields.imgSign, sign);
	}
	
	
	
	
}
