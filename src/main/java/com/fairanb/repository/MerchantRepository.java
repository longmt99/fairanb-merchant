package com.fairanb.repository;

import com.fairanb.model.Merchant;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

/**
 * @longmt99
 */
@Transactional
public interface MerchantRepository  extends PagingAndSortingRepository<Merchant, Long> {
	
	 public Merchant findByEmail(String email);
}
