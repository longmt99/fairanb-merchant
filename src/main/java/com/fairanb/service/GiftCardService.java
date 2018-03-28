package com.fairanb.service;

import com.fairanb.model.GiftCard;
import com.fairanb.model.request.GiftCardRequest;

import java.util.List;

public interface GiftCardService {
	GiftCard createGiftCard(GiftCard giftCard);

	List<GiftCard> getMerchantGiftCards(Long merchantId);

	GiftCard getById(Long id);

	void delete(Long id);

	GiftCard update(Long id, GiftCardRequest request);
}
