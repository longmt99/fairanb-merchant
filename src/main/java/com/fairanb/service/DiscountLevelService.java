package com.fairanb.service;

import com.fairanb.model.DiscountLevel;

import java.util.List;

public interface DiscountLevelService {

	DiscountLevel findOne(Long id);

	List<DiscountLevel> findAll();
}
