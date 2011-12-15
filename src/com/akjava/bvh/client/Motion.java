package com.akjava.bvh.client;

import java.util.ArrayList;
import java.util.List;

public class Motion {
private int frames;
private double frameTime;
public int getFrames() {
	return frames;
}
public void setFrames(int frames) {
	this.frames = frames;
}
public double getFrameTime() {
	return frameTime;
}
public void setFrameTime(double frameTime) {
	this.frameTime = frameTime;
}
public List<double[]> getMotions() {
	return motions;
}
private List<double[]> motions=new ArrayList<double[]>();
public void add(double[] motion){
	motions.add(motion);
}
public int size() {
	return motions.size();
}
public double getDuration(){//TODO support ignore first
	return frameTime*motions.size();
}

}
