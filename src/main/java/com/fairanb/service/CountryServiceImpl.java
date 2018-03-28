package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.Country;
import com.fairanb.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @mustafamym
 */
@Service
public class CountryServiceImpl implements CountryService {

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;


	@Override
	public Page<Country> findAll(Pageable pageable) {
		return countryRepository.findAll(pageable);
	}

	@Override
	public List<Country> findAll() {
		return (List) countryRepository.findAll();
	}

	@Override
	public Country findById(Long id) {
		return countryRepository.findOne(id);
	}

	@Override
	public Country findByCountryName(String name) {
		return countryRepository.findByName(name);
	}

	@Override
	public Country findByIsoCode2(String isoCode2) {
		return countryRepository.findByIsoCode2(isoCode2);
	}

	@Override
	public Country findByIsoCode3(String isoCode3) {
		return countryRepository.findByIsoCode3(isoCode3);
	}

	@Override
	public Country save(Country country) {
		country = countryRepository.save(country);
		Long id = country.getId();
		simpleSourceBean.publishMerchantChange(JConstants.CREATE_EVENT, id);
		return country;
	}

	@Override
	public Country update(Country country) {
		country = countryRepository.save(country);
		Long id = country.getId();
		simpleSourceBean.publishMerchantChange(JConstants.UPDATE_EVENT, id);
		return country;
	}

	@Override
	public Boolean delete(Long id) {
		countryRepository.delete(id);
		return !countryRepository.exists(id);
	}

	@Override
	public Boolean deleteAll() {
		try {
			countryRepository.deleteAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Iterable<Country> save(List<Country> countries) {
		return countryRepository.save(countries);
	}
}


