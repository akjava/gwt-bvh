package com.akjava.bvh.client;

public class NameAndChannel {
private String name;
private int channelType;
private Channels channels;
public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public int getChannel() {
	return channelType;
}

public void setChannel(int channel) {
	this.channelType = channel;
}

public NameAndChannel(String name,int channel,Channels channels){
	this.name=name;
	this.channelType=channel;
	this.channels=channels;
}
public String getOrder(){
	return channels.getOrder();
}

}
