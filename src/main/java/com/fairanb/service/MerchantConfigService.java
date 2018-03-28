package com.fairanb.service;


import com.fairanb.model.MerchantConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MerchantConfigService {
	public MerchantConfig findOne(Long id);
	public Page<MerchantConfig> findAll(Pageable pageable) ;
	public MerchantConfig save(MerchantConfig merchantConfig);
	public List<MerchantConfig> findByMerchantId(Long merchantId);
	public Boolean delete(Long id);
}
