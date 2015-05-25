package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TransitLeg {
	protected int id;

	protected String url;

	protected List<TransitHop> hops;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlElement(name = "TransitHop")
	public List<TransitHop> getHops() {
		return hops;
	}

	public void setHops(List<TransitHop> hops) {
		this.hops = hops;
	}

}
