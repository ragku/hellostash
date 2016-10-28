package com.hellostash;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseOutput extends BaseConfig {
	
	protected JSONObject outPutConfig = new JSONObject();

	public BaseOutput(JSONObject config) {
		super(config);
	}

	protected abstract void init();
	
	public abstract void process(JSONObject event);
	
	public abstract void destory();
}
