package ro.ase.csie.findAway.server.model.api;

import javax.xml.bind.annotation.XmlAttribute;

public class Place {
	protected int id;

	protected String kind;

	protected String longName;

	protected String shortName;

	protected String pos;

	protected float lat;

	protected float lng;

	protected String countryCode;

	protected String regionCode;

	protected String timeZone;

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
	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	@XmlAttribute
	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	@XmlAttribute
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@XmlAttribute
	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	@XmlAttribute
	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	@XmlAttribute
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@XmlAttribute
	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	@XmlAttribute
	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return this.longName + " - " + this.kind + " - "
				+ new Position(this.lat, this.lng);
	}

}
