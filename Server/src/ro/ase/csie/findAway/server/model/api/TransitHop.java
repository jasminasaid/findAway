package ro.ase.csie.findAway.server.model.api;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class TransitHop {
	protected int id;

	protected String sName;

	protected String sPos;

	protected String tName;

	protected String tPos;

	protected float frequency;

	protected float duration;

	protected IndicativePrice indicativePrice;

	protected List<TransitLine> lines;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@XmlElement(name = "IndicativePrice")
	public IndicativePrice getIndicativePrice() {
		return indicativePrice;
	}

	public void setIndicativePrice(IndicativePrice indicativePrice) {
		this.indicativePrice = indicativePrice;
	}

	@XmlElement(name = "TransitLine")
	public List<TransitLine> getLines() {
		return lines;
	}

	public void setLines(List<TransitLine> lines) {
		this.lines = lines;
	}

}
