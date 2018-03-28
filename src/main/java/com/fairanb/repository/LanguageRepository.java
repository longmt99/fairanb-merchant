package com.fairanb.repository;

import com.fairanb.model.Language;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

/**
 * @mustafamym
 */
@Transactional
public interface LanguageRepository extends PagingAndSortingRepository<Language, Long> {
    public Language findByName(String name);

}
