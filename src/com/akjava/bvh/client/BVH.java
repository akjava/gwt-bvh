package com.akjava.bvh.client;

import java.util.ArrayList;
import java.util.List;


public class BVH {
private BVHNode hiearchy;
private Motion motion;
public Motion getMotion() {
	return motion;
}

public void setMotion(Motion motion) {
	this.motion = motion;
}

public BVHNode getHiearchy() {
	return hiearchy;
}

public void setHiearchy(BVHNode hiearchy) {
	this.hiearchy = hiearchy;
}
public List<NameAndChannel> nameAndChannels=new ArrayList<NameAndChannel>();
public List<NameAndChannel> getNameAndChannels() {
	return nameAndChannels;
}

public void add(NameAndChannel nc){
	nameAndChannels.add(nc);
}
}
