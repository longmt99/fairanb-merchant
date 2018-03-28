package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.PromoCode;
import com.fairanb.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

	@Autowired
	PromoCodeRepository promoCodeRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;

	@Override
	public PromoCode findByCode(String code) {
		return promoCodeRepository.findByCode(code);
	}

	@Override
	public PromoCode save(PromoCode promocode) {
		boolean isNew = promocode.getId() == null || promocode.getId() == 0;
		promocode = promoCodeRepository.save(promocode);
		Long id = promocode.getId();
		if (isNew) {
			simpleSourceBean.publishPromoCodeChange(JConstants.CREATE_EVENT, id);
		} else {
			simpleSourceBean.publishPromoCodeChange(JConstants.UPDATE_EVENT, id);
		}
		return promocode;
	}

	@Override
	public PromoCode findOne(Long id) {
		return promoCodeRepository.findOne(id);
	}

	@Override
	public List<PromoCode> findByStatus(String status) {
		return promoCodeRepository.findByStatus(status);
	}

}
