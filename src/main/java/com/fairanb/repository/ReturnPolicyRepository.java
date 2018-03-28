package com.fairanb.repository;

import com.fairanb.model.ReturnPolicy;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ReturnPolicyRepository extends PagingAndSortingRepository<ReturnPolicy, Long> {
	List<ReturnPolicy> findByMerchantId(Long merchantId);
}
