package com.fairanb.service;

import com.fairanb.model.DiscountLevel;
import com.fairanb.repository.DiscountLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountLevelServiceImpl implements DiscountLevelService {

	@Autowired
	DiscountLevelRepository discountLevelRepository;

	@Override
	public DiscountLevel findOne(Long id) {
		return discountLevelRepository.findOne(id);
	}

	@Override
	public List<DiscountLevel> findAll() {
		return discountLevelRepository.findAll();
	}

}
