package com.fairanb.service;

import com.fairanb.model.response.LanguageResponse;

import java.util.List;

/**
 * @mustafamym
 */
public interface MerchantLanguageService {

	public 	List<LanguageResponse>  findByMerchantId(Long merchantId);
}


