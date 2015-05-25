package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class FlightLeg {
	protected int id;

	protected String days;

	protected List<FlightHop> hops;

	protected IndicativePrice indicativePrice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@XmlElement(name = "FlightHop")
	public List<FlightHop> getHops() {
		return hops;
	}

	public void setHops(List<FlightHop> hops) {
		this.hops = hops;
	}

	@XmlElement(name = "IndicativePrice")
	public IndicativePrice getIndicativePrice() {
		return indicativePrice;
	}

	public void setIndicativePrice(IndicativePrice indicativePrice) {
		this.indicativePrice = indicativePrice;
	}

}
