package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.GeoZone;
import com.fairanb.model.Zone;
import com.fairanb.repository.GeoZoneRepository;
import com.fairanb.service.GeoZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @mustafamym
 */
@Service
public class GeoZoneServiceImpl implements GeoZoneService {

	@Autowired
	private GeoZoneRepository geoZoneRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;


	@Override
	public Page<GeoZone> findAll(Pageable pageable) {
		return geoZoneRepository.findAll(pageable);
	}

	@Override
	public GeoZone findById(Long id) {
		return geoZoneRepository.findOne(id);
	}

	@Override
	public GeoZone findByName(String name) {
		return geoZoneRepository.findByName(name);
	}


	@Override
	public GeoZone save(GeoZone createGeoZone) {
		createGeoZone = geoZoneRepository.save(createGeoZone);
		Long id = createGeoZone.getId();
		simpleSourceBean.publishMerchantChange(JConstants.CREATE_EVENT, id);
		return createGeoZone;
	}

	@Override
	public GeoZone update(GeoZone updateGeoZone) {
		updateGeoZone = geoZoneRepository.save(updateGeoZone);
		Long id = updateGeoZone.getId();
		simpleSourceBean.publishMerchantChange(JConstants.UPDATE_EVENT, id);
		return updateGeoZone;
	}

	@Override
	public Boolean delete(Long id) {
		geoZoneRepository.delete(id);
		return !geoZoneRepository.exists(id);
	}
}


