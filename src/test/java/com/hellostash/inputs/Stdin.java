package com.hellostash.inputs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.alibaba.fastjson.JSONObject;
import com.hellostash.BaseFilter;
import com.hellostash.BaseInput;
import com.hellostash.BaseOutput;

public class Stdin extends BaseInput{

	public Stdin(JSONObject inputConfig, BaseFilter[] filters, BaseOutput[] outputs) {
		super(inputConfig, filters, outputs);
	}

	@Override
	public void process() {
		try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String input;

            while ((input = br.readLine()) != null) {
                flush(input);
            }

        } catch (IOException io) {
            io.printStackTrace();
        }
	}

	@Override
	protected void init() {
		
	}

}
