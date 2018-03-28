package com.fairanb.service;

import com.fairanb.MerchantRestAPI;
import com.fairanb.model.GiftCard;
import com.fairanb.model.request.GiftCardRequest;
import com.fairanb.repository.GiftCardRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class GiftCardServiceImplTest {
	private static final Long GIFT_CARD_ID = 1L;
	private static final Long MERCHANT_ID  = 1L;

	@Autowired
	GiftCardServiceImpl service;

	@MockBean
	GiftCardRepository repository;

	private GiftCard giftCard;

	@Before
	public void setUp() throws Exception {
		giftCard = new GiftCard();
		giftCard.setId(GIFT_CARD_ID);


		when(repository.save(any(GiftCard.class))).then(invocation -> {
			GiftCard argument = invocation.getArgumentAt(0, GiftCard.class);
			if (argument.getId() == null) {
				argument.setId(GIFT_CARD_ID);
			}
			return argument;
		});

		when(repository.findOne(GIFT_CARD_ID)).thenReturn(giftCard);
		when(repository.findByMerchantIdAndActive(MERCHANT_ID, true)).thenReturn(Arrays.asList(giftCard));
	}

	@After
	public void tearDown() throws Exception {
		verifyNoMoreInteractions(repository);
	}

	@Test
	public void createGiftCard() throws Exception {
		ArgumentCaptor<GiftCard> captor = ArgumentCaptor.forClass(GiftCard.class);

		String     code           = "code";
		String     name           = "name";
		String     description    = "description";
		Date       startDate      = new Date();
		Date       endDate        = new Date();
		BigDecimal balance        = new BigDecimal(100);
		BigDecimal purchaseAmount = new BigDecimal(50);
		String     securityCode   = "security_code";
		String     status         = "status";

		GiftCard request = new GiftCard();
		request.setCode(code);
		request.setName(name);
		request.setDescription(description);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setBalance(balance);
		request.setPurchaseAmount(purchaseAmount);
		request.setSecurityCode(securityCode);
		request.setStatus(status);

		GiftCard result = service.createGiftCard(request);

		verify(repository).save(captor.capture());

		GiftCard captured = captor.getValue();

		Assert.assertEquals(captured, request);
	}

	@Test
	public void getMerchantGiftCards() throws Exception {
		List<GiftCard> actual = service.getMerchantGiftCards(MERCHANT_ID);

		Assert.assertEquals(actual.size(), 1);
		Assert.assertEquals(actual.get(0), this.giftCard);

		verify(repository).findByMerchantIdAndActive(MERCHANT_ID, true);
	}

	@Test
	public void getById() throws Exception {
		GiftCard actual = service.getById(GIFT_CARD_ID);

		Assert.assertEquals(actual, this.giftCard);

		verify(repository).findOne(GIFT_CARD_ID);
	}

	@Test
	public void delete() throws Exception {
		ArgumentCaptor<GiftCard> captor = ArgumentCaptor.forClass(GiftCard.class);

		service.delete(GIFT_CARD_ID);

		verify(repository).findOne(GIFT_CARD_ID);
		verify(repository).save(captor.capture());

		GiftCard value = captor.getValue();
		Assert.assertSame(value, this.giftCard);
		Assert.assertEquals(value.getActive(), false);
	}

	@Test
	public void update() throws Exception {
		ArgumentCaptor<GiftCard> captor = ArgumentCaptor.forClass(GiftCard.class);

		String     code           = "code";
		String     name           = "name";
		String     description    = "description";
		Date       startDate      = new Date();
		Date       endDate        = new Date();
		BigDecimal balance        = new BigDecimal(100);
		BigDecimal purchaseAmount = new BigDecimal(50);
		String     securityCode   = "security_code";
		String     status         = "status";

		GiftCardRequest request = new GiftCardRequest();
		request.setCode(code);
		request.setName(name);
		request.setDescription(description);
		request.setStartDate(startDate);
		request.setEndDate(endDate);
		request.setBalance(balance);
		request.setPurchaseAmount(purchaseAmount);
		request.setSecurityCode(securityCode);
		request.setStatus(status);

		service.update(GIFT_CARD_ID, request);

		verify(repository).findOne(GIFT_CARD_ID);
		verify(repository).save(captor.capture());

		GiftCard value = captor.getValue();

		Assert.assertEquals(value.getCode(), code);
		Assert.assertEquals(value.getName(), name);
		Assert.assertEquals(value.getDescription(), description);
		Assert.assertEquals(value.getStartDate(), startDate);
		Assert.assertEquals(value.getEndDate(), endDate);
		Assert.assertEquals(value.getBalance(), balance);
		Assert.assertEquals(value.getPurchaseAmount(), purchaseAmount);
		Assert.assertEquals(value.getSecurityCode(), securityCode);
		Assert.assertEquals(value.getStatus(), status);
	}
}