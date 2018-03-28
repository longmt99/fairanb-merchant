package com.fairanb.model;

import com.fairanb.model.response.DiscountConditionResponse;
import com.fairanb.model.response.DiscountDefinitionResponse;
import com.fairanb.model.response.DiscountResultResponse;
import com.fairanb.model.response.MerchantResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "discount_definition")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class DiscountDefinition extends BaseEntity {

	@Basic(optional = false)
	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "message")
	private String message;

	@Column(name = "priority")
	private Short priority;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "status")
	private String status;

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Merchant.class, optional = true)
	@JoinColumn(name = "merchant_id", referencedColumnName = "id", updatable = true)
	private Merchant merchant = new Merchant();

	@Transient
	private List<DiscountCondition> discountCondition = new ArrayList<>();

	@Transient
	private List<DiscountResult> discountResult = new ArrayList<>();

	public DiscountDefinitionResponse getResponse() {
		DiscountDefinitionResponse response = new DiscountDefinitionResponse();
		BeanUtils.copyProperties(this, response);

		MerchantResponse merchantResponse = this.getMerchant().getResponse();
		response.setMerchantResponse(merchantResponse);

		List<DiscountResultResponse> discountResultResponseList = new ArrayList<DiscountResultResponse>();
		if (this.getDiscountResult() != null) {
			this.getDiscountResult().forEach(discountResult -> {
				discountResultResponseList.add(discountResult.getResponse());
			});
		}

		response.setDiscountResults(discountResultResponseList);
		List<DiscountConditionResponse> discountConditionList = new ArrayList<DiscountConditionResponse>();
		if (this.getDiscountCondition() != null) {
			this.getDiscountCondition().forEach(discountCondition -> {
				discountConditionList.add(discountCondition.getResponse());
			});
		}
		response.setDiscountConditions(discountConditionList);

		return response;
	}

}
