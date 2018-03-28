package com.fairanb.service;

import com.fairanb.model.Language;
import com.fairanb.model.MerchantLanguage;
import com.fairanb.model.response.LanguageResponse;
import com.fairanb.repository.LanguageRepository;
import com.fairanb.repository.MerchantLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @mustafamym
 */
@Service
public class MerchantLanguageServiceImpl implements MerchantLanguageService {

	@Autowired
	private MerchantLanguageRepository mLanguageRepository;

	@Autowired
	private LanguageRepository languageRepository;

	@Override
	public List<LanguageResponse> findByMerchantId(Long merchantId) {
			List< MerchantLanguage>  merchantLanguageList=mLanguageRepository.findByMerchantId(merchantId);
			List<LanguageResponse> languageList = new ArrayList<>();
			for(MerchantLanguage merchantLanguage:merchantLanguageList) {
				Language    language=	languageRepository.findOne(merchantLanguage.getLanguageId());
				LanguageResponse languageResponse=new LanguageResponse();
				if (language!=null) {
					languageResponse.setName(language.getName());
					languageList.add(languageResponse);
				}
			}
			return languageList;
		}
}


