package ro.ase.csie.findAway.server.model.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

public class Airport implements Serializable, Comparable<Airport> {

	private static final long serialVersionUID = 1L;

	protected int id;

	protected String code;

	protected String name;

	protected String pos;

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
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		final int hash = 31;
		int res = 1;
		res = hash * res + this.id;
		return res;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Airport airport = (Airport) obj;
		if (this.getId() != airport.getId())
			return false;
		return true;
	}

	@Override
	public int compareTo(Airport arp) {
		if (this.getId() == arp.getId())
			return 0;
		return (this.getId() < arp.getId()) ? -1 : 1;
	}

	// @Override
	// public void printNode() {
	// System.out.println(this.getClass() + ":   " + this.getCode() + " -- "
	// + this.getName());
	// }

	public Airport(int id) {
		this.id = id;
	}

	public Airport() {
		super();
	}

}
