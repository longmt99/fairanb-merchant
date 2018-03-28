package com.fairanb.service;

import com.fairanb.model.GiftCard;
import com.fairanb.model.request.GiftCardRequest;
import com.fairanb.repository.GiftCardRepository;
import com.fairanb.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GiftCardServiceImpl implements GiftCardService {
	@Autowired
	GiftCardRepository repository;
	@Autowired
	MerchantRepository merchantRepository;

	@Override
	public GiftCard createGiftCard(GiftCard giftCard) {
		Objects.requireNonNull(merchantRepository.findOne(giftCard.getMerchantId()), "Merchant not found");
		return repository.save(giftCard);
	}

	@Override
	public List<GiftCard> getMerchantGiftCards(Long merchantId) {
		List<GiftCard> giftCards = repository.findByMerchantIdAndActive(merchantId, true);
		if (giftCards.isEmpty()) {
			Objects.requireNonNull(merchantRepository.findOne(merchantId), "Merchant not found");
		}
		return giftCards;
	}

	@Override
	public GiftCard getById(Long id) {
		GiftCard giftCard = repository.findOne(id);
		Objects.requireNonNull(giftCard, "Gift card not found");
		return giftCard;
	}

	@Override
	public void delete(Long id) {
		GiftCard giftCard = repository.findOne(id);
		Objects.requireNonNull(giftCard, "Gift card not found");

		giftCard.setActive(false);

		repository.save(giftCard);
	}

	@Override
	public GiftCard update(Long id, GiftCardRequest request) {
		GiftCard giftCard = repository.findOne(id);
		Objects.requireNonNull(giftCard, "Gift card not found");

		if (request.getMerchantId() != null) {
			Objects.requireNonNull(merchantRepository.findOne(request.getMerchantId()), "Merchant not found");
		}

		mergeGiftCardUpdates(giftCard, request);

		return repository.save(giftCard);
	}

	private void mergeGiftCardUpdates(GiftCard giftCard, GiftCardRequest upd) {
		if (upd.getCode() != null) {
			giftCard.setCode(upd.getCode());
		}

		if (upd.getMerchantId() != null) {
			giftCard.setMerchantId(upd.getMerchantId());
		}

		if (upd.getName() != null) {
			giftCard.setName(upd.getName());
		}

		if (upd.getDescription() != null) {
			giftCard.setDescription(upd.getDescription());
		}

		if (upd.getStartDate() != null) {
			giftCard.setStartDate(upd.getStartDate());
		}

		if (upd.getEndDate() != null) {
			giftCard.setEndDate(upd.getEndDate());
		}

		if (upd.getBalance() != null) {
			giftCard.setBalance(upd.getBalance());
		}

		if (upd.getPurchaseAmount() != null) {
			giftCard.setPurchaseAmount(upd.getPurchaseAmount());
		}

		if (upd.getSecurityCode() != null) {
			giftCard.setSecurityCode(upd.getSecurityCode());
		}

		if (upd.getStatus() != null) {
			giftCard.setStatus(upd.getStatus());
		}
	}
}
