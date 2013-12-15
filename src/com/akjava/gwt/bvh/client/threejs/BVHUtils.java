package com.akjava.gwt.bvh.client.threejs;

import com.akjava.bvh.client.Vec3;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.math.Vector3;

public class BVHUtils {

	
	
	
	
	public static Vector3 toVector3(Vec3 vec){
		return THREE.Vector3(vec.getX(), vec.getY(), vec.getZ());
	}
}
