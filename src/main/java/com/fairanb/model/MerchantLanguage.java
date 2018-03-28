package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @longmt99
 */
@Entity
@Table(name="merchant_language")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class MerchantLanguage extends BaseEntity {

	@Column(name = "merchant_id")
	private Long merchantId;

	@Column(name = "language_id")
	private Long languageId;

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
