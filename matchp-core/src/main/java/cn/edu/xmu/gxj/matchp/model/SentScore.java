package cn.edu.xmu.gxj.matchp.model;

/*
 * this is to describe sentiemnt score model responsed by django
 */
public class SentScore {
	private String Message;
	private String Code;
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	
}
