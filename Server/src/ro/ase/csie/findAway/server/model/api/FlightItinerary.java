package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class FlightItinerary {
	protected int id;

	protected List<FlightLeg> legs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement(name = "FlightLeg")
	public List<FlightLeg> getLegs() {
		return legs;
	}

	public void setLegs(List<FlightLeg> legs) {
		this.legs = legs;
	}

}
