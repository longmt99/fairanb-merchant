package com.fairanb.service;

import com.fairanb.model.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @mustafamym
 */
public interface CountryService {

	Page<Country> findAll(Pageable pageable);
	List<Country> findAll();
	Country findById(Long id);

	Country findByCountryName(String name);

	Country findByIsoCode2(String isoCode2);

	Country findByIsoCode3(String isoCode3);

	Country save(Country country);

	Country update(Country country);

	Boolean delete(Long id);

	Boolean deleteAll();

	Iterable<Country> save (List<Country> countries);

}


