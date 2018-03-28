package com.fairanb.service;

import com.fairanb.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManufacturerService {
    Manufacturer getById (Long id) ;
    Page<Manufacturer> getAll(Pageable pageable);
    void deleteById(Long id);
    Manufacturer save(Manufacturer m);
}
