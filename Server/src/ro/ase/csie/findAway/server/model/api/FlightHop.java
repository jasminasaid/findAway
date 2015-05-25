package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class FlightHop {
	protected int id;

	protected String sCode;

	protected String tCode;

	protected String sTerminal;

	protected String tTerminal;

	protected String sTime;

	protected String tTime;

	protected String flight;

	protected String airline;

	protected float duration;

	protected float lDuration;

	protected String aircraft;

	protected List<Codeshare> shares;

	protected int dayChange;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@XmlAttribute
	public String getsTerminal() {
		return sTerminal;
	}

	public void setsTerminal(String sTerminal) {
		this.sTerminal = sTerminal;
	}

	@XmlAttribute
	public String gettTerminal() {
		return tTerminal;
	}

	public void settTerminal(String tTerminal) {
		this.tTerminal = tTerminal;
	}

	@XmlAttribute
	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	@XmlAttribute
	public String gettTime() {
		return tTime;
	}

	public void settTime(String tTime) {
		this.tTime = tTime;
	}

	@XmlAttribute
	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	@XmlAttribute
	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	@XmlAttribute
	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	@XmlAttribute
	public float getlDuration() {
		return lDuration;
	}

	public void setlDuration(float lDuration) {
		this.lDuration = lDuration;
	}

	@XmlAttribute
	public String getAircraft() {
		return aircraft;
	}

	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}

	@XmlElement(name = "Codeshare")
	public List<Codeshare> getShares() {
		return shares;
	}

	public void setShares(List<Codeshare> shares) {
		this.shares = shares;
	}

	@XmlAttribute
	public int getDayChange() {
		return dayChange;
	}

	public void setDayChange(int dayChange) {
		this.dayChange = dayChange;
	}

}
