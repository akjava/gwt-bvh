package com.akjava.gwt.bvh.client.threejs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.bvh.client.BVH;
import com.akjava.bvh.client.BVHNode;
import com.akjava.bvh.client.Vec3;
import com.akjava.gwt.three.client.gwt.GWTThreeUtils;
import com.akjava.gwt.three.client.gwt.animation.AnimationBone;
import com.akjava.gwt.three.client.gwt.animation.AnimationData;
import com.akjava.gwt.three.client.gwt.animation.AnimationKey;
import com.akjava.gwt.three.client.gwt.animation.AnimationUtils;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.math.Matrix4;
import com.akjava.gwt.three.client.js.math.Vector3;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;

public class AnimationBoneConverter {

	private List<String> nameOrderList;
	
	
	public List<List<Vector3>> convertJsonBoneEndSites(BVH bvh){
		List<List<Vector3>> result=new ArrayList<List<Vector3>>();
		convertJsonBoneEndSites(bvh.getHiearchy(),result);
		return result;
	}
	private void convertJsonBoneEndSites(BVHNode node,List<List<Vector3>> result){
		
		List<Vec3> endsites=node.getEndSites();
		List<Vector3> endVecs=new ArrayList<Vector3>();
		for(Vec3 vec:endsites){
			endVecs.add(THREE.Vector3(vec.getX(), vec.getY(), vec.getZ()));
		}
		result.add(endVecs);
		
		for(BVHNode childNode:node.getJoints()){
			convertJsonBoneEndSites(childNode,result);
		}
		
	}
	
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
			mxPos.getPositionFromMatrix(mx);
			bone.setPos(mxPos);
			map.put(children.getName(), bone);
			
			convert(map,children,mx);
		}
	}
	
	
	//Dont work
	public static void setBoneAngles(JsArray<AnimationBone> bones,AnimationData data,int index){
		JsArrayNumber rootPos=null;
		for(int i=0;i<bones.length();i++){
			
			AnimationKey key=data.getHierarchy().get(i).getKeys().get(index);
			
			/*
			if(i==0){
				//COPY only root
				rootPos=GWTThreeUtils.clone(key.getPos());
				bones.get(i).setPos(rootPos);
			}else{
				
				JsArrayNumber newPos=GWTThreeUtils.clone(key.getPos());
				newPos.set(0, newPos.get(0)+rootPos.get(0));
				newPos.set(1, newPos.get(1)+rootPos.get(1));
				newPos.set(2, newPos.get(2)+rootPos.get(2));
			}*/
			
			/*
			Matrix4 angleM=GWTThreeUtils.degitRotationToMatrix4(THREE.Vector3(0, 0, 45));
			Vector3 pos=GWTThreeUtils.jsArrayToVector3(key.getPos());
			angleM.multiplyVector3(pos);
			bones.get(i).setPos(pos);
			*/
			
			//noeffect?
			//bones.get(i).setRot(GWTThreeUtils.clone(key.getRot()));
		}
	}
}
