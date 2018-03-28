package com.fairanb.repository;

import com.fairanb.model.MerchantLanguage;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @mustafamym
 */
@Transactional
public interface MerchantLanguageRepository extends PagingAndSortingRepository<MerchantLanguage, Long> {
    public List<MerchantLanguage> findByMerchantId(Long merchantId);
}
