package com.akjava.gwt.bvh.client.threejs;

import java.util.HashMap;
import java.util.Map;

import com.akjava.bvh.client.Vec3;
import com.akjava.gwt.three.client.THREE;
import com.akjava.gwt.three.client.core.Matrix4;
import com.akjava.gwt.three.client.core.Vector3;
import com.akjava.gwt.three.client.gwt.animation.AnimationBone;
import com.akjava.gwt.three.client.gwt.animation.AnimationUtils;
import com.google.gwt.core.client.JsArray;

public class BVHUtils {

	
	
	
	
	public static Vector3 toVector3(Vec3 vec){
		return THREE.Vector3(vec.getX(), vec.getY(), vec.getZ());
	}
}
