package com.fairanb.service;

import com.fairanb.model.DiscountResult;

import java.util.List;

public interface DiscountResultService {

	DiscountResult save(DiscountResult discountResult);

	DiscountResult findOne(Long id);

	void delete(Long id);

	List<DiscountResult> findByDiscountDefinitionId(Long id);
}
