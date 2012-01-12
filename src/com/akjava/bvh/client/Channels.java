/*
 * Copyright (C) 2011 aki@akjava.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.akjava.bvh.client;

public class Channels {
private boolean Xrotation,Yrotation,Zrotation,Xposition,Yposition,Zposition;

public static final int XPOSITION=0;
public static final int YPOSITION=1;
public static final int ZPOSITION=2;
public static final int XROTATION=3;
public static final int YROTATION=4;
public static final int ZROTATION=5;
private String order=""; //TODO merge string
private String text="";
public String getOrder() {
	return order;
}

public boolean isXrotation() {
	return Xrotation;
}

public void addOrder(String ch){
	order+=ch;
}

public void setXrotation(boolean xrotation) {
	Xrotation = xrotation;
	text+="Xrotation ";
}

public boolean isYrotation() {
	return Yrotation;
}

public void setYrotation(boolean yrotation) {
	Yrotation = yrotation;
	text+="Yrotation ";
}

public boolean isZrotation() {
	return Zrotation;
}

public void setZrotation(boolean zrotation) {
	Zrotation = zrotation;
	text+="Zrotation ";
}

public boolean isXposition() {
	return Xposition;
}

public void setXposition(boolean xposition) {
	Xposition = xposition;
	text+="Xposition ";
}

public boolean isYposition() {
	return Yposition;
}

public void setYposition(boolean yposition) {
	Yposition = yposition;
	text+="Yposition ";
}

public boolean isZposition() {
	return Zposition;
}

public void setZposition(boolean zposition) {
	Zposition = zposition;
	text+="Zposition ";
}
public String toString(){
	int size=0;
	//String text="";
	if(Xposition){
		size++;
	
	}
	if(Yposition){
		size++;
		
	}
	if(Zposition){
		size++;
	
	}if(Xrotation){
		size++;
		
	}
	if(Yrotation){
		size++;
		
	}
	if(Zrotation){
		size++;

	}
	if(text.isEmpty()){
		return "CHANNELS 0";
	}else{
		return "CHANNELS "+size+" "+text.substring(0,text.length()-1);
	}
}
}
