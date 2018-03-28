package com.fairanb.repository;

import com.fairanb.model.DiscountCondition;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountConditionRepository extends PagingAndSortingRepository<DiscountCondition, Long> {

	public List<DiscountCondition> findByDiscountDefinitionId(Long id);

}
