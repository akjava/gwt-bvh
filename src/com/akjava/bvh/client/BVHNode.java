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


public class BVHNode {
private Vec3 offset;
public Vec3 getOffset() {
	return offset;
}
public void setOffset(Vec3 offset) {
	this.offset = offset;
}
public Vec3 getEndSite() {
	return endSite;
}
public void setEndSite(Vec3 endSite) {
	this.endSite = endSite;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Channels getChannels() {
	return channels;
}
public void setChannels(Channels channels) {
	this.channels = channels;
}
private Vec3 endSite;
private String name;
private Channels channels;
private List<BVHNode> joints=new ArrayList<BVHNode>();
public List<BVHNode> getJoints() {
	return joints;
}
public void add(BVHNode joint){
	joints.add(joint);
}

//usually null for special purpose
private String parentName;
public String getParentName() {
	return parentName;
}
public void setParentName(String parentName) {
	this.parentName = parentName;
}
}
