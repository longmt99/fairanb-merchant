package com.fairanb.service;

import com.fairanb.model.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @mustafamym
 */
public interface ZoneService {
	List<Zone> loadAllZones();
	Page<Zone> findAll(Pageable pageable);

	Zone findById(Long id);
	Zone findByName(String name);
	Zone save(Zone zone);
	Iterable<Zone> save (List<Zone> zones);
	Zone update(Zone zone);

	Boolean delete(Long id);
	Boolean deleteAll();

}


