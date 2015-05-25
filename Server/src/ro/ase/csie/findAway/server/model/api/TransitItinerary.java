package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class TransitItinerary {
	protected int id;

	protected List<TransitLeg> legs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement(name = "TransitLeg")
	public List<TransitLeg> getLegs() {
		return legs;
	}

	public void setLegs(List<TransitLeg> legs) {
		this.legs = legs;
	}

}
