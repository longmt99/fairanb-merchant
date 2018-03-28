package com.fairanb.repository;

import com.fairanb.model.MerchantConfig;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @mustafamym
 */
@Transactional
public interface MerchantConfigRepository extends PagingAndSortingRepository<MerchantConfig, Long> {

    public List<MerchantConfig> findByMerchantId(Long merchantId);
}
