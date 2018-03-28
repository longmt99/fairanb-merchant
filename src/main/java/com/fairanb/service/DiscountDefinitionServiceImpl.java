package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.DiscountCondition;
import com.fairanb.model.DiscountDefinition;
import com.fairanb.model.DiscountResult;
import com.fairanb.repository.DiscountDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class DiscountDefinitionServiceImpl implements DiscountDefinitionService {

	@Autowired
	DiscountDefinitionRepository discountDefinitionRepository;

	@Autowired
	DiscountConditionService discountConditionService;

	@Autowired
	DiscountResultService discountResultService;

	@Autowired
	SimpleSourceBean simpleSourceBean;

	@Override
	public DiscountDefinition findByName(String name) {
		return discountDefinitionRepository.findByName(name);
	}

	@Override
	public DiscountDefinition save(DiscountDefinition discountDefinition) {
		boolean isNew = discountDefinition.getId() == null || discountDefinition.getId() == 0;
		discountDefinition = discountDefinitionRepository.save(discountDefinition);
		Long id = discountDefinition.getId();
		if (isNew) {
			simpleSourceBean.publishDiscountDefinitionChange(JConstants.CREATE_EVENT, id);
		} else {
			simpleSourceBean.publishDiscountDefinitionChange(JConstants.UPDATE_EVENT, id);
		}
		return discountDefinition;
	}

	@Override
	public DiscountDefinition findOne(Long id) {
		DiscountDefinition discountDefinition = discountDefinitionRepository.findOne(id);
		if (discountDefinition != null)
			discountDefinition = setDiscountConditionAndResult(discountDefinition);
		return discountDefinition;
	}

	@Override
	public Page<DiscountDefinition> findByStatus(String status, Pageable page) {
		Page<DiscountDefinition> discountDefinitionPage = discountDefinitionRepository.findByStatus(status, page);
		if (discountDefinitionPage.getContent().size() > 0) {
			discountDefinitionPage.getContent().forEach(definition -> {
				definition = setDiscountConditionAndResult(definition);
			});
		}
		return discountDefinitionPage;
	}

	@Override
	public void softDelete(DiscountDefinition discountDefinition) {
		discountDefinition.setStatus("INACTIVE");
		discountDefinition.setActive(Boolean.FALSE);
		discountDefinition.setModifiedDate(new Timestamp(new Date().getTime()));
		discountDefinitionRepository.save(discountDefinition);
	}

	private DiscountDefinition setDiscountConditionAndResult(DiscountDefinition discountDefinition) {
		if (discountDefinition.getId() != null) {
			Long id = discountDefinition.getId();
			List<DiscountCondition> discountCondition = discountConditionService.findByDiscountDefinitionId(id);
			discountDefinition.setDiscountCondition(discountCondition);
			List<DiscountResult> discountResult = discountResultService.findByDiscountDefinitionId(id);
			discountDefinition.setDiscountResult(discountResult);
		}
		return discountDefinition;
	}

}
