package cn.edu.xmu.gxj.matchp.model;

import cn.edu.xmu.gxj.matchp.util.MPException;

/*
 * this is to describe matchp response object like index api and django response.
 */
public class Reply {
	private String Message;
	private int Code;
	
	public Reply(MPException e){
		this.Message = e.getMessage();
		this.Code = e.getCode();
	}
	
	public Reply(String message,int code){
		this.Message = message;
		this.Code = code;
	}
	
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public int getCode() {
		return Code;
	}
	public void setCode(int code) {
		Code = code;
	}
	
}
