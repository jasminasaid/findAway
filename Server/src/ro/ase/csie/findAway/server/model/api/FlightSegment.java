package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class FlightSegment {
	protected int id;

	protected String kind;

	protected Boolean isMajor;

	protected float distance;

	protected float duration;

	protected float transferDuration;

	protected String sCode;

	protected String tCode;

	protected IndicativePrice indicativePrice;

	protected List<FlightItinerary> itineraries;

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
	public Boolean getIsMajor() {
		return isMajor;
	}

	public void setIsMajor(Boolean isMajor) {
		this.isMajor = isMajor;
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
	public float getTransferDuration() {
		return transferDuration;
	}

	public void setTransferDuration(float transferDuration) {
		this.transferDuration = transferDuration;
	}

	@XmlAttribute
	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	@XmlAttribute
	public String gettCode() {
		return tCode;
	}

	public void settCode(String tCode) {
		this.tCode = tCode;
	}

	@XmlElement(name = "IndicativePrice")
	public IndicativePrice getIndicativePrice() {
		return indicativePrice;
	}

	public void setIndicativePrice(IndicativePrice indicativePrice) {
		this.indicativePrice = indicativePrice;
	}

	@XmlElement(name = "FlightItinerary")
	public List<FlightItinerary> getItineraries() {
		return itineraries;
	}

	public void setItineraries(List<FlightItinerary> itineraries) {
		this.itineraries = itineraries;
	}

}
