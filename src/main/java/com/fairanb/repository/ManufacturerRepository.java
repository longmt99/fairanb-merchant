package com.fairanb.repository;

import com.fairanb.model.Manufacturer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ManufacturerRepository extends PagingAndSortingRepository<Manufacturer, Long> {
}
