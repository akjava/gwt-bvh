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
