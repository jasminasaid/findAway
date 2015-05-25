package ro.ase.csie.findAway.server.model.api;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Position implements Serializable {

	private static final long serialVersionUID = 1L;

	protected double lat;
	protected double lon;

	public Position(String positionString) {
		if (positionString == null)
			throw new NullPointerException("Null position string");
		if (positionString.isEmpty())
			throw new IllegalArgumentException("Empty position string");

		String pos[] = positionString.split(",");

		if (pos.length != 2)
			throw new NumberFormatException();

		try {
			double lat = Double.parseDouble(pos[0]);
			double lon = Double.parseDouble(pos[1]);

			if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0)
				throw new IllegalArgumentException(
						"Coordinates out of range. Latitude should pe between -90.0 and 90.0 and longitude between -180.0 and 180.0");
			this.lat = lat;
			this.lon = lon;
		} catch (NumberFormatException ex) {
			throw ex;
		}
	}

	public Position(double[] positions) {
		if (positions.length != 2)
			throw new IllegalArgumentException(
					"Invalid length for positions array - must be 2");
		if (positions[0] < -90.0 || positions[0] > 90.0
				|| positions[1] < -180.0 || positions[1] > 180.0)
			throw new IllegalArgumentException(
					"Coordinates out of range. Latitude should pe between -90.0 and 90.0 and longitude between -180.0 and 180.0");
		this.lat = positions[0];
		this.lon = positions[1];
	}

	public Position(float lat, float lon) {
		if (lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0)
			throw new IllegalArgumentException(
					"Coordinates out of range. Latitude should pe between -90.0 and 90.0 and longitude between -180.0 and 180.0");
		this.lat = (double) lat;
		this.lon = (double) lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double[] getPositionArray() {
		return new double[] { this.lat, this.lon };
	}

	public double getDistance(Position pos) {
		if (this == null || pos == null)
			throw new NullPointerException("Null Position object");

		final int R = 6371; // Radius of the earth

		Double latDistance = deg2rad(pos.lat - this.lat);
		Double lonDistance = deg2rad(pos.lon - this.lon);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(deg2rad(this.lat)) * Math.cos(deg2rad(pos.lat))
				* Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c; // in km

		return distance;
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	@Override
	public String toString() {
		return this.lat + "," + this.lon;
	}

	public boolean isSame(Position pos) {
		if (this == null || pos == null)
			throw new NullPointerException("Null position object");
		return (this.lat == pos.lat && this.lon == pos.lon);
	}

}
