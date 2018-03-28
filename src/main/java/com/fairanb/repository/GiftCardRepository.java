package com.fairanb.repository;

import com.fairanb.model.GiftCard;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface GiftCardRepository extends PagingAndSortingRepository<GiftCard, Long>{
	List<GiftCard> findByMerchantIdAndActive(Long merchantId, Boolean active);
}
