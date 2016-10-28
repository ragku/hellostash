package com.hellostash.filters;

import com.alibaba.fastjson.JSONObject;
import com.hellostash.BaseFilter;

public class OneLevelFilter extends BaseFilter{

	public OneLevelFilter(JSONObject config) {
		super(config);
	}

	@Override
	protected boolean filter(JSONObject event) {
		if(null == event || event.isEmpty()) {
			return false;
		}
		
		JSONObject child = new JSONObject();
		for(String key : event.keySet()) {
			if(event.get(key) instanceof JSONObject) {
				Child(event.getJSONObject(key), child);
				event.remove(key);
			}
		}
		event.putAll(child);
		
		return true;
	}

	@Override
	protected void init() {
	}
	
	private void Child(JSONObject json, JSONObject result) {
		for(String key : json.keySet()) {
			if(json.get(key) instanceof JSONObject) {
				Child(json.getJSONObject(key), result);
			} else {
				result.put(key, json.get(key));
			}
		}
	}
}
