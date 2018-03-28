package com.fairanb.Controller;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.Rest;
import com.fairanb.common.TestBase;
import com.fairanb.controller.DiscountResultController;
import com.fairanb.model.request.DiscountResultRequest;
import com.fairanb.model.response.DiscountResultResponse;
import com.fairanb.repository.DiscountDefinitionRepository;
import com.fairanb.repository.DiscountLevelRepository;
import com.fairanb.repository.MerchantRepository;
import com.fairanb.service.DiscountDefinitionService;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class DiscountResultControllerTest extends TestBase {

	@Autowired
	DiscountDefinitionRepository discountDefinitionRepository;

	@Autowired
	DiscountLevelRepository discountLevelRepository;

	@Autowired
	DiscountResultController discountResultController;

	@Autowired
	DiscountDefinitionService discountDefinitionService;

	@Autowired
	MerchantRepository merchantRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testServiceDiscountResult() throws Exception {

		DiscountResultRequest request = new DiscountResultRequest();
		BeanUtils.copyProperties(discountResult, request);
		Long discountResultId = 0L, merchantId = 0L, discountLevelId = 0L, discountDefinitionId = 0L;
		ResponseEntity<Rest> rest = null;
		DiscountResultResponse response = null;
		try {
			C00.setEmail("merchant02200@fairanb.com");
			C00.setPhone("+1444-111-2224");
			C00.setLanguages(null);
			C00 = merchantRepository.save(C00);
			merchantId = C00.getId();
			discountDefinition.setMerchant(C00);
			discountDefinition = discountDefinitionService.save(discountDefinition);
			discountDefinitionId = discountDefinition.getId();

			// create discountCondition without discount Definition
			rest = discountResultController.createDiscountResult(request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			request.setDiscountDefinitionId(0L);

			// create Discount condition without discount level and invalid
			// Discount Definition
			rest = discountResultController.createDiscountResult(request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			request.setDiscountDefinitionId(discountDefinitionId);
			request.setDiscountLevelId(0L);

			// create Discount condition invalid discount level
			rest = discountResultController.createDiscountResult(request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			discountLevel = discountLevelRepository.save(discountLevel);
			discountLevelId = discountLevel.getId();
			request.setDiscountLevelId(discountLevelId);

			// create Discount condition with correct data
			rest = discountResultController.createDiscountResult(request, accessToken);
			Assert.assertEquals(HttpStatus.CREATED.value(), rest.getBody().getStatus());

			response = (DiscountResultResponse) rest.getBody().getData();
			discountResultId = response.getId();

			// update Discount condition with out id
			request.setId(null);
			rest = discountResultController.updateDiscountResult(null, request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// update Discount condition with invalid id
			request.setId(0L);
			rest = discountResultController.updateDiscountResult(0L, request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());
			request.setId(discountResultId);
			request.setDiscountDefinitionId(0L);

			// update Discount condition with invalid Discount Definition Id
			rest = discountResultController.updateDiscountResult(discountResultId, request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());
			request.setDiscountDefinitionId(discountDefinitionId);
			request.setDiscountLevelId(0L);

			// update Discount condition with invalid Discount level Id
			rest = discountResultController.updateDiscountResult(discountResultId, request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());
			request.setDiscountLevelId(discountLevelId);

			// update discount condition with correct data
			rest = discountResultController.updateDiscountResult(discountResultId, request, accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// Delete Discount condition
			rest = discountResultController.deleteDiscountResult(discountResultId, accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// Delete Discount condition with invalid id
			rest = discountResultController.deleteDiscountResult(0L, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			if (discountDefinitionId > 0)
				discountDefinitionRepository.delete(discountDefinitionId);
			if (merchantId > 0)
				merchantRepository.delete(merchantId);
			if (discountLevelId > 0)
				discountLevelRepository.delete(discountLevelId);
		}
	}
}
