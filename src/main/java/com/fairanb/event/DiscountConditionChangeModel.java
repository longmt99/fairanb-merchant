package com.fairanb.event;

public class DiscountConditionChangeModel {

	private String type;
	private String action;
	private Long discountConditionId;
	private String correlationId;

	public DiscountConditionChangeModel(String type, String action, Long discountConditionId, String correlationId) {
		super();
		this.type = type;
		this.action = action;
		this.discountConditionId = discountConditionId;
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

	public Long getDiscountConditionId() {
		return discountConditionId;
	}

	public void setDiscountConditionId(Long discountConditionId) {
		this.discountConditionId = discountConditionId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

}
