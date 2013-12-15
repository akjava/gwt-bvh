package com.akjava.gwt.bvh.client.poseframe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.akjava.gwt.three.client.gwt.animation.AngleAndPosition;
import com.akjava.gwt.three.client.gwt.animation.AnimationBonesData;
import com.akjava.gwt.three.client.js.math.Vector3;

public class PoseFrameData {
List<AngleAndPosition> matrixs;//for current bone

private List<Vector3> positions;//relate-pos,+bonePos & multiply angles -> matrixs
public List<Vector3> getPositions() {
	return positions;
}
public void setPositions(List<Vector3> positions) {
	this.positions = positions;
}
public List<Vector3> getAngles() {
	return angles;
}
public void setAngles(List<Vector3> angles) {
	this.angles = angles;
}
private List<Vector3> angles;


/*
List<Vector3> ikTargetPositions;	
List<String> ikTargetNames;
*/
public List<AngleAndPosition> getAngleAndMatrixs() {
	return matrixs;
}
public void setAngleAndMatrixs(List<AngleAndPosition> matrixs) {
	this.matrixs = matrixs;
}
private Map<String,Vector3> ikTargetPositionMap=new LinkedHashMap<String,Vector3>();

public Map<String, Vector3> getIkTargetPositionMap() {
	return ikTargetPositionMap;
}
public void setIkTargetPositionMap(Map<String, Vector3> ikTargetPositionMap) {
	this.ikTargetPositionMap = ikTargetPositionMap;
}
public Vector3 getIkTargetPosition(String name){
	return ikTargetPositionMap.get(name);
}

public void setIkTargetPosition(String name,Vector3 pos){
	 ikTargetPositionMap.put(name,pos);
}

public PoseFrameData(List<AngleAndPosition> matrixs,Map<String, Vector3> ikTargetPositionMap){
	this.matrixs=matrixs;
	this.ikTargetPositionMap=ikTargetPositionMap;
}

/*
public List<String> getIkTargetNames() {
	return ikTargetNames;
}
public void setIkTargetNames(List<String> ikTargetNames) {
	this.ikTargetNames = ikTargetNames;
}
*/
public PoseFrameData(){}
public PoseFrameData clone(){
	List<AngleAndPosition> matrixs=AnimationBonesData.cloneAngleAndMatrix(this.matrixs);
	
	
	
	List<Vector3> ags=new ArrayList<Vector3>();
	for(Vector3 vec:angles){
		ags.add(vec.clone());
	}
	
	List<Vector3> pos=new ArrayList<Vector3>();
	for(Vector3 vec:positions){
		pos.add(vec.clone());
	}
	
	 Map<String,Vector3> ikData=new LinkedHashMap<String,Vector3>();
	for(String key:ikTargetPositionMap.keySet()){
		ikData.put(key, ikTargetPositionMap.get(key));
	}
	
	PoseFrameData pdata= new PoseFrameData(matrixs,ikData);
	pdata.setAngles(ags);
	pdata.setPositions(pos);
	return pdata;
}
}
