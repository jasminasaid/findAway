package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "SearchResponse", namespace = "http://www.rome2rio.com/api/1.2/xml")
public class SearchResponse {

	protected int id;
	protected List<Place> places;
	protected List<Airport> airports;
	protected List<Airline> airlines;
	protected List<Aircraft> aircrafts;
	protected List<Agency> agencies;
	protected List<Route> routes;
	protected String data;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement(name = "Place")
	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	@XmlElement(name = "Airport")
	public List<Airport> getAirports() {
		return airports;
	}

	public void setAirports(List<Airport> airports) {
		this.airports = airports;
	}

	@XmlElement(name = "Airline")
	public List<Airline> getAirlines() {
		return airlines;
	}

	public void setAirlines(List<Airline> airlines) {
		this.airlines = airlines;
	}

	@XmlElement(name = "Aircraft")
	public List<Aircraft> getAircrafts() {
		return aircrafts;
	}

	public void setAircrafts(List<Aircraft> aircrafts) {
		this.aircrafts = aircrafts;
	}

	@XmlElement(name = "Agency")
	public List<Agency> getAgencies() {
		return agencies;
	}

	public void setAgencies(List<Agency> agencies) {
		this.agencies = agencies;
	}

	@XmlElement(name = "Route")
	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
