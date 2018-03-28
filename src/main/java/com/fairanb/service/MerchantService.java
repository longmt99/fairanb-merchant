package com.fairanb.service;


import com.fairanb.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface MerchantService {

	public Merchant findOne(Long id);
	public Merchant findByEmail(String code);
	public Page<Merchant> findAll(Pageable pageable) ;
	@Transactional
	public Merchant save(Merchant merchant);
	public void delete(Long id);
}
