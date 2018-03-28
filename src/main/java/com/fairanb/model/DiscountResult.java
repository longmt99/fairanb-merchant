package com.fairanb.model;

import com.fairanb.model.response.DiscountResultResponse;
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
@Table(name = "discount_result")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class DiscountResult extends BaseEntity {

	@Column(name = "amount_off", columnDefinition = "decimal", precision = 19, scale = 2)
	private BigDecimal amountOff;

	@Column(name = "percent_off", columnDefinition = "decimal", precision = 19, scale = 2)
	private BigDecimal percentOff;

	@Column(name = "is_free_gift", columnDefinition = "TINYINT")
	private Boolean isFreeGift = false;

	@Column(name = "discount_level_id")
	private Long discountLevelId;

	@Column(name = "discount_definition_id")
	private Long discountDefinitionId;

	public DiscountResultResponse getResponse() {
		DiscountResultResponse response = new DiscountResultResponse();
		ModelMapper modelMapper = new ModelMapper();
		BeanUtils.copyProperties(this, response);
		return response;
	}
}
