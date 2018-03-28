package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.Language;
import com.fairanb.model.MerchantLanguage;
import com.fairanb.model.response.MerchantLanguageResponse;
import com.fairanb.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @mustafamym
 */
@Service
public class LanguageServiceImpl implements LanguageService {

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;


	@Override
	public Page<Language> findAll(Pageable pageable) {
		return languageRepository.findAll(pageable);
	}

	@Override
	public Language findById(Long id) {
		return languageRepository.findOne(id);
	}

	@Override
	public Language findByLanguageName(String name) {
		return languageRepository.findByName(name);
	}

	@Override
	public Language save(Language language) {
		language = languageRepository.save(language);
		Long id = language.getId();
		simpleSourceBean.publishMerchantChange(JConstants.CREATE_EVENT, id);
		return language;
	}

	@Override
	public Language update(Language language) {
		language = languageRepository.save(language);
		Long id = language.getId();
		simpleSourceBean.publishMerchantChange(JConstants.UPDATE_EVENT, id);
		return language;
	}

	@Override
	public Boolean delete(Long id) {
		languageRepository.delete(id);
		return !languageRepository.exists(id);
	}

	@Override
	public List<MerchantLanguageResponse> getMerchantLanguages(List<MerchantLanguage> languages) {
		List<MerchantLanguageResponse> merchantLanguagelanguages=new ArrayList<>();
		Language language=null;
		if(languages != null && !languages.isEmpty()) {
			for (MerchantLanguage merchantLanguage : languages) {
				language = languageRepository.findOne(merchantLanguage.getLanguageId());
				if (language != null) {
					MerchantLanguageResponse merchantLanguageResponse = new MerchantLanguageResponse();
					merchantLanguageResponse.setId(merchantLanguage.getId());
					merchantLanguageResponse.setMerchantId(merchantLanguage.getMerchantId());
					merchantLanguageResponse.setLanguageId(merchantLanguage.getLanguageId());
					merchantLanguageResponse.setName(language.getName());
					merchantLanguagelanguages.add(merchantLanguageResponse);
				}
			}
		}
       return merchantLanguagelanguages;
	}
}


