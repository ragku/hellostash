package com.hellostash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class AppContext {

	private final Set<Class<?>> classPool = new HashSet<>();
	private final Map<Class<?>, JSONObject> inputMap = new HashMap<>();
	private final Map<Class<?>, JSONObject> filterMap = new HashMap<>();
	private final Map<Class<?>, JSONObject> outputMap = new HashMap<>();
	private final JSONObject config;

	public AppContext(String fileName) {

		String configStr = getConfigStr(fileName);
		config = JSONObject.parseObject(configStr);

		JSONArray packages = config.getJSONArray("packages");
		if (null == packages || packages.isEmpty()) {
			return;
		}

		for(Object obj : packages) {
			try {
				Set<Class<?>> tmps = ClassScanUtil.listClasses(obj.toString());
				if (null == tmps || tmps.isEmpty()) {
					continue;
				}
				for(Class<?> clazz : tmps) {
					Class<?> parent = clazz.getSuperclass();
					if(null == parent) {
						continue;
					}
					if(parent.equals(BaseInput.class)
						|| parent.equals(BaseFilter.class)
						|| parent.equals(BaseOutput.class)) {
						classPool.add(clazz);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (classPool.isEmpty()) {
			// nothing to do
			return;
		}
		
		JSONObject inputConfigs = config.getJSONObject("inputs");
		if (null != inputConfigs && !inputConfigs.isEmpty()) {
			for(String key : inputConfigs.keySet()) {
				Class<?> clazz = getClassByName(key, BaseInput.class);
				if(null != clazz) {
					if(inputConfigs.get(key) instanceof JSONObject) {
						inputMap.put(clazz, inputConfigs.getJSONObject(key));
					} else {
						inputMap.put(clazz, new JSONObject());
					}
				}
			}
		}

		JSONObject outputConfigs = config.getJSONObject("outputs");
		if (null != outputConfigs && !outputConfigs.isEmpty()) {
			for(String key : outputConfigs.keySet()) {
				Class<?> clazz = getClassByName(key, BaseOutput.class);
				if(null != clazz) {
					if(outputConfigs.get(key) instanceof JSONObject) {
						outputMap.put(clazz, outputConfigs.getJSONObject(key));
					} else {
						outputMap.put(clazz, new JSONObject());
					}
				}
			}
		}
		
		JSONObject filterConfigs = config.getJSONObject("filters");
		if (null != filterConfigs && !filterConfigs.isEmpty()) {
			for(String key : filterConfigs.keySet()) {
				Class<?> clazz = getClassByName(key, BaseFilter.class);
				if(null != clazz) {
					if(filterConfigs.get(key) instanceof JSONObject) {
						filterMap.put(clazz, filterConfigs.getJSONObject(key));
					} else {
						filterMap.put(clazz, new JSONObject());
					}
					
				}
			}
		}
		
		inputMap.forEach((inputClass, inputConfig) -> {
			try {
				BaseInput bf = (BaseInput) inputClass
						.getConstructor(JSONObject.class, BaseFilter[].class, BaseOutput[].class)
						.newInstance(inputConfig, instantFilter(),  instantOutput());
				bf.process();;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		});
	}
	
	private BaseOutput[] instantOutput() throws Exception {
		BaseOutput[] outputs = new BaseOutput[outputMap.size()];
		int i = 0;
		for(Class<?> clazz : outputMap.keySet()) {
			outputs[i] = (BaseOutput) clazz.getConstructor(JSONObject.class).newInstance(outputMap.get(clazz));
			i++;
		}
		return outputs;
	}
	
	private BaseFilter[] instantFilter() throws Exception {
		BaseFilter[] filters = new BaseFilter[filterMap.size()];
		int i = 0;
		for(Class<?> clazz : filterMap.keySet()) {
			filters[i] = (BaseFilter) clazz.getConstructor(JSONObject.class).newInstance(filterMap.get(clazz));
			i++;
		}
		return filters;
	}
	
	private Class<?> getClassByName(String className, Class<?> clazz) {
		for(Class<?> c: classPool) {
			if(c.getName().lastIndexOf(className) != -1 && c.getSuperclass().equals(clazz)) {
				return c;
			}
		}
		return null;
	}

	@SuppressWarnings("resource")
	private String getConfigStr(String filename) {
		StringBuilder sb = new StringBuilder();
		try {
			FileInputStream input = new FileInputStream(new File(filename));
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception e) {
			Logger.hello.error("read file:" + filename + "error!", e);
		}
		return sb.toString();
	}
}
