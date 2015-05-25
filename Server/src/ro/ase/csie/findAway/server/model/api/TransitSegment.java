package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TransitSegment {
	protected int id;

	protected String kind;

	protected String subkind;

	protected String vehicle;

	protected Boolean isMajor;

	protected Boolean isImperial;

	protected float distance;

	protected float duration;

	protected String sName;

	protected String sPos;

	protected String tName;

	protected String tPos;

	protected IndicativePrice indicativePrice;

	protected List<TransitItinerary> itineraries;

	protected List<Stop> stops;

	protected String path;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	@XmlAttribute
	public String getSubkind() {
		return subkind;
	}

	public void setSubkind(String subkind) {
		this.subkind = subkind;
	}

	@XmlAttribute
	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	@XmlAttribute
	public Boolean getIsMajor() {
		return isMajor;
	}

	public void setIsMajor(Boolean isMajor) {
		this.isMajor = isMajor;
	}

	@XmlAttribute
	public Boolean getIsImperial() {
		return isImperial;
	}

	public void setIsImperial(Boolean isImperial) {
		this.isImperial = isImperial;
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

	@XmlAttribute
	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	@XmlAttribute
	public String getsPos() {
		return sPos;
	}

	public void setsPos(String sPos) {
		this.sPos = sPos;
	}

	@XmlAttribute
	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	@XmlAttribute
	public String gettPos() {
		return tPos;
	}

	public void settPos(String tPos) {
		this.tPos = tPos;
	}

	@XmlElement(name = "IndicativePrice")
	public IndicativePrice getIndicativePrice() {
		return indicativePrice;
	}

	public void setIndicativePrice(IndicativePrice indicativePrice) {
		this.indicativePrice = indicativePrice;
	}

	@XmlElement(name = "TransitItinerary")
	public List<TransitItinerary> getItineraries() {
		return itineraries;
	}

	public void setItineraries(List<TransitItinerary> itineraries) {
		this.itineraries = itineraries;
	}

	@XmlElement(name = "Stop")
	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	@XmlAttribute
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
