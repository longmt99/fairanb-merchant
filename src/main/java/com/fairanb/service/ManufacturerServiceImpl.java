package com.fairanb.service;

import com.fairanb.model.Manufacturer;
import com.fairanb.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Override
    public Manufacturer getById(Long id) {
        return manufacturerRepository.findOne(id);
    }

    @Override
    public Page<Manufacturer> getAll(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        manufacturerRepository.delete(id);
    }

    @Override
    public Manufacturer save(Manufacturer m) {
        return manufacturerRepository.save(m);
    }
}
