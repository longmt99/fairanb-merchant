package com.fairanb.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * @Longmt99
 */
@EqualsAndHashCode
@Getter
@Setter
@Data
public class MerchantConfigRequest {
	@ApiModelProperty(position =1)
	private Long id;

	private Long merchantId;

	private String configKey;

	private String type;

	private String value;

	private String status;

}
