package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.MerchantConfig;
import com.fairanb.repository.MerchantConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantConfigServiceImp implements MerchantConfigService {
	private static final Logger log = LoggerFactory.getLogger(MerchantConfigServiceImp.class);

	@Autowired
	protected MerchantConfigRepository repository;

	@Autowired
	SimpleSourceBean simpleSourceBean;


	@Override
	public MerchantConfig findOne(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Page<MerchantConfig> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public MerchantConfig save(MerchantConfig merchantConfig) {
		Long id=null;
		boolean isNew = merchantConfig.getId() == null || merchantConfig.getId() == 0;

		MerchantConfig createOrUpdateMerchantConfig= repository.save(merchantConfig);
		if(createOrUpdateMerchantConfig==null) {
			return null;
		}
		id = createOrUpdateMerchantConfig.getId();
		if(isNew){
			simpleSourceBean.publishMerchantChange(JConstants.CREATE_EVENT, id);
		}else {
			simpleSourceBean.publishMerchantChange(JConstants.UPDATE_EVENT, id);
		}
		return merchantConfig;
	}

	@Override
	public List<MerchantConfig> findByMerchantId(Long merchantId) {
		return repository.findByMerchantId(merchantId);
	}


	@Override
	public Boolean delete(Long id) {
		repository.delete(id);
		return !repository.exists(id);
	}
}
