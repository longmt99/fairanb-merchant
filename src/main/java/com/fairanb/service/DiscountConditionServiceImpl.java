package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.DiscountCondition;
import com.fairanb.repository.DiscountConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountConditionServiceImpl implements DiscountConditionService {

	@Autowired
	DiscountConditionRepository discountConditionRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;

	@Override
	public DiscountCondition save(DiscountCondition discountCondition) {
		boolean isNew = discountCondition.getId() == null || discountCondition.getId() == 0;
		discountCondition = discountConditionRepository.save(discountCondition);
		Long id = discountCondition.getId();
		if (isNew) {
			simpleSourceBean.publishDefinitionConditionChange(JConstants.CREATE_EVENT, id);
		} else {
			simpleSourceBean.publishDefinitionConditionChange(JConstants.UPDATE_EVENT, id);
		}
		return discountCondition;
	}

	@Override
	public DiscountCondition findOne(Long id) {
		return discountConditionRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		discountConditionRepository.delete(id);
	}

	@Override
	public List<DiscountCondition> findByDiscountDefinitionId(Long id) {
		return discountConditionRepository.findByDiscountDefinitionId(id);
	}

}
