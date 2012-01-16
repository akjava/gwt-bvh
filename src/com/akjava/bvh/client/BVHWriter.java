package com.akjava.bvh.client;


public class BVHWriter {
private String text;
	public String writeToString(BVH bvh){
		text="";
		StringBuilder buffer=new StringBuilder();
		//hierachy
		buffer.append("HIERARCHY"+"\n");
		//text+=("HIERARCHY"+"\n");
		writeTo(bvh.getHiearchy(),buffer,0);
		
		//MOTION
		int frames=bvh.getFrames();
		buffer.append("MOTION"+"\n");
		buffer.append("Frames: "+frames+"\n");
		buffer.append("Frame Time: "+bvh.getFrameTime()+"\n");

		/*
		text+=("MOTION"+"\n");
		text+=("Frames: "+frames+"\n");
		text+=("Frame Time: "+bvh.getFrameTime()+"\n");
		*/
		
		for(int i=0;i<frames;i++){
			double[] values=bvh.getFrameAt(i);
			String v="";
			for(int j=0;j<values.length;j++){
				v+=values[j];
				if(j!=values.length-1){
					v+=" ";
				}
			}
			//text+=v+"\n";
			buffer.append(v+"\n");
		}
		//return text;
		return buffer.toString();
	}
	/*
	private void writeTo(BVHNode node,StringBuilder buffer,int indent){
		String indentText="";
		for(int i=0;i<indent;i++){
			indentText+="\t";
		}
		if(indent==0){
			text+=("ROOT "+node.getName());
			text+=("\n");
		}else{
			text+=(indentText+"JOINT "+node.getName());
			text+=("\n");
		}
		text+=(indentText+"{");
		text+=("\n");
			//offset
			
		text+=("\t"+indentText+"");
		text+=(node.getOffset().toString());
		text+=("\n");
			//channel
		text+=("\t"+indentText+"");
		text+=(node.getChannels().toString());
		text+=("\n");
			//joint
			for(int i=0;i<node.getJoints().size();i++){
				writeTo(node.getJoints().get(i),buffer,indent+1);
			}
			//endsite
			if(node.getEndSite()!=null){
				text+=("\t"+indentText+"End Site");
				text+=("\n");
				
				text+=("\t"+indentText+"{");
				text+=("\n");
				
				text+=("\t"+indentText+"\t"+node.getEndSite().toString());
				text+=("\n");
				
				text+=("\t"+indentText+"}");
				text+=("\n");
			}
			text+=(indentText+"}");
			text+=("\n");
	}*/
	
	 private void writeTo(BVHNode node,StringBuilder buffer,int indent){
		String indentText="";
		for(int i=0;i<indent;i++){
			indentText+="\t";
		}
		if(indent==0){
			buffer.append("ROOT "+node.getName());
			buffer.append("\n");
		}else{
			buffer.append(indentText+"JOINT "+node.getName());
			buffer.append("\n");
		}
			buffer.append(indentText+"{");
			buffer.append("\n");
			//offset
			
			buffer.append("\t"+indentText+"");
			buffer.append(node.getOffset().toString());
			buffer.append("\n");
			//channel
			buffer.append("\t"+indentText+"");
			buffer.append(node.getChannels().toString());
			buffer.append("\n");
			//joint
			for(int i=0;i<node.getJoints().size();i++){
				writeTo(node.getJoints().get(i),buffer,indent+1);
			}
			//endsite
			if(node.getEndSite()!=null){
				buffer.append("\t"+indentText+"End Site");
				buffer.append("\n");
				
				buffer.append("\t"+indentText+"{");
				buffer.append("\n");
				
				buffer.append("\t"+indentText+"\t"+node.getEndSite().toString());
				buffer.append("\n");
				
				buffer.append("\t"+indentText+"}");
				buffer.append("\n");
			}
			buffer.append(indentText+"}");
			buffer.append("\n");
	}
	 
}
