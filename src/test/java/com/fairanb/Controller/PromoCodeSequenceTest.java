package com.fairanb.Controller;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.Rest;
import com.fairanb.common.TestBase;
import com.fairanb.controller.PromoCodeController;
import com.fairanb.model.request.PromoCodeRequest;
import com.fairanb.model.response.PromoCodeResponse;
import com.fairanb.repository.PromoCodeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class PromoCodeSequenceTest extends TestBase {

	@Autowired
	PromoCodeRepository promoCodeRepository;

	@Autowired
	private PromoCodeController controller;

	@Before
	public void setUp() throws Exception {
		// dummyAccessToken(USER_ID, MERCHANT_ID);
		super.setUp();
	}

	@Test
	public void testServicePromoCode() throws Exception {

		PromoCodeRequest request = new PromoCodeRequest();
		BeanUtils.copyProperties(promoCode, request);
		Long id = 0L;
		ResponseEntity<Rest> rest = null;
		PromoCodeResponse response = null;
		try {
			rest = controller.listPromoCode();
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			log.debug(rest.getBody().getData().toString());
			request.setCode(promoCode.getCode());
			rest = controller.createPromoCode(request, accessToken);
			response = (PromoCodeResponse) rest.getBody().getData();
			id = response.getId();
			Assert.assertEquals(HttpStatus.CREATED.value(), rest.getBody().getStatus());
			Assert.assertNotNull(response);

			// Code already conflicted
			rest = controller.createPromoCode(request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());

			// Update entire object with PUT method
			String updatedCode = request.getCode() + request.getCode();
			request = new PromoCodeRequest();
			request.setId(id);
			request.setCode(updatedCode);
			rest = controller.updatePromoCode(id,request, accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// Get Customer
			rest = controller.getPromoCode(id);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			PromoCodeResponse promoCodeResponse = (PromoCodeResponse) rest.getBody().getData();
			Assert.assertEquals(updatedCode, promoCodeResponse.getCode());

			// Search List by status
			rest = controller.listPromoCode();
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			List<PromoCodeResponse> promoCodeResponseList = (List<PromoCodeResponse>) rest.getBody().getData();
			Assert.assertNotNull(promoCodeResponseList);
			Assert.assertTrue(promoCodeResponseList.size() >= 1);

			// soft delete
			request = new PromoCodeRequest();
			request.setId(id);
			rest = controller.deletePromCode(request.getId(), accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// Create promo code for exiting and inactive user
			request = new PromoCodeRequest();
			request.setCode(updatedCode);
			rest = controller.createPromoCode(request, accessToken);
			Assert.assertEquals(HttpStatus.LOCKED.value(), rest.getBody().getStatus());

			// soft delete with invalide data
			request = new PromoCodeRequest();
			request.setId(0L);
			rest = controller.deletePromCode(request.getId(), accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// update with invalid data
			request = new PromoCodeRequest();
			request.setId(0L);
			rest = controller.updatePromoCode(0L,request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// Get invalid promo code id
			rest = controller.getPromoCode(0L);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			// Clean if existed
			if (id > 0) {
				promoCodeRepository.delete(id);
			}
		}
	}
}
