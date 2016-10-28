package com.hellostash.outputs;

import com.alibaba.fastjson.JSONObject;
import com.hellostash.BaseOutput;

public class Stdout extends BaseOutput{

	public Stdout(JSONObject config) {
		super(config);
	}

	@Override
	protected void init() {
		
	}

	@Override
	public void process(JSONObject event) {
		if(null != event) {
			System.out.println(event.toJSONString());
		} else {
			System.out.println("nothing!");
		}
	}

	@Override
	public void destory() {
		
	}

}
