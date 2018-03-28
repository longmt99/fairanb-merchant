package com.fairanb.event;

public class DiscountDefinitionChangeModel {

	private String type;
	private String action;
	private Long discountDefinitionId;
	private String correlationId;

	public DiscountDefinitionChangeModel(String type, String action, Long discountDefinitionId, String correlationId) {
		super();
		this.type = type;
		this.action = action;
		this.discountDefinitionId = discountDefinitionId;
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

	public Long getDiscountDefinitionId() {
		return discountDefinitionId;
	}

	public void setDiscountDefinitionId(Long discountDefinitionId) {
		this.discountDefinitionId = discountDefinitionId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

}
