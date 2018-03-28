package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.DiscountResult;
import com.fairanb.repository.DiscountResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountResultServiceImpl implements DiscountResultService {

	@Autowired
	DiscountResultRepository discountResultRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;

	@Override
	public DiscountResult save(DiscountResult discountResult) {
		boolean isNew = discountResult.getId() == null || discountResult.getId() == 0;
		discountResult = discountResultRepository.save(discountResult);
		Long id = discountResult.getId();
		if (isNew) {
			simpleSourceBean.publishDefinitionResultChange(JConstants.CREATE_EVENT, id);
		} else {
			simpleSourceBean.publishDefinitionResultChange(JConstants.UPDATE_EVENT, id);
		}
		return discountResult;
	}

	@Override
	public DiscountResult findOne(Long id) {
		return discountResultRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		discountResultRepository.delete(id);
	}

	@Override
	public List<DiscountResult> findByDiscountDefinitionId(Long id) {
		return discountResultRepository.findByDiscountDefinitionId(id);
	}

}
