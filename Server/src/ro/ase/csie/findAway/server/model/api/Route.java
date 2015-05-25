package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Route {
	protected int id;

	protected String name;

	protected float distance;

	protected float duration;

	protected List<Stop> stops;

	protected IndicativePrice indicativePrice;

	protected List<TransitSegment> transitSegments;

	protected List<FlightSegment> flightSegments;

	protected List<CarSegment> carSegments;

	protected List<WalkSegment> walkSegments;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	@XmlAttribute
	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	@XmlElement(name = "Stop")
	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	@XmlElement(name = "IndicativePrice")
	public IndicativePrice getIndicativePrice() {
		return indicativePrice;
	}

	public void setIndicativePrice(IndicativePrice indicativePrice) {
		this.indicativePrice = indicativePrice;
	}

	@XmlElement(name = "TransitSegment")
	public List<TransitSegment> getTransitSegments() {
		return transitSegments;
	}

	public void setTransitSegments(List<TransitSegment> transitSegments) {
		this.transitSegments = transitSegments;
	}

	@XmlElement(name = "FlightSegment")
	public List<FlightSegment> getFlightSegments() {
		return flightSegments;
	}

	public void setFlightSegments(List<FlightSegment> flightSegments) {
		this.flightSegments = flightSegments;
	}

	@XmlElement(name = "CarSegment")
	public List<CarSegment> getCarSegments() {
		return carSegments;
	}

	public void setCarSegments(List<CarSegment> carSegments) {
		this.carSegments = carSegments;
	}

	@XmlElement(name = "WalkSegment")
	public List<WalkSegment> getWalkSegments() {
		return walkSegments;
	}

	public void setWalkSegments(List<WalkSegment> walkSegments) {
		this.walkSegments = walkSegments;
	}

}
