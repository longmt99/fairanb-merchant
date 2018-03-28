package com.fairanb.event;

public class DiscountResultChangeModel {

	private String type;
	private String action;
	private Long discountResultId;
	private String correlationId;

	public DiscountResultChangeModel(String type, String action, Long discountResultId, String correlationId) {
		super();
		this.type = type;
		this.action = action;
		this.discountResultId = discountResultId;
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

	public Long getDiscountResultId() {
		return discountResultId;
	}

	public void setDiscountResultId(Long discountResultId) {
		this.discountResultId = discountResultId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

}
