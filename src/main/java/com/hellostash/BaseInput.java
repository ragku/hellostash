package com.hellostash;

import com.alibaba.fastjson.JSONObject;

public abstract class BaseInput extends BaseConfig {

    protected StashContext stashContext;

	public BaseInput(JSONObject inputConfig, BaseFilter[] filters, BaseOutput[] outputs) {
		super(inputConfig);
        this.stashContext = new StashContext(getConfig().getString("codec"), filters, outputs);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	deploy();
            }
        });
    }
	
	protected void deploy() {}
	
	public void flush(String data) {
		stashContext.process(data);
	}

    protected abstract void process();
}
