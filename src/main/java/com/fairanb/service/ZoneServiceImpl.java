package com.fairanb.service;

import com.fairanb.common.JConstants;
import com.fairanb.event.SimpleSourceBean;
import com.fairanb.model.Zone;
import com.fairanb.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @mustafamym
 */
@Service
public class ZoneServiceImpl implements ZoneService {

	@Autowired
	private ZoneRepository zoneRepository;

	@Autowired
	SimpleSourceBean simpleSourceBean;


	@Override
	public List<Zone> loadAllZones() {
		return (List)zoneRepository.findAll();
	}

	@Override
	public Page<Zone> findAll(Pageable pageable) {
		return zoneRepository.findAll(pageable);
	}

	public Zone findById(Long id) {
		return zoneRepository.findOne(id);
	}

	@Override
	public Zone findByName(String name) {
		return zoneRepository.findByName(name);
	}


	@Override
	public Zone save(Zone zone) {
		zone = zoneRepository.save(zone);
		Long id = zone.getId();
		simpleSourceBean.publishMerchantChange(JConstants.CREATE_EVENT, id);
		return zone;
	}

	@Override
	public Iterable<Zone> save(List<Zone> zones) {
		return zoneRepository.save(zones);
	}

	@Override
	public Zone update(Zone zone) {
		zone = zoneRepository.save(zone);
		Long id = zone.getId();
		simpleSourceBean.publishMerchantChange(JConstants.UPDATE_EVENT, id);
		return zone;
	}


	@Override
	public Boolean delete(Long id) {
		zoneRepository.delete(id);
		return !zoneRepository.exists(id);
	}

	@Override
	public Boolean deleteAll() {
		try {
			zoneRepository.deleteAll();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}


