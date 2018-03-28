package com.fairanb.repository;

import com.fairanb.model.DiscountDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface DiscountDefinitionRepository extends PagingAndSortingRepository<DiscountDefinition, Long> {

	public DiscountDefinition findByName(String name);

	public Page<DiscountDefinition> findByStatus(String status, Pageable page);
}
