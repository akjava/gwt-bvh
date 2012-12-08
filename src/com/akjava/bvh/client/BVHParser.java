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

import com.akjava.gwt.lib.client.LineSplitter;
import com.akjava.gwt.lib.client.LineSplitter.SplitterListener;
import com.akjava.gwt.lib.client.LogUtils;
import com.akjava.lib.common.utils.Benchmark;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;


public class BVHParser {

	private final int EXPECT_HIERARCHY=0;
	private final int EXPECT_ROOT=1;
	private final int EXPECT_JOINT_OPEN=2;
	private final int EXPECT_JOINT_CLOSE=3;
	private final int EXPECT_JOINT_INSIDE=4;
	private final int EXPECT_ENDSITES_OPEN=5;
	private final int EXPECT_ENDSITES_INSIDE=6;
	private final int EXPECT_ENDSITES_CLOSE=7;
	
	private final int EXPECT_MOTION=8;
	private final int EXPECT_MOTION_FRAMES=9;
	private final int EXPECT_MOTION_FRAME_TIME=10;
	private final int EXPECT_MOTION_DATA=11;
	private int mode;
	List<BVHNode> nodes=new ArrayList<BVHNode>();
	private BVH bvh;
	
	
	
	public static interface ParserListener {
		public void onSuccess(BVH bvh);
		public void onFaild(String message);
	}
	
	public  void parseAsync(String text,final ParserListener listener){
		initialize();
		final String replaced=text.replace("\r", "");
		
		Benchmark.start("split");
		LineSplitter splitter=new LineSplitter(replaced,10,new SplitterListener() {
			@Override
			public void onSuccess(List<String> lines) {
				//LogUtils.log("splited:"+Benchmark.end("split"));
				try {
					parseLines(lines.toArray(new String[lines.size()]));
				} catch (InvalidLineException e) {
					listener.onFaild(e.getMessage());
				}
				//LogUtils.log("parsed:"+Benchmark.end("parse"));
				validate();
				listener.onSuccess(bvh);
			}
		});
		
		
		Scheduler.get().scheduleEntry(splitter);
	}
	
	public  BVH parse(String text) throws InvalidLineException{
		initialize();
		text=text.replace("\r", "");
		
		String lines[]=LineSplitter.splitLineAsArray(text);
		parseLines(lines);
		
		validate();
		return bvh;
	}
		
	public BVH getBvh() {
		return bvh;
	}

	public  void parseLines(String[] lines) throws InvalidLineException{ 
		parseLines(lines,0,lines.length);
	}
	public  void parseLines(String[] lines,int start,int end) throws InvalidLineException{ 
		
		
		
		
		

		for(int i=start;i<end&&i<lines.length;i++){
		
		String line=lines[i].trim();
		if(line.isEmpty()){
			continue;//empty always skip
		}
		switch(mode){
		case EXPECT_HIERARCHY:
			if(!line.equals("HIERARCHY")){
				throw new InvalidLineException(i,line, "Expected HIERARCHY");
			}
			mode=EXPECT_ROOT;
			break;
		case EXPECT_ROOT:
			if(line.startsWith("ROOT")){
				BVHNode node=new BVHNode();
				String name=line.substring("ROOT".length()).trim();
				node.setName(name);
				bvh.setHiearchy(node);
				nodes.add(node);
				
				mode=EXPECT_JOINT_OPEN;
			}else{
				throw new InvalidLineException(i,line, "Expected ROOT");
			}
			break;
		case EXPECT_JOINT_OPEN:
			if(line.equals("{")){
				mode=EXPECT_JOINT_INSIDE;
			}else{
				throw new InvalidLineException(i,line, "Expected {");
			}
			break;
		case EXPECT_JOINT_INSIDE:
			String[] values=line.split(" ");
			if(values[0].equals("OFFSET")){
				if(values.length!=4){
					throw new InvalidLineException(i,line, "OFFSET value need 3 points");
				}
				double x=toDouble(i,line,values[1]);
				double y=toDouble(i,line,values[2]);
				double z=toDouble(i,line,values[3]);
				getLast().setOffset(new Vec3(x,y,z));
			}else if(values[0].equals("CHANNELS")){
				if(values.length<2){
					throw new InvalidLineException(i,line, "CHANNEL must have 3 values");
				}
				int channelSize=toInt(i,line,values[1]);
				if(channelSize<1){
					throw new InvalidLineException(i,line, "CHANNEL  size must be larger than 0");
				}
				
				if(channelSize!=values.length-2){
					throw new InvalidLineException(i,line, " Invalid CHANNEL  size:"+channelSize);
				}
				Channels channel=new Channels();
				for(int j=2;j<values.length;j++){
					if(values[j].equals("Xposition")){
						channel.setXposition(true);
						bvh.add(new NameAndChannel(getLast().getName(), Channels.XPOSITION,channel));
					}else if(values[j].equals("Yposition")){
						channel.setYposition(true);
						bvh.add(new NameAndChannel(getLast().getName(), Channels.YPOSITION,channel));
					}else if(values[j].equals("Zposition")){
						channel.setZposition(true);
						
						bvh.add(new NameAndChannel(getLast().getName(), Channels.ZPOSITION,channel));
					}else if(values[j].equals("Xrotation")){
						channel.setXrotation(true);
						channel.addOrder("X");
						bvh.add(new NameAndChannel(getLast().getName(), Channels.XROTATION,channel));
					}else if(values[j].equals("Yrotation")){
						channel.setYrotation(true);
						channel.addOrder("Y");
						bvh.add(new NameAndChannel(getLast().getName(), Channels.YROTATION,channel));
					}else if(values[j].equals("Zrotation")){
						channel.setZrotation(true);
						channel.addOrder("Z");
						bvh.add(new NameAndChannel(getLast().getName(), Channels.ZROTATION,channel));
					}else{
						throw new InvalidLineException(i,line, " Invalid CHANNEL value:"+values[j]);
					}
				}
				getLast().setChannels(channel);
			}else if(line.equals("End Site")){
				mode=EXPECT_ENDSITES_OPEN;
			}else if(line.equals("}")){
				mode=EXPECT_JOINT_INSIDE;
				nodes.remove(getLast());//pop up
				
				if(nodes.size()==0){
					mode=EXPECT_MOTION;
				}
			}else if(values[0].equals("JOINT")){
				if(values.length!=2){
					throw new InvalidLineException(i,line, " Invalid Joint name:"+line);
				}
				String name=values[1];
				BVHNode node=new BVHNode();
				node.setName(name);
				getLast().add(node);
				nodes.add(node);
				mode=EXPECT_JOINT_OPEN;
			}else{
				throw new InvalidLineException(i,line, " Invalid Joint inside:"+values[0]);
			}
			break;
		case EXPECT_ENDSITES_OPEN:
			if(line.equals("{")){
				mode=EXPECT_ENDSITES_INSIDE;
			}else{
				throw new InvalidLineException(i,line, "Expected {");
			}
			break;
		case EXPECT_ENDSITES_INSIDE:
			String[] values2=line.split(" ");
			if(values2[0].equals("OFFSET")){
				if(values2.length!=4){
					throw new InvalidLineException(i,line, "OFFSET value need 3 points");
				}
				double x=toDouble(i,line,values2[1]);
				double y=toDouble(i,line,values2[2]);
				double z=toDouble(i,line,values2[3]);
				getLast().addEndSite(new Vec3(x,y,z));
				mode=EXPECT_ENDSITES_CLOSE;
			}else{
				throw new InvalidLineException(i,line, "Endsite only support offset");
			}
			break;
		case EXPECT_ENDSITES_CLOSE:	
			if(line.equals("}")){
				mode=EXPECT_JOINT_CLOSE;
			}else{
				throw new InvalidLineException(i,line, "Expected {");
			}
			break;
		case EXPECT_JOINT_CLOSE:	
			if(line.equals("}")){
				mode=EXPECT_JOINT_INSIDE;//maybe joint again or close
				nodes.remove(getLast());//pop up
				
				if(nodes.size()==0){
					mode=EXPECT_MOTION;
				}
			}else if(line.equals("End Site")){
				mode=EXPECT_ENDSITES_OPEN;
			}else{
				throw new InvalidLineException(i,line, "Expected {");
			}
			break;
		case EXPECT_MOTION:
			if(line.equals("MOTION")){
				BVHMotion motion=new BVHMotion();
				bvh.setMotion(motion);
				mode=EXPECT_MOTION_FRAMES;
			}else{
				throw new InvalidLineException(i,line, "Expected MOTION");
			}
			break;
		case EXPECT_MOTION_FRAMES:
			String[] mv=line.split(" ");
			if(mv[0].equals("Frames:")&& mv.length==2){
				int frames=toInt(i, line, mv[1]);
				bvh.getMotion().setFrames(frames);
				mode=EXPECT_MOTION_FRAME_TIME;
			}else{
				throw new InvalidLineException(i,line, "Expected Frames:");
			}
			break;
		case EXPECT_MOTION_FRAME_TIME:
			String[] mv2=line.split(":");//not space
			if(mv2[0].equals("Frame Time")&& mv2.length==2){
				double frameTime=toDouble(i, line, mv2[1].trim());
				bvh.getMotion().setFrameTime(frameTime);
				mode=EXPECT_MOTION_DATA;
			}else{
				throw new InvalidLineException(i,line, "Expected Frame Time");
			}
			break;
		case EXPECT_MOTION_DATA:
			double vs[]=toDouble(i,line);
			bvh.getMotion().getMotions().add(vs);
		break;
		}
		}
		
		
	}
	
	public boolean validate(){
		if(bvh.getMotion().getFrames()!=bvh.getMotion().getMotions().size()){
		return false;	
		}
		return true;
	}
	
	protected static double[] toDouble(String[] values){
		double vs[]=new double[values.length];
		for(int i=0;i<values.length;i++){
			vs[i]=Double.parseDouble(values[i]);
		}
		return vs;
	}
	protected  double[] toDouble(int index,String line) throws InvalidLineException{
		String values[]=line.split(" ");
		double vs[]=new double[values.length];
		try{
		for(int i=0;i<values.length;i++){
			vs[i]=Double.parseDouble(values[i]);
		}
		}catch(Exception e){
			throw new InvalidLineException(index, line, "has invalid double");
		}
		return vs;
	}
	
	public void initialize() {
		bvh=new BVH();
		nodes=new ArrayList<BVHNode>();
		mode=0;
	}
	protected BVHNode getLast(){
		return nodes.get(nodes.size()-1);
	}
	
	protected int toInt(int index,String line,String v) throws InvalidLineException{
		int d=0;
		try{
			d=Integer.parseInt(v);
		}catch(Exception e){
			throw new InvalidLineException(index,line,"invalid double value:"+v);
		}
		return d;
	}
	
	protected double toDouble(int index,String line,String v) throws InvalidLineException{
		double d=0;
		try{
			d=Double.parseDouble(v);
		}catch(Exception e){
			throw new InvalidLineException(index,line,"invalid double value:"+v);
		}
		return d;
	}
	
	public class InvalidLineException extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public InvalidLineException(int index,String line,String message){
			super("line "+index+":"+line+":message:"+message);
		}
		
	}
}
