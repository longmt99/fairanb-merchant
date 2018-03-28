package com.fairanb.service;

import com.fairanb.model.DiscountDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountDefinitionService {

	DiscountDefinition findByName(String name);

	DiscountDefinition save(DiscountDefinition discountDefinition);

	DiscountDefinition findOne(Long id);

	Page<DiscountDefinition> findByStatus(String status, Pageable page);

	void softDelete(DiscountDefinition discountDefinition);

}
