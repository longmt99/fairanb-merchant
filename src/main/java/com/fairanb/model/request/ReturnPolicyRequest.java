package com.fairanb.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnPolicyRequest {
	@NotNull(message = "'name' is required")
	private String name;

	private String description;

	private Boolean isDefault;

	@NotNull(message = "'merchantId' is required")
	private Long merchantId;

	@NotNull(message = "'contactWithin' is required")
	private Integer contactWithin;

	@NotNull(message = "'returnTypeId' is required")
	private Long returnTypeId;

	@NotNull(message = "'returnShippingType' is required")
	private String returnShippingType;

	@NotNull(message = "'restockingFee' is required")
	private Integer restockingFee;
}
