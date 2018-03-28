package com.fairanb.repository;

import com.fairanb.model.ReturnType;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface ReturnTypeRepository extends PagingAndSortingRepository<ReturnType, Long> {
}
