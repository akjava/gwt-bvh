package com.akjava.bvh.client;

public class Channels {
private boolean Xrotation,Yrotation,Zrotation,Xposition,Yposition,Zposition;

public static final int XPOSITION=0;
public static final int YPOSITION=1;
public static final int ZPOSITION=2;
public static final int XROTATION=3;
public static final int YROTATION=4;
public static final int ZROTATION=5;
private String order="";
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
}

public boolean isYrotation() {
	return Yrotation;
}

public void setYrotation(boolean yrotation) {
	Yrotation = yrotation;
}

public boolean isZrotation() {
	return Zrotation;
}

public void setZrotation(boolean zrotation) {
	Zrotation = zrotation;
}

public boolean isXposition() {
	return Xposition;
}

public void setXposition(boolean xposition) {
	Xposition = xposition;
}

public boolean isYposition() {
	return Yposition;
}

public void setYposition(boolean yposition) {
	Yposition = yposition;
}

public boolean isZposition() {
	return Zposition;
}

public void setZposition(boolean zposition) {
	Zposition = zposition;
}
public String toString(){
	int size=0;
	String text="";
	if(Xposition){
		size++;
		text+="Xpositon ";
	}
	if(Yposition){
		size++;
		text+="Ypositon ";
	}
	if(Zposition){
		size++;
		text+="Zpositon ";
	}if(Xrotation){
		size++;
		text+="Xrotation ";
	}
	if(Yrotation){
		size++;
		text+="Yrotation ";
	}
	if(Zrotation){
		size++;
		text+="Zrotation ";
	}
	if(text.isEmpty()){
		return "CHANNELS 0";
	}else{
		return "CHANNELS "+size+" "+text.substring(0,text.length()-1);
	}
}
}
