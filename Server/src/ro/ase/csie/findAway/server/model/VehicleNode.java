package ro.ase.csie.findAway.server.model;

import java.io.Serializable;

public class VehicleNode extends PathNode implements Serializable,
		Comparable<VehicleNode> {

	private static final long serialVersionUID = 1L;

	protected int segmentID;
	protected String kind;
	protected String subkind;
	protected String vehicle;
	protected String sName;
	// protected Position sPos;
	protected String tName;
	// protected Position tPos;
	protected float distance;
	// protected float duration;
	// protected float price;
	protected String path;
	protected int routeID;

	public int getSegmentID() {
		return segmentID;
	}

	public void setSegmentID(int segmentID) {
		this.segmentID = segmentID;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getSubkind() {
		return subkind;
	}

	public void setSubkind(String subkind) {
		this.subkind = subkind;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getRouteID() {
		return routeID;
	}

	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}

	@Override
	public void printNode() {
		System.out.println("Vehicle from " + this.sName + " to " + this.tName
				+ " by " + this.getSubkind() + " at " + this.getPrice()
				+ "�, in " + this.getDuration() + " min");

	}

	@Override
	public int compareTo(VehicleNode vn1) {
		if (this.segmentID == vn1.segmentID)
			return 0;
		return (this.segmentID < vn1.segmentID) ? -1 : 1;
	}

}
