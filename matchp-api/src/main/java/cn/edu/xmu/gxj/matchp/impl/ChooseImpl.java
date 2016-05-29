package cn.edu.xmu.gxj.matchp.impl;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.edu.xmu.gxj.matchp.api.ChooseAPI;
import cn.edu.xmu.gxj.matchp.util.MatchpConfig;

@Component
public class ChooseImpl implements ChooseAPI{
	
	@Autowired
	private MatchpConfig config;

	@Override
	public Response Choose() {
		return null;
	}

	@Override
	public Response Prefer(String type) {
		return null;
	}

}
