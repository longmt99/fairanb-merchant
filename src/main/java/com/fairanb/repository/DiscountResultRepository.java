package com.fairanb.repository;

import com.fairanb.model.DiscountResult;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountResultRepository extends PagingAndSortingRepository<DiscountResult, Long> {

	public List<DiscountResult> findByDiscountDefinitionId(Long id);
}
