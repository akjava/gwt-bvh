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

public class Vec3 {
public Vec3(){
	this(0,0,0);
}
public Vec3(double x2, double y2, double z2) {
		this.x=x2;
		this.y=y2;
		this.z=z2;
	}
private double x;
public double getX() {
	return x;
}
public void setX(double x) {
	this.x = x;
}
public double getY() {
	return y;
}
public void setY(double y) {
	this.y = y;
}
public double getZ() {
	return z;
}
public void setZ(double z) {
	this.z = z;
}
private double y;
private double z;
public String toString(){
	return "("+x+","+y+","+z+")";
}
}
