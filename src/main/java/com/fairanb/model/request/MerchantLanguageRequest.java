package com.fairanb.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * @longmt99
 */
@EqualsAndHashCode
@Getter
@Setter
@Data
public class MerchantLanguageRequest {
	private Long id;
	@Column(name = "merchant_id")
	private Long merchantId;

	@Column(name = "language_id")
	private Long languageId;
}
