package com.hellostash.decode;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hellostash.IDecode;
import com.hellostash.Logger;

public class JsonDecoder implements IDecode {
	
    @Override
    public JSONObject decode(final String message) {
        JSONObject event = null;
        try {
            event = JSONObject.parseObject(message);
        } catch (Exception e) {
        	Logger.hello.error("can not convert message to json: " + message);
        }

        if (event == null) {
        	event = new JSONObject();
        }
        event.put("message", message);
    	event.put("@timestamp", new Date());
        return event;
    }
}
