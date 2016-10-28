package com.hellostash;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseFilter extends BaseConfig {

	public BaseFilter(JSONObject config) {
		super(config);
	}
	
	public boolean process(JSONObject event) {
		return filter(event);
	}

	protected void prepare() {}

	protected abstract boolean filter(JSONObject event);

}
