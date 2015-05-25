package ro.ase.csie.findAway.server.model.api;

import javax.xml.bind.annotation.XmlAttribute;

public class TransitLine {
	protected int id;

	protected String name;

	protected String vehicle;

	protected String code;

	protected String agency;

	protected float frequency;

	protected float duration;

	protected String days;

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
	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	@XmlAttribute
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlAttribute
	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	@XmlAttribute
	public float getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	@XmlAttribute
	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	@XmlAttribute
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

}
