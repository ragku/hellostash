package com.hellostash;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseConfig {
	
	private JSONObject config = new JSONObject();
	
	public BaseConfig(JSONObject config) {
		if(null != config) {
			this.config.putAll(config);
		}
		init();
	}
	
	protected abstract void init();
	
	protected <T> T getFilterConfig(Class<T> clazz) {
		return config.toJavaObject(clazz);
	}
	
	protected JSONObject getConfig() {
		return config;
	}

}
