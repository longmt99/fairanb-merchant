package com.fairanb.repository;

import com.fairanb.model.Country;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @mustafamym
 */

public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {
    public Country findByName(String name);
    public Country findByIsoCode2(String isoCode2);
    public Country findByIsoCode3(String isoCode3);
    public List<Country> findByStatus(String status);


}
