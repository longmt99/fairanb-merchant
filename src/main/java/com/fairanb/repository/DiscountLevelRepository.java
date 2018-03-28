package com.fairanb.repository;

import com.fairanb.model.DiscountLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountLevelRepository extends JpaRepository<DiscountLevel, Long> {

}
