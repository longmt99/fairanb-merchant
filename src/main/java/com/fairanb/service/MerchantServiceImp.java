package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.Merchant;
import com.fairanb.repository.MerchantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImp implements MerchantService {
	private static final Logger log = LoggerFactory.getLogger(MerchantServiceImp.class);

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;


	public MerchantServiceImp() {
		log.debug("Service Impl start");
	}

	public Merchant findOne(Long id) {
		return merchantRepository.findOne(id);
	}


	public Merchant findByEmail(String code) {
		return merchantRepository.findByEmail(code);
	}


	public Page<Merchant> findAll(Pageable pageable) {
		return merchantRepository.findAll(pageable);
	}

	public Merchant save(Merchant merchant) {
		boolean isNew = merchant.getId() == null || merchant.getId() == 0;

		merchant = merchantRepository.save(merchant);
		Long id = merchant.getId();
		if(isNew){
			simpleSourceBean.publishMerchantChange(JConstants.CREATE_EVENT, id);
		}else {
			simpleSourceBean.publishMerchantChange(JConstants.UPDATE_EVENT, id);
		}
		return merchant;
	}

	public void delete(Long id) {
		merchantRepository.delete(id);
		simpleSourceBean.publishMerchantChange(JConstants.DELETE_EVENT, id);
	}
}
