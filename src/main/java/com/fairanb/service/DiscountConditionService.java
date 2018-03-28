package com.fairanb.service;

import com.fairanb.model.DiscountCondition;

import java.util.List;

public interface DiscountConditionService {

	DiscountCondition save(DiscountCondition discountCondition);

	DiscountCondition findOne(Long id);

	void delete(Long id);
	
	List<DiscountCondition> findByDiscountDefinitionId(Long id);
}
