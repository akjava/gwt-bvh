package com.akjava.bvh.client;

public class BVHWriter {

	public String writeToString(BVH bvh){
		StringBuffer buffer=new StringBuffer();
		//hierachy
		buffer.append("HIERARCHY"+"\n");
		writeTo(bvh.getHiearchy(),buffer,0);
		
		//MOTION
		int frames=bvh.getFrames();
		buffer.append("MOTION"+"\n");
		buffer.append("Frames: "+frames+"\n");
		buffer.append("Frame Time: "+bvh.getFrameTime()+"\n");
		
		for(int i=0;i<frames;i++){
			double[] values=bvh.getFrameAt(i);
			String v="";
			for(int j=0;j<values.length;j++){
				v+=values[j];
				if(j!=values.length-1){
					v+=" ";
				}
			}
			buffer.append(v+"\n");
		}
		
		return buffer.toString();
	}
	private void writeTo(BVHNode node,StringBuffer buffer,int indent){
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
