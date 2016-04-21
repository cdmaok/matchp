package cn.edu.xmu.gxj.matchp.util;

public class MPException extends Exception{
	private int Code;
	private String Message;
	
	
	public MPException(int code, String message) {
		super();
		Code = code;
		Message = message;
	}
	
	public int getCode() {
		return Code;
	}
	public void setCode(int code) {
		Code = code;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	
	

}
