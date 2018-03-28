package com.fairanb.service;

import com.fairanb.model.GeoZone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @mustafamym
 */
public interface GeoZoneService {

	Page<GeoZone> findAll(Pageable pageable);

	GeoZone findById(Long id);

	GeoZone findByName(String name);

	GeoZone save(GeoZone createGeoZone);

	GeoZone update(GeoZone updateGeoZone);

	Boolean delete(Long id);

}


