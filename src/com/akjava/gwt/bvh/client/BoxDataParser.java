package com.akjava.gwt.bvh.client;

import java.util.HashMap;
import java.util.Map;



public class BoxDataParser {

	public Map<String,BoxData> parse(String text){
		Map<String,BoxData> map=new HashMap<String, BoxData>();
		
		text=text.replace("\r", "");
		String[] lines=text.split("\n");
		for(String line:lines){
			if(line.isEmpty()){
				continue;
			}
			String[] csv=line.split(",");
			BoxData data=new BoxData();
			data.setName(csv[0]);
			
			data.setScaleX(Double.parseDouble(csv[1]));
			data.setScaleY(Double.parseDouble(csv[2]));
			data.setScaleZ(Double.parseDouble(csv[3]));
			if(csv.length>4){//Z
				data.setRotateZ(Double.parseDouble(csv[4]));
			}
			map.put(data.getName(), data);
		}
		return map;
	}
}
