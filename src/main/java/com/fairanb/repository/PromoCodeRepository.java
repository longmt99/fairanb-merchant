package com.fairanb.repository;

import com.fairanb.model.PromoCode;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PromoCodeRepository extends PagingAndSortingRepository<PromoCode, Long> {

	public PromoCode findByCode(String code);

	public List<PromoCode> findByStatus(String status);
}
