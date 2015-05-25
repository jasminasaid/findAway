package ro.ase.csie.findAway.server.model.api;

import javax.xml.bind.annotation.XmlAttribute;

public class IndicativePrice {
	protected int id;

	protected float price;

	protected String currency;

	protected float nativePrice;

	protected String nativeCurrency;

	protected Boolean isFreeTransfer;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@XmlAttribute
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@XmlAttribute
	public float getNativePrice() {
		return nativePrice;
	}

	public void setNativePrice(float nativePrice) {
		this.nativePrice = nativePrice;
	}

	@XmlAttribute
	public String getNativeCurrency() {
		return nativeCurrency;
	}

	public void setNativeCurrency(String nativeCurrency) {
		this.nativeCurrency = nativeCurrency;
	}

	@XmlAttribute
	public Boolean getIsFreeTransfer() {
		return isFreeTransfer;
	}

	public void setIsFreeTransfer(Boolean isFreeTransfer) {
		this.isFreeTransfer = isFreeTransfer;
	}

}
