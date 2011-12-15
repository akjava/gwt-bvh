package com.akjava.gwt.bvh.client;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akjava.bvh.client.BVH;
import com.akjava.bvh.client.BVHNode;
import com.akjava.bvh.client.BVHParser;
import com.akjava.bvh.client.BVHParser.InvalidLineException;
import com.akjava.bvh.client.Channels;
import com.akjava.bvh.client.NameAndChannel;
import com.akjava.bvh.client.Vec3;
import com.akjava.gwt.html5.client.HTML5InputRange;
import com.akjava.gwt.html5.client.extra.HTML5Builder;
import com.akjava.gwt.html5.client.file.File;
import com.akjava.gwt.html5.client.file.FileHandler;
import com.akjava.gwt.html5.client.file.FileReader;
import com.akjava.gwt.html5.client.file.FileUtils;
import com.akjava.gwt.three.client.THREE;
import com.akjava.gwt.three.client.core.Geometry;
import com.akjava.gwt.three.client.core.Intersect;
import com.akjava.gwt.three.client.core.Matrix4;
import com.akjava.gwt.three.client.core.Object3D;
import com.akjava.gwt.three.client.core.Projector;
import com.akjava.gwt.three.client.core.Vector3;
import com.akjava.gwt.three.client.gwt.Clock;
import com.akjava.gwt.three.client.gwt.SimpleDemoEntryPoint;
import com.akjava.gwt.three.client.lights.Light;
import com.akjava.gwt.three.client.objects.Mesh;
import com.akjava.gwt.three.client.renderers.WebGLRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTBVH extends SimpleDemoEntryPoint {

	final Projector projector=THREE.Projector();
	@Override
	public void onMouseClick(ClickEvent event) {
		int x=event.getX();
		int y=event.getY();
		/*
		if(inEdge(x,y)){
			screenMove(x,y);
			return;
		}*/
		
		JsArray<Intersect> intersects=projector.pickIntersects(event.getX(), event.getY(), screenWidth, screenHeight, camera,scene);
		
		for(int i=0;i<intersects.length();i++){
			Intersect sect=intersects.get(i);
			
			
			select(sect.getObject());
			break;
		}
		
	}


	Object3D selectedObject;
	
	private void select(Object3D selected){
		selectedObject=selected;
		meshScaleX.setValue((int) (selectedObject.getScale().getX()*10));
		meshScaleY.setValue((int) (selectedObject.getScale().getY()*10));
		meshScaleZ.setValue((int) (selectedObject.getScale().getZ()*10));
		
		positionX.setValue((int) (selectedObject.getPosition().getX()*10));
		positionY.setValue((int) (selectedObject.getPosition().getY()*10));
		positionZ.setValue((int) (selectedObject.getPosition().getZ()*10));
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		// TODO Auto-generated method stub
		
	}
	

	Map<String,Vector3> boneSizeMap=new HashMap<String,Vector3>();
	@Override
	public void initializeOthers(WebGLRenderer renderer) {
		cameraY=10;
		defaultZoom=5;
		canvas.setClearColorHex(0xcccccc);
		
		
		
		scene.add(THREE.AmbientLight(0x888888));
		Light pointLight = THREE.PointLight(0xffffff);
		pointLight.setPosition(0, 10, 300);
		scene.add(pointLight);
		
		doLoad("80_70");
		
		Geometry geo=THREE.PlaneGeometry(50, 50);
		Mesh mesh=THREE.Mesh(geo, THREE.MeshBasicMaterial().color(0x666666).wireFrame(false).build());
		mesh.setPosition(0, 0, -50);
		mesh.setRotation(Math.toRadians(90), 0, 0);
		//scene.add(mesh);
		/*
		BVHParser parser=new BVHParser();
		//String singleBvh=Bundles.INSTANCE.single_basic().getText();
		//String singleBvh=Bundles.INSTANCE.twolink_n_rz().getText();
		//String singleBvh=Bundles.INSTANCE.twolink_full().getText();
		//String singleBvh=Bundles.INSTANCE.fourlink_branch().getText();
		String singleBvh=Bundles.INSTANCE.twelve01().getText();
		try {
			jointMap=new HashMap<String,Object3D>();
		bvh = parser.parse(singleBvh);
		BVHNode node=bvh.getHiearchy();
		
		root=THREE.Object3D();
		scene.add(root);
		doLog(root,node);
		
		//  
		//jointMap.get("Hips").setRotation(Math.toRadians(7.1338),Math.toRadians(-1.8542),Math.toRadians( -7.8190));
		
		//doPose(bvh,"-0.3899 17.5076 7.8007 0 0 0 0 0 0 -21 0 0 0 0 0 0 0 0 0 0 0 0 0 0 21 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 -16 0 0 21 0 0 11 0 0 0 -8 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 8 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0");
		//doPose(bvh,"-0.3899 17.5076 7.8007 -7.8190 -1.8542 7.1338 0.0000 0.0000 0.0000 -20.0095 -34.8717 23.1818 0.0543 -1.5040 -4.1368 -5.1851 39.6153 -34.2618 0.1274 2.3012 6.3387 0.0000 0.0000 0.0000 27.9815 23.6282 8.6217 -0.0094 -0.6245 1.7162 0.4741 -16.6281 -18.3131 -0.0041 -0.4123 1.1330 4.5929 -0.1445 3.0131 1.7537 0.3513 -8.5234 -0.7284 -0.1282 -10.7842 -14.7495 -15.2891 1.4491 15.1151 -23.2999 16.3175 5.0944 -11.3477 8.3590 -0.0000 -0.0000 0.0000 -91.7499 11.2503 9.6316 11.7870 -19.4845 -2.0307 -0.0000 -0.0000 23.6415 17.7243 -7.2146 -2.2985 -7.1250 0.0000 -0.0000 -6.6264 22.4256 3.6210 -0.0000 -0.0000 0.0000 92.9366 -10.7837 -16.0742 -9.0830 15.2927 -1.2219 0.0000 0.0000 58.8395 3.1909 -29.5170 -1.5733 7.1250 -0.0000 -0.0000 36.2824 -55.5617 -10.3909");
		//doPose(bvh,"-0.5011 17.6282 7.6163 -6.2586 -15.2051 5.4723 0.0000 0.0000 0.0000 -21.3981 -26.0192 22.4790 0.0292 -1.1029 -3.0320 -3.3535 38.5451 -41.4272 0.6169 5.0265 13.9840 0.0000 0.0000 0.0000 24.7397 22.1958 5.0034 -0.0966 -2.0046 5.5183 -0.3873 -3.4522 -13.5373 -0.2228 3.0387 -8.3865 3.1625 -2.9263 2.2948 2.9237 -3.5238 -7.9690 1.7270 -1.8496 -9.7328 3.3763 2.5058 -7.4595 13.0244 0.5160 17.4961 3.6704 1.4696 6.5682 -0.0000 0.0000 0.0000 -42.7751 -56.6315 -8.6834 50.4538 -53.1769 -26.5370 -0.0000 0.0000 -77.6575 14.7878 7.4808 1.9684 -7.1250 0.0000 -0.0000 -10.2345 37.5343 1.9047 -0.0000 0.0000 0.0000 76.3877 59.4476 93.5538 -141.2974 47.2822 -102.5201 0.0000 -0.0000 -31.1956 -12.8820 -58.8974 11.0797 7.1250 -0.0000 -0.0000 91.0580 -76.5215 -77.2227");
	doPose(bvh,bvh.getMotion().getMotions().get(0));
		Matrix4 mx=THREE.Matrix4();
		mx.setRotationFromEuler(THREE.Vector3(Math.toRadians(23.1818),Math.toRadians(-34.8717),Math.toRadians(-20.0095)), "ZYX");
		Vector3 rot=THREE.Vector3(0, 0, 0);
		rot.setRotationFromMatrix(mx);
		double x=Math.toDegrees(rot.getX());
		double y=Math.toDegrees(rot.getY());
		double z=Math.toDegrees(rot.getZ());
		GWT.log(x+","+y+","+z);
		//jointMap.get("LeftUpLeg").setRotation(rot.getX(), rot.getY(), rot.getZ());
		//jointMap.get("LeftUpLeg").setRotation(Math.toRadians(23.1818),Math.toRadians(-34.8717),Math.toRadians(-20.0095));
		//jointMap.get("RightUpLeg").getRotation(Math.toRadians(23.1818),Math.toRadians(-34.8717),Math.toRadians(-20.0095));
		} catch (InvalidLineException e) {
			log(e.getMessage());
			e.printStackTrace();
		}
		*/
	//	ctime=System.currentTimeMillis();
	}
	
	private void doZYX(Object3D target,String lastOrder){
		Vector3 vec=target.getRotation();
		Matrix4 mx=THREE.Matrix4();
		mx.setRotationFromEuler(vec, lastOrder);
		
		vec.setRotationFromMatrix(mx);
	}
	/*
	private void doPose(BVH bvh,String line){
		String[] tmp=line.split(" ");
		double[] vs=BVHParser.toDouble(tmp);
		Object3D oldTarget=null;
		String lastOrder=null;
		for(int i=0;i<vs.length;i++){
			NameAndChannel nchannel=bvh.getNameAndChannels().get(i);
			lastOrder=nchannel.getOrder();
			Object3D target=jointMap.get(nchannel.getName());
			switch(nchannel.getChannel()){
			case Channels.XROTATION:
				target.getRotation().setX(Math.toRadians(vs[i]));
				
			break;
			case Channels.YROTATION:
				target.getRotation().setY(Math.toRadians(vs[i]));
			break;
			case Channels.ZROTATION:
				target.getRotation().setZ(Math.toRadians(vs[i]));
			break;
			}
			
			if(oldTarget!=null && oldTarget!=target){
				doZYX(oldTarget,lastOrder);
			}
			oldTarget=target;
		}
		doZYX(oldTarget,lastOrder);//do last one
	}*/
	
	private void doPose(BVH bvh,double[] vs){
		Object3D oldTarget=null;
		String lastOrder=null;
		for(int i=0;i<vs.length;i++){
			NameAndChannel nchannel=bvh.getNameAndChannels().get(i);
			lastOrder=nchannel.getOrder();
			Object3D target=jointMap.get(nchannel.getName());
			switch(nchannel.getChannel()){
			case Channels.XROTATION:
				target.getRotation().setX(Math.toRadians(vs[i]));
				
			break;
			case Channels.YROTATION:
				target.getRotation().setY(Math.toRadians(vs[i]));
			break;
			case Channels.ZROTATION:
				target.getRotation().setZ(Math.toRadians(vs[i]));
			break;
			case Channels.XPOSITION:
				if(translatePosition.getValue()){
				target.getPosition().setX(vs[i]);
				}else{
					target.getPosition().setX(0);	
				}
				break;
			case Channels.YPOSITION:
				if(translatePosition.getValue()){
				target.getPosition().setY(vs[i]);
				}else{
					target.getPosition().setY(0);	
				}
				break;
			case Channels.ZPOSITION:
				if(translatePosition.getValue()){
				target.getPosition().setZ(vs[i]);
				}else{
					target.getPosition().setZ(0);	
				}
				break;	
			}
			
			if(oldTarget!=null && oldTarget!=target){
				doZYX(oldTarget,lastOrder);
			}
			oldTarget=target;
		}
		doZYX(oldTarget,lastOrder);//do last one
	}
	
	private Map<String,Object3D> jointMap;
	
	public Mesh createLine(Vec3 from,Vec3 to){
		Geometry lineG = THREE.Geometry();
		lineG.vertices().push(THREE.Vertex(THREE.Vector3(from.getX(), from.getY(), from.getY())));
		lineG.vertices().push(THREE.Vertex(THREE.Vector3(to.getX(), to.getY(), to.getZ())));
		Mesh line=THREE.Line(lineG, THREE.LineBasicMaterial().color(0).build());
		return line;
	}
	public void doLog(Object3D parent,BVHNode node){
		GWT.log(node.getName()+","+node.getOffset()+",endsite:"+node.getEndSite());
		GWT.log(node.getChannels().toString());
		
		Object3D group=THREE.Object3D();
		Mesh mesh=THREE.Mesh(THREE.CubeGeometry(.3,.3, .3), THREE.MeshLambertMaterial().color(0xffffff).build());
		group.add(mesh);
		mesh.setName(node.getName());
		
		//initial position
		group.setPosition(THREE.Vector3(node.getOffset().getX(), node.getOffset().getY(), node.getOffset().getZ()));
		jointMap.put(node.getName(), group);
		
		Mesh l1=createLine(new Vec3(),node.getOffset());
		parent.add(l1);
		
		if(node.getEndSite()!=null){
			Mesh end=THREE.Mesh(THREE.CubeGeometry(.1, .1, .1), THREE.MeshBasicMaterial().color(0x008800).build());
			end.setPosition(THREE.Vector3(node.getEndSite().getX(), node.getEndSite().getY(), node.getEndSite().getZ()));
			group.add(end);
			Geometry lineG = THREE.Geometry();
			lineG.vertices().push(THREE.Vertex(THREE.Vector3(0, 0, 0)));
			lineG.vertices().push(THREE.Vertex(THREE.Vector3(node.getEndSite().getX(), node.getEndSite().getY(), node.getEndSite().getZ())));
			Mesh line=THREE.Line(lineG, THREE.LineBasicMaterial().color(0).build());
			group.add(line);
		}
		parent.add(group);
		List<BVHNode> joints=node.getJoints();
		if(joints!=null){
			for(BVHNode joint:joints){
			doLog(group,joint);
			}
		}
		
	}
	

	private Label loadingLabel=new Label();
	private CheckBox translatePosition;
	private HTML5InputRange YPosition;
	private HTML5InputRange meshScaleX;
	private HTML5InputRange meshScaleY;
	private HTML5InputRange meshScaleZ;
	private HTML5InputRange positionX;
	private HTML5InputRange positionY;
	private HTML5InputRange positionZ;
	private PopupPanel bottomPanel;
	protected boolean playing;

	private void loadBVH(String path){
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(path));
		loadingLabel.setText("loading-data");
			try {
				builder.sendRequest(null, new RequestCallback() {
					
					@Override
					public void onResponseReceived(Request request, Response response) {

						String bvhText=response.getText();
						//useless spend allmost time with request and spliting.
						parseBVH(bvhText);
						
					}
					
					
					

@Override
public void onError(Request request, Throwable exception) {
				// TODO Auto-generated method stub
				
}
				});
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	private void parseBVH(String bvhText){
		final BVHParser parser=new BVHParser();
		jointMap=new HashMap<String,Object3D>();
		try {
			bvh=parser.parse(bvhText);
			BVHNode node=bvh.getHiearchy();
			
			if(root!=null){
				scene.remove(root);
			}
			root=THREE.Object3D();
			scene.add(root);
			doLog(root,node);
			
			int poseIndex=0;
			if(ignoreFirst.getValue()){
				poseIndex=1;
			}
			
			updatePoseIndex(poseIndex);
			doPose(bvh,bvh.getMotion().getMotions().get(poseIndex));
			currentFrameRange.setMax(bvh.getMotion().getFrames()-1);
			
		} catch (InvalidLineException e) {
			log(e.getMessage());
		}
	}
	
	/* timer style
	 * parser.initialize();
						bvhText=bvhText.replace("\r", "");
						final String lines[]=bvhText.split("\n");
						final int pLine=lines.length/20;
Timer timer=new Timer(){
							int index=0;
							boolean parsing;
							@Override
							public synchronized void  run() {
								if(parsing){
									return;
								}
								parsing=true;
								loadingLabel.setText(index+"/"+lines.length);
								GWT.log("called:"+index+","+pLine);
							try {
								parser.parseLines(lines, index, index+pLine);
								if(index>=lines.length){
									//done
									bvh=parser.getBvh();
									BVHNode node=bvh.getHiearchy();
									
									if(root!=null){
										scene.remove(root);
									}
									root=THREE.Object3D();
									scene.add(root);
									doLog(root,node);
									
									doPose(bvh,bvh.getMotion().getMotions().get(0));
									poseIndex=0;
									cancel();
								}else{
									index+=pLine;
								}
							} catch (InvalidLineException e) {
								log(e.getMessage());
							}
							
							parsing=false;
						
							
							}
							
					};
						timer.scheduleRepeating(20);
						*/
	
	private long getFrameTime(int index){
		long time=(long) (bvh.getMotion().getFrameTime()*index*1000);
		return time;
	}

	private void createBottomPanel(){
		bottomPanel = new PopupPanel();
		bottomPanel.setVisible(true);
		bottomPanel.setSize("720px", "40px");
		HorizontalPanel pPanel=new HorizontalPanel();
		
		
		bottomPanel.add(pPanel);
		bottomPanel.show();
		super.leftBottom(bottomPanel);
		
		
		
		
		playing=true;
		final Button playButton=new Button("Play");
		playButton.setEnabled(false);
		final Button stopButton=new Button("Stop");
		
		playButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				clock.update();
				//ctime=System.currentTimeMillis()-getFrameTime();
				playing=true;
				playButton.setEnabled(false);
				stopButton.setEnabled(true);
				
			}
		});
		pPanel.add(playButton);
		
		
		stopButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				playing=false;
				playButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});
		
		pPanel.add(stopButton);
		
		
		
		currentFrameRange = new HTML5InputRange(0,1000,0);
		currentFrameRange.setWidth("500px");
		pPanel.add(currentFrameRange);
		
		currentFrameRange.addMouseUpHandler(new MouseUpHandler() {
			
			@Override
			public void onMouseUp(MouseUpEvent event) {
				
				updatePoseIndex(currentFrameRange.getValue());
			}
		});
		
		currentFrameLabel = new Label();
		pPanel.add(currentFrameLabel);
		
	}
	
	
	
	
	@Override
	public void createControl(Panel parent) {
		
		parent.add(loadingLabel);
		
		final ListBox list=new ListBox();
		//parent.add(list);
		/*
		list.addItem("12_01");
		list.addItem("12_02");
		list.addItem("12_03");
		list.addItem("13_01");
		list.addItem("13_02");
		list.addItem("13_03");
		list.addItem("13_04");
		list.addItem("13_05");
		list.addItem("13_06");
		list.addItem("13_07");
		list.addItem("13_08");
		list.addItem("13_09");
		list.addItem("13_10");
		*/
		/*
		for(int i=1;i<=73;i++){
			String ind=""+i;
			if(ind.length()<2){
				ind="0"+ind;
			}
			list.addItem("80_"+ind);
		}*/
		
		list.setSelectedIndex(0);
		list.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				doLoad(list.getItemText(list.getSelectedIndex()));
			}
		});
		
		translatePosition = new CheckBox("Translate Position");
		parent.add(translatePosition);
		
		ignoreFirst = new CheckBox("ignore First(Pose Frame)");
		ignoreFirst.setValue(true);
		parent.add(ignoreFirst);
		
		
		
		rotationRange = new HTML5InputRange(-180,180,0);
		parent.add(HTML5Builder.createRangeLabel("X-Rotate", rotationRange));
		parent.add(rotationRange);
		Button reset=new Button("zero");
		reset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				rotationRange.setValue(0);
			}
		});
		parent.add(reset);
		
		
		rotationYRange = new HTML5InputRange(-180,180,0);
		parent.add(HTML5Builder.createRangeLabel("Y-Rotate", rotationYRange));
		parent.add(rotationYRange);
		
		
		rotationZRange = new HTML5InputRange(-180,180,0);
		parent.add(HTML5Builder.createRangeLabel("Z-Rotate", rotationZRange));
		parent.add(rotationZRange);
		
		YPosition = new HTML5InputRange(-50,50,0);
		parent.add(HTML5Builder.createRangeLabel("Y-Position", YPosition));
		parent.add(YPosition);
		
		FileUpload file=new FileUpload();
		file.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				JsArray<File> files=FileUtils.toFile(event.getNativeEvent());
				GWT.log(files.get(0).getFileName());
				GWT.log(files.get(0).getFileType());
				GWT.log(""+files.get(0).getFileSize());
				log(event.getNativeEvent());
				final FileReader reader=FileReader.createFileReader();
				reader.setOnLoad(new FileHandler() {
					@Override
					public void onLoad() {
						//GWT.log(reader.getResultAsString());
						parseBVH(reader.getResultAsString());
					}
				});
				reader.readAsText(files.get(0),"utf-8");
			}
		});
		parent.add(file);
		
		
		
		meshScaleX = new HTML5InputRange(0,150,3);
		parent.add(HTML5Builder.createRangeLabel("Scale-x", meshScaleX));
		parent.add(meshScaleX);
		
		meshScaleY = new HTML5InputRange(0,150,3);
		parent.add(HTML5Builder.createRangeLabel("Scale-y", meshScaleY));
		parent.add(meshScaleY);
		
		meshScaleZ = new HTML5InputRange(0,150,3);
		parent.add(HTML5Builder.createRangeLabel("Scale-z", meshScaleZ));
		parent.add(meshScaleZ);
		
		positionX = new HTML5InputRange(-150,150,0);
		parent.add(HTML5Builder.createRangeLabel("Position-x", positionX));
		parent.add(positionX);
		
		positionY = new HTML5InputRange(-150,150,0);
		parent.add(HTML5Builder.createRangeLabel("Position-y", positionY));
		parent.add(positionY);
		
		positionZ = new HTML5InputRange(-150,150,0);
		parent.add(HTML5Builder.createRangeLabel("Position-z", positionZ));
		parent.add(positionZ);
		
		createBottomPanel();
	}

	protected void doLoad(String itemText) {
		String[] g_n=itemText.split("_");
		loadBVH("bvhs/"+g_n[0]+"/"+itemText+".bvh");
	}

	private HTML5InputRange rotationRange;
	private HTML5InputRange rotationYRange;
	private HTML5InputRange rotationZRange;

	Object3D root;
	
	
	private BVH bvh;
	private CheckBox ignoreFirst;
	
	//private long ctime;
	private HTML5InputRange currentFrameRange;
	private Label currentFrameLabel;
	//private int poseIndex;
	
	private void updatePoseIndex(int index){
		//poseIndex=index;
		currentFrameRange.setValue(index);
		currentFrameLabel.setText((index+1)+"/"+bvh.getMotion().size());
		doPose(bvh,bvh.getMotion().getMotions().get(index));
	}
	
	Clock clock=new Clock();
	@Override
	protected void beforeUpdate(WebGLRenderer renderer) {
		if(selectedObject!=null){
			double sx=(double)meshScaleX.getValue()/10;
			double sy=(double)meshScaleY.getValue()/10;
			double sz=(double)meshScaleZ.getValue()/10;
			double px=(double)positionX.getValue()/10;
			double py=(double)positionY.getValue()/10;
			double pz=(double)positionZ.getValue()/10;
			selectedObject.setScale(sx, sy, sz);
			selectedObject.setPosition(px,py,pz);
		}
		
		
		cameraY=YPosition.getValue();
		if(root!=null){
		root.getRotation().set(Math.toRadians(rotationRange.getValue()),Math.toRadians(rotationYRange.getValue()),Math.toRadians(rotationZRange.getValue()));
		}
		
		if(bvh!=null){
			if(playing){
				long last=clock.delta();
				int frame=(int) ((double)last/(bvh.getMotion().getFrameTime()*1000));//not good at sync
			//	log(""+frame);
			if(frame>0){
			int index=currentFrameRange.getValue()+frame;
			
			index%=bvh.getMotion().getFrames();
			
			if(ignoreFirst.getValue() && index==0){
				index=1;
			}
			
			updatePoseIndex(index);		
			}
		/*		
		double delta=(double)(System.currentTimeMillis()-ctime)/1000;
		delta%=bvh.getMotion().getDuration();
		poseIndex = (int) (delta/bvh.getMotion().getFrameTime());
		*/
			}
	
		}
	}
	
	@Override
	public void resized(int width, int height) {
		super.resized(width, height);
		leftBottom(bottomPanel);
	}

}
