package com.fairanb.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "gift_card")
@Data
public class GiftCard extends BaseEntity {
	@Basic(optional = false)
	@Column(name = "code")
	private String code;

	@Basic(optional = false)
	@Column(name = "merchant_id")
	private Long merchantId;

	@Basic(optional = false)
	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name="start_date")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date startDate;

	@Column(name="end_date")
	@Temporal(TemporalType.TIMESTAMP)
	protected Date endDate;

	@Column(name = "balance")
	private BigDecimal balance;

	@Column(name = "purchase_amount")
	private BigDecimal purchaseAmount;

	@Basic(optional = false)
	@Column(name = "security_code")
	private String securityCode;

	@Column(name = "status")
	private String status;
}
