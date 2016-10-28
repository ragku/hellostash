package com.hellostash;

import com.alibaba.fastjson.JSONObject;
import com.hellostash.decode.JsonDecoder;
import com.hellostash.decode.PlainDecoder;

public class StashContext {
	
	protected IDecode decoder;
    protected BaseFilter[] filters;
    protected BaseOutput[] outputs;

    public StashContext(String codec, BaseFilter[] filters, BaseOutput[] outputs) {
    	if(null != codec && codec.equalsIgnoreCase("plain")) {
    		decoder = new PlainDecoder();
    	} else {
    		decoder = new JsonDecoder();
    	}
    	
    	this.filters = filters;
    	this.outputs = outputs;
    	
    	Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (BaseOutput bo : outputs) {
                    bo.destory();
                }
            }
        });
    }
    
    public void process(String data) {
    	JSONObject json = decoder.decode(data);
    	for(BaseFilter bf : filters) {
    		if(!bf.process(json)) {
    			return;
    		}
    	}
    	for(BaseOutput bo : outputs) {
    		bo.process(json);
    	}
    }
    
    protected void registerShutdownHook(final BaseOutput[] bos) {
        
    }
}
