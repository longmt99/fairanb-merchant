package com.fairanb.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class DiscountConditionRequest {

	@ApiModelProperty(position = 1)
	private Long id;

	private Long discountDefinitionId;

	private Long discountLevelId;

	private BigDecimal amount;
}
