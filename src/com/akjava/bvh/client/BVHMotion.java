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

import java.util.ArrayList;
import java.util.List;

public class BVHMotion {
private int frames;
private double frameTime;
public int getFrames() {
	return frames;
}
public double[] getFrameAt(int index){
	return motions.get(index);
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
