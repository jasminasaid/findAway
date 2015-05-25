package ro.ase.csie.findAway.server.model.api;

import javax.xml.bind.annotation.*;

public class Agency {
	protected int id;

	protected String code;

	protected String name;

	protected String url;

	protected String iconPath;

	protected String iconSize;

	protected String iconOffset;

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
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@XmlAttribute
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	@XmlAttribute
	public String getIconSize() {
		return iconSize;
	}

	public void setIconSize(String iconSize) {
		this.iconSize = iconSize;
	}

	@XmlAttribute
	public String getIconOffset() {
		return iconOffset;
	}

	public void setIconOffset(String iconOffset) {
		this.iconOffset = iconOffset;
	}

}
