package com.akjava.gwt.bvh.client.threejs;

import java.util.ArrayList;
import java.util.List;

import com.akjava.bvh.client.BVH;
import com.akjava.bvh.client.BVHMotion;
import com.akjava.bvh.client.BVHNode;
import com.akjava.bvh.client.Channels;
import com.akjava.bvh.client.NameAndChannel;
import com.akjava.bvh.client.Vec3;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.gwt.three.client.gwt.animation.AngleAndPosition;
import com.akjava.gwt.three.client.gwt.animation.AnimationBone;
import com.akjava.gwt.three.client.java.utils.GWTThreeUtils;
import com.akjava.gwt.three.client.js.THREE;
import com.akjava.gwt.three.client.js.math.Matrix4;
import com.akjava.gwt.three.client.js.math.Quaternion;
import com.akjava.gwt.three.client.js.math.Vector3;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayNumber;

public class BVHConverter {
	public BVHNode convertBVHNode(JsArray<AnimationBone> bones){
		List<BVHNode> bvhs=new ArrayList<BVHNode>();
		
		for(int i=0;i<bones.length();i++){
			AnimationBone bone=bones.get(i);
			
			BVHNode node=null;
			if(bone.getParent()!=-1){
				
				node=boneToBVHNode(bone,bones);//try has rotq
				//node=boneToBVHNode(bone);
			}else{
				node=boneToBVHNode(bone);
			}
			
			//BVHNode node=boneToBVHNode(bone);
			
			/*
			if(i==0){
				node.setOffset(new Vec3(0,0,0));//i think it's better,i change mind, need this one
			}
			*/
			bvhs.add(node);
			//add parent
			if(bone.getParent()!=-1){
				bvhs.get(bone.getParent()).add(node);
			}
		}
		
		//add endsite
		for(BVHNode node:bvhs){
			if(node.getJoints().size()==0){//empty has end site
				Vec3 endSite=node.getOffset().clone();//.multiplyScalar(2);
				node.addEndSite(endSite);
			}
		}
		
		return bvhs.get(0);
	}
	
	public BVH createBVH(JsArray<AnimationBone> bones){
		BVHConverter converter=new BVHConverter();
		
		BVHNode rootNode=converter.convertBVHNode(bones);

		converter.setChannels(rootNode,0,"XYZ");	//TODO support other order
		BVH bvh=new BVH();
		bvh.setHiearchy(rootNode);
		
		BVHMotion motion=new BVHMotion();
		motion.setFrameTime(.25);
				
		createChannels(bvh,rootNode);

		//create initial zero motion
		motion.add(new double[bvh.getNameAndChannels().size()]);//only root has pos,TODO find better way
		motion.setFrames(motion.getMotions().size());//
		
		bvh.setMotion(motion);
		
		return bvh;
	}
	
	private void createChannels(BVH bvh,BVHNode node){
		
		if(node.getChannels().isXposition()){
			
			
			bvh.add(new NameAndChannel(node.getName(), Channels.XPOSITION,node.getChannels()));
		}
		if(node.getChannels().isYposition()){
			
			
			bvh.add(new NameAndChannel(node.getName(), Channels.YPOSITION,node.getChannels()));
		}
		
		if(node.getChannels().isZposition()){
		
			bvh.add(new NameAndChannel(node.getName(), Channels.ZPOSITION,node.getChannels()));
		}
		
		if(node.getChannels().isXrotation()){
			
			bvh.add(new NameAndChannel(node.getName(), Channels.XROTATION,node.getChannels()));
		}
		if(node.getChannels().isYrotation()){
		
			bvh.add(new NameAndChannel(node.getName(), Channels.YROTATION,node.getChannels()));
		}
		
		if(node.getChannels().isZrotation()){
			
			bvh.add(new NameAndChannel(node.getName(), Channels.ZROTATION,node.getChannels()));
		}
	
		for(BVHNode joint:node.getJoints()){
			createChannels(bvh, joint);
		}
	}
	
	
public double[] angleAndMatrixsToMotion(List<AngleAndPosition> matrixs,int mode,String order){
		
		List<Double> values=new ArrayList<Double>();
		for(int i=0;i<matrixs.size();i++){
			Matrix4 mx=matrixs.get(i).getMatrix();//TODO change angle
			if((i==0 && mode==ROOT_POSITION_ROTATE_ONLY) || mode==POSITION_ROTATE){
				Vector3 pos=GWTThreeUtils.toPositionVec(mx);
				values.add(pos.getX());
				values.add(pos.getY());
				values.add(pos.getZ());
			}
			Vector3 tmprot=GWTThreeUtils.rotationToVector3(mx);
			//TODO order convert;
			if(!order.equals("XYZ")){
				LogUtils.log("Warning:only support-XYZ");
			}
			//rot has problem
			/*
			Vector3 rotDegree=GWTThreeUtils.radiantToDegree(tmprot);
			values.add(rotDegree.getX());
			values.add(rotDegree.getY());
			values.add(rotDegree.getZ());
			*/
			Vector3 angle=matrixs.get(i).getAngle();
			values.add(angle.getX());
			values.add(angle.getY());
			values.add(angle.getZ());
		}
		
		
		
		
		double[] bt=new double[values.size()];
		for(int i=0;i<values.size();i++){
			bt[i]=values.get(i).doubleValue();
		}
		return bt;
	}
	/**
	 * bugs cant handle over 90 angle
	 * @deprecated
	 * @param matrixs
	 * @param mode
	 * @param order
	 * @return
	 */
	public double[] matrixsToMotion(List<Matrix4> matrixs,int mode,String order){
		
		List<Double> values=new ArrayList<Double>();
		for(int i=0;i<matrixs.size();i++){
			Matrix4 mx=matrixs.get(i);
			if((i==0 && mode==ROOT_POSITION_ROTATE_ONLY) || mode==POSITION_ROTATE){
				Vector3 pos=GWTThreeUtils.toPositionVec(mx);
				values.add(pos.getX());
				values.add(pos.getY());
				values.add(pos.getZ());
			}
			Vector3 tmprot=GWTThreeUtils.rotationToVector3(mx);
			//TODO order convert;
			if(!order.equals("XYZ")){
				LogUtils.log("Warning:only support-XYZ");
			}
			Vector3 rotDegree=GWTThreeUtils.radiantToDegree(tmprot);
			values.add(rotDegree.getX());
			values.add(rotDegree.getY());
			values.add(rotDegree.getZ());
		}
		
		
		
		
		double[] bt=new double[values.size()];
		for(int i=0;i<values.size();i++){
			bt[i]=values.get(i).doubleValue();
		}
		return bt;
	}
	
	
	private BVHNode boneToBVHNode(AnimationBone bone,JsArray<AnimationBone> bones){
		BVHNode bvhNode=new BVHNode();
		bvhNode.setName(bone.getName());
		
		
		Quaternion q=THREE.Quaternion();
		AnimationBone parent=bones.get(bone.getParent());
		
		List<Integer> parents=new ArrayList<Integer>();
		parents.add(bone.getParent());
		
		
		while(parent.getParent()!=-1){
			parents.add(parent.getParent());
			parent=bones.get(parent.getParent());
		}
		
		//order is very important
		for(int i=parents.size()-1;i>=0;i--){
			int v=parents.get(i);
			q.multiply(THREE.Quaternion().fromArray(bones.get(v).getRotq()));
		}
		
		
		Vector3 v=THREE.Vector3().fromArray(bone.getPos());
		
		v.applyQuaternion(q);
		
		
		
		Vec3 pos=new Vec3(v.getX(),v.getY(),v.getZ());
		
		//Vec3 pos=new Vec3(bone.getPos().get(0),bone.getPos().get(1),bone.getPos().get(2));
		bvhNode.setOffset(pos);
		
		//no channels
		return bvhNode;
	}
	
	
	private BVHNode boneToBVHNode(AnimationBone bone){
		BVHNode bvhNode=new BVHNode();
		bvhNode.setName(bone.getName());
		
		
		//TODO support rotQ
		
		//Matrix4 m4=THREE.Matrix4().makeRotationFromQuaternion(THREE.Quaternion().fromArray(bone.getRotq()));
		
		//Quaternion q=THREE.Quaternion().fromArray(bone.getRotq());
		Vector3 v=THREE.Vector3().fromArray(bone.getPos());
		
		//v.applyQuaternion(q);
		
		
		
		Vec3 pos=new Vec3(v.getX(),v.getY(),v.getZ());
		
		//Vec3 pos=new Vec3(bone.getPos().get(0),bone.getPos().get(1),bone.getPos().get(2));
		bvhNode.setOffset(pos);
		
		//no channels
		return bvhNode;
	}
	
	public static int ROOT_POSITION_ROTATE_ONLY=0;
	public static int ROTATE_ONLY=1;
	public static int POSITION_ROTATE=2;
	public void setChannels(BVHNode node,int mode,String order){
		Channels ch=new Channels();
		if(mode==ROOT_POSITION_ROTATE_ONLY || mode==POSITION_ROTATE){
		ch.setXposition(true);
		ch.setYposition(true);
		ch.setZposition(true);
		}
		if(order.equals("XZY")){
			ch.setXrotation(true);
			ch.setZrotation(true);
			ch.setYrotation(true);
		}else if(order.equals("YZX")){
			ch.setYrotation(true);
			ch.setZrotation(true);
			ch.setXrotation(true);
		}else if(order.equals("YXZ")){
			ch.setYrotation(true);
			ch.setXrotation(true);
			ch.setZrotation(true);
		}else if(order.equals("ZXY")){
			ch.setZrotation(true);
			ch.setXrotation(true);
			ch.setYrotation(true);
		}else if(order.equals("ZYX")){
			ch.setZrotation(true);
			ch.setYrotation(true);
			ch.setXrotation(true);
		}else{//XYZ
			ch.setXrotation(true);
			ch.setYrotation(true);
			ch.setZrotation(true);
		}
		node.setChannels(ch);
		for(BVHNode child:node.getJoints()){
			int m=mode;
			if(mode==ROOT_POSITION_ROTATE_ONLY){
				m=ROTATE_ONLY;
			}
			setChannels(child,m,order);
		}
	}
}
