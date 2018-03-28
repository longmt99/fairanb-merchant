package com.fairanb.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @longmt99
 */
@Entity
@Table(name="merchant_config")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class MerchantConfig extends BaseEntity {


	@Column(name="merchant_id")
	private Long merchantId;

	@Column(name = "config_key")
	private String configKey;

	@Column(name = "type")
	private String type;

	@Lob
	@Column(name = "value")
	private String value;

	@Column(name = "status")
	private String status;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}
}
