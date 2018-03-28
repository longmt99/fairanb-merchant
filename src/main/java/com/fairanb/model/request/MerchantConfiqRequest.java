package com.fairanb.model.request;

import com.fairanb.model.MerchantConfig;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Lob;

/**
 * @longmt99
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class MerchantConfiqRequest {
	private Long id;
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



	public MerchantConfiqRequest() {
	}

	public MerchantConfig copyBean(MerchantConfiqRequest request) {
		MerchantConfig merchantConfiq = new MerchantConfig();
		BeanUtils.copyProperties(request, merchantConfiq);
		return merchantConfiq;
	}

	public MerchantConfiqRequest copyRequest(MerchantConfig merchantConfiq) {
		BeanUtils.copyProperties(merchantConfiq, this);
		return this;
	}
}
