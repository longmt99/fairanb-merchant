package com.fairanb.service;

import com.fairanb.model.Language;
import com.fairanb.model.MerchantLanguage;
import com.fairanb.model.response.MerchantLanguageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @mustafamym
 */
public interface LanguageService {

	Page<Language> findAll(Pageable pageable);

	Language findById(Long id);

	Language findByLanguageName(String name);

	Language save(Language language);

	Language update(Language language);

	Boolean delete(Long id);
	List<MerchantLanguageResponse> getMerchantLanguages (List<MerchantLanguage> languages);

}


