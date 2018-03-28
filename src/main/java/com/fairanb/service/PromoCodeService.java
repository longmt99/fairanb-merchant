package com.fairanb.service;

import com.fairanb.model.PromoCode;

import java.util.List;

public interface PromoCodeService {

	PromoCode findByCode(String code);

	PromoCode save(PromoCode promocode);

	PromoCode findOne(Long id);

	List<PromoCode> findByStatus(String status);

}
