package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "return_policy")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class ReturnPolicy extends BaseEntity {
	@Basic(optional = false)
	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Basic(optional = false)
	@Column(name = "value")
	private String value;

	@Column(name = "is_default")
	private Boolean isDefault;

	@Basic(optional = false)
	@Column(name = "merchant_id")
	private Long merchantId;

	@Data
	public static class ValueDto {
		private Integer contactWithin;
		private String returnType;
		private String returnShippingType;
		private Integer restockingFee;
	}
}
