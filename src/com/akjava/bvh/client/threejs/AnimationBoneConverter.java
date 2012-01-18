package com.akjava.bvh.client.threejs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.bvh.client.BVH;
import com.akjava.bvh.client.BVHNode;
import com.akjava.gwt.three.client.THREE;
import com.akjava.gwt.three.client.core.Matrix4;
import com.akjava.gwt.three.client.core.Quaternion;
import com.akjava.gwt.three.client.core.Vector3;
import com.akjava.gwt.three.client.gwt.GWTThreeUtils;
import com.akjava.gwt.three.client.gwt.animation.AnimationBone;
import com.akjava.gwt.three.client.gwt.animation.AnimationUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;

public class AnimationBoneConverter {

	private List<String> nameOrderList;
	
	public JsArray<AnimationBone> convertJsonBone(BVH bvh){
		nameOrderList=new ArrayList<String>();
		String oldName=null;
		for(int i=0;i<bvh.getNameAndChannels().size();i++){
			String newName=bvh.getNameAndChannels().get(i).getName();

			if(newName==null){
				GWT.log("null-name");
			}
			
			if(!newName.equals(oldName)){
				nameOrderList.add(newName);
				oldName=newName;
			}
		}
		
		
		parentIdMaps=new HashMap<String,Integer>();
		for(int i=0;i<nameOrderList.size();i++){
			parentIdMaps.put(nameOrderList.get(i), i);
		}
		JsArray<AnimationBone> array=(JsArray<AnimationBone>)JsArray.createArray();
		
		
		Map<String,AnimationBone> bones=new HashMap<String,AnimationBone>();
		BVHNode root=bvh.getHiearchy();
		
		Matrix4 mx=THREE.Matrix4();
		Vector3 pos=BVHUtils.toVector3(root.getOffset());
		mx.setPosition(pos);
		
		AnimationBone bone=AnimationUtils.createAnimationBone();
		bone.setParent(-1);
		bone.setName(root.getName());
		bone.setPos(pos);
		//root dont have rot
		bones.put(root.getName(), bone);
		
		
		convert(bones,root,mx);
		
		for(int i=0;i<nameOrderList.size();i++){
			AnimationBone abone=bones.get(nameOrderList.get(i));
			array.push(abone);
		}
		return array;
	}
	private Map<String,Integer> parentIdMaps;
	
	private  void convert(Map<String,AnimationBone> map,BVHNode parent,Matrix4 parentMx){
		for(BVHNode children:parent.getJoints()){
			Matrix4 mx=THREE.Matrix4();
			Vector3 pos=BVHUtils.toVector3(children.getOffset());
			mx.setPosition(pos);
			AnimationBone bone=AnimationUtils.createAnimationBone();
			bone.setParent(parentIdMaps.get(parent.getName()));
			bone.setName(children.getName());
			
			//bone rot
			Vector3 rot=GWTThreeUtils.getRotation(THREE.Vector3(), pos);
			//value not good as expected,TODO fix
			//bone.setRotq(GWTThreeUtils.rotationQuaternion(rot));
			
			//TODO If you wish absolutepath use parent matrix,but this version format dont need it.
			//mx=mx.multiply(parentMx, mx);
			
			Vector3 mxPos=THREE.Vector3();
			mxPos.setPositionFromMatrix(mx);
			bone.setPos(mxPos);
			map.put(children.getName(), bone);
			
			convert(map,children,mx);
		}
	}
}