package com.fairanb.model;

import com.fairanb.model.response.DiscountConditionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "discount_condition")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class DiscountCondition extends BaseEntity {

	@Column(name = "amount", columnDefinition = "decimal", precision = 10, scale = 2)
	private BigDecimal amount = new BigDecimal(0);

	@Column(name = "discount_level_id")
	private Long discountLevelId;

	@Column(name = "discount_definition_id")
	private Long discountDefinitionId;

	@Column(name = "status")
	private String status;

	public DiscountConditionResponse getResponse() {
		DiscountConditionResponse response = new DiscountConditionResponse();
		ModelMapper modelMapper = new ModelMapper();
		BeanUtils.copyProperties(this, response);
		return response;
	}
}
