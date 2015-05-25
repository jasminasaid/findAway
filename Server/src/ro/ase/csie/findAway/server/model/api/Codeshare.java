package ro.ase.csie.findAway.server.model.api;

import javax.xml.bind.annotation.XmlAttribute;

public class Codeshare {
	protected int id;

	protected String airline;

	protected String flight;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute
	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	@XmlAttribute
	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

}
