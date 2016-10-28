package com.hellostash.decode;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.hellostash.IDecode;

public class PlainDecoder implements IDecode {

	@Override
	public JSONObject decode(final String message) {
		JSONObject event = new JSONObject();
		event.put("message", message);
		event.put("@timestamp", new Date());
		return event;
	}
}
