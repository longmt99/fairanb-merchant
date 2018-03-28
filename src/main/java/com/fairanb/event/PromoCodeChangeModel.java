package com.fairanb.event;

public class PromoCodeChangeModel {

	private String type;
	private String action;
	private Long promoCodeId;
	private String correlationId;

	public PromoCodeChangeModel(String type, String action, Long promoCodeId, String correlationId) {
		super();
		this.type = type;
		this.action = action;
		this.promoCodeId = promoCodeId;
		this.correlationId = correlationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getPromoCodeId() {
		return promoCodeId;
	}

	public void setPromoCodeId(Long promoCodeId) {
		this.promoCodeId = promoCodeId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

}
