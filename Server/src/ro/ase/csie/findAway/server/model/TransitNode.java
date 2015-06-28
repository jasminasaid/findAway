package ro.ase.csie.findAway.server.model;

import java.io.Serializable;

public class TransitNode extends PathNode implements Serializable,
		Comparable<TransitNode> {

	private static final long serialVersionUID = 1L;

	protected int transitHopID;
	protected String sName;
	// protected Position sPos;
	protected String tName;
	// protected Position tPos;
	protected float frequency;
	// protected float duration;
	// protected float price;
	protected int transitLegID;

	public int getTransitHopID() {
		return transitHopID;
	}

	public void setTransitHopID(int transitHopID) {
		this.transitHopID = transitHopID;
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

	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public int getTransitLegID() {
		return transitLegID;
	}

	public void setTransitLegID(int transitLegID) {
		this.transitLegID = transitLegID;
	}

	@Override
	public void printNode() {
		System.out.println("Transit from " + this.sName + " to " + this.tName
				+ " at " + this.getPrice() + "�, in " + this.getDuration() + " min");
	}

	@Override
	public int compareTo(TransitNode tn1) {
		if (this.transitHopID == tn1.transitHopID)
			return 0;
		return (this.transitHopID < tn1.transitHopID) ? -1 : 1;
	}

}
