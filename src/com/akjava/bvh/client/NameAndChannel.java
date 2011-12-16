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
