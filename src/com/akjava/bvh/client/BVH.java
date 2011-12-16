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


public class BVH {
private BVHNode hiearchy;
private Motion motion;
public Motion getMotion() {
	return motion;
}

public void setMotion(Motion motion) {
	this.motion = motion;
}

public BVHNode getHiearchy() {
	return hiearchy;
}

public void setHiearchy(BVHNode hiearchy) {
	this.hiearchy = hiearchy;
}
public List<NameAndChannel> nameAndChannels=new ArrayList<NameAndChannel>();
public List<NameAndChannel> getNameAndChannels() {
	return nameAndChannels;
}

public void add(NameAndChannel nc){
	nameAndChannels.add(nc);
}
}
