package com.fairanb.repository;

import com.fairanb.model.GeoZone;
import com.fairanb.model.Zone;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

/**
 * @mustafamym
 */
@Transactional
public interface GeoZoneRepository extends PagingAndSortingRepository<GeoZone, Long> {
    public GeoZone findByName(String name);
}
