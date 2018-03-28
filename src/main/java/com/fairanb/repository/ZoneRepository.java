package com.fairanb.repository;

import com.fairanb.model.Zone;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @mustafamym
 */
@Transactional
public interface ZoneRepository extends PagingAndSortingRepository<Zone, Long> {
    public Zone findByName(String name);
}
