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
}
