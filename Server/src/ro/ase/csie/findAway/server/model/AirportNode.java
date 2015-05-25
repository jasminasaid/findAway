package ro.ase.csie.findAway.server.model;

import java.io.Serializable;

import ro.ase.csie.findAway.server.model.api.Airport;
import ro.ase.csie.findAway.server.model.api.Position;

public class AirportNode extends PathNode implements Serializable,
		Comparable<AirportNode> {

	private static final long serialVersionUID = 1L;

	protected Airport source;
//	protected Position sPos;
	protected Airport tDest;
//	protected Position tPos;
	protected Airport fDest;
	protected Position fPos;
//	protected float price;
//	protected float duration;
	protected boolean hasStop;
	protected int indicativePriceID;
	protected int flightLegID;
	protected int flightHopID;

	public Airport getSource() {
		return source;
	}

	public void setSource(Airport source) {
		this.source = source;
	}

	public Airport gettDest() {
		return tDest;
	}

	public void settDest(Airport tDest) {
		this.tDest = tDest;
	}

	public Airport getfDest() {
		return fDest;
	}

	public void setfDest(Airport fDest) {
		this.fDest = fDest;
	}

	public int getIndicativePriceID() {
		return indicativePriceID;
	}

	public void setIndicativePriceID(int indicativePriceID) {
		this.indicativePriceID = indicativePriceID;
	}

	public int getFlightLegID() {
		return flightLegID;
	}

	public void setFlightLegID(int flightLegID) {
		this.flightLegID = flightLegID;
	}

	public boolean isHasStop() {
		return hasStop;
	}

	public void setHasStop(boolean hasStop) {
		this.hasStop = hasStop;
	}

	public int getFlightHopID() {
		return flightHopID;
	}

	public void setFlightHopID(int flightHopID) {
		this.flightHopID = flightHopID;
	}

	public Position getfPos() {
		return fPos;
	}

	public void setfPos(Position fPos) {
		this.fPos = fPos;
	}

	@Override
	public int hashCode() {
		final int hash = 31;
		int res = 1;
		res = hash
				* res
				+ (this.getSource().getId() + this.gettDest().getId() + this
						.getfDest().getId());
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		AirportNode airportNode = (AirportNode) obj;
		if (this.getSource().getId() != airportNode.getSource().getId()
				|| this.gettDest().getId() != airportNode.gettDest().getId()
				|| this.getfDest().getId() != airportNode.getfDest().getId())
			return false;
		return true;
	}

	@Override
	public int compareTo(AirportNode an) {
		// if (this.getFlightHopID() == an.getFlightHopID())
		// return 0;
		// return (this.getFlightHopID() < an.getFlightHopID()) ? -1 : 1;
		if (this.getSource().getId() == an.getSource().getId()
				&& this.gettDest().getId() == an.gettDest().getId()
				&& this.getfDest().getId() == an.getfDest().getId())
			return 0;
		if (this.getSource().getId() < an.getSource().getId()
				|| this.gettDest().getId() < an.gettDest().getId()
				|| this.getfDest().getId() < an.getfDest().getId())
			return -1;
		else
			return 1;
	}

	@Override
	public void printNode() {
		System.out.println("Fly from " + this.getSource().getCode() + " - "
				+ this.getSource().getName() + " to "
				+ this.getfDest().getCode() + " - " + this.getfDest().getName()
				+ " through " + this.gettDest().getCode() + " at "
				+ this.getPrice() + "€, in " + this.getDuration()
				+ " min, FH - " + this.getFlightHopID());

	}

}
