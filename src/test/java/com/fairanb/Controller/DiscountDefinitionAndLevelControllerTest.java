package com.fairanb.Controller;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.TestBase;
import com.fairanb.controller.DiscountDefinitionAndLevelController;
import com.fairanb.model.DiscountLevel;
import com.fairanb.model.Paging;
import com.fairanb.model.request.DiscountDefinitionRequest;
import com.fairanb.model.response.DiscountDefinitionResponse;
import com.fairanb.repository.DiscountDefinitionRepository;
import com.fairanb.repository.DiscountLevelRepository;
import com.fairanb.repository.MerchantRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class DiscountDefinitionAndLevelControllerTest extends TestBase {

	private static final Logger log = LoggerFactory.getLogger(DiscountDefinitionAndLevelControllerTest.class);

	@Autowired
	DiscountDefinitionAndLevelController controller;

	@Autowired
	DiscountDefinitionRepository discountDefinitionRepository;

	@Autowired
	DiscountLevelRepository discountLevelRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@Test
	public void testServiceDiscountDefinition() throws Exception {

		DiscountDefinitionRequest request = new DiscountDefinitionRequest();
		BeanUtils.copyProperties(discountDefinition, request);
		Long id = 0L, merchantId = 0L, discountLevelId = 0L, discountDefinitionId = 0L;
		ResponseEntity<Rest> rest = null;
		DiscountDefinitionResponse response = null;
		try {
			C00.setEmail("merchant02200@fairanb.com");
			C00.setPhone("+1444-111-2224");
			C00.setLanguages(null);
			C00 = merchantRepository.save(C00);
			merchantId = C00.getId();
			request.setMerchantId(merchantId);
			rest = controller.listDiscountDefinition(null, 0, 0, null, null);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			log.debug(rest.getBody().getData().toString());
			request.setName(discountDefinition.getName());
			rest = controller.createDiscountDefinition(request, accessToken);
			response = (DiscountDefinitionResponse) rest.getBody().getData();
			id = response.getId();
			Assert.assertEquals(HttpStatus.CREATED.value(), rest.getBody().getStatus());
			Assert.assertNotNull(response);

			// Code already conflicted
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());

			// create discount definition for invalid merchant
			request.setName(discountDefinition.getName() + " " + discountDefinition.getName());
			request.setMerchantId(0L);
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// Update entire object with PUT method
			String updatedName = request.getName() + request.getName();
			request = new DiscountDefinitionRequest();
			request.setId(id);
			request.setName(updatedName);
			rest = controller.updateDiscountDefinition(id, request, accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// update discount definition with invalid id
			request = new DiscountDefinitionRequest();
			request.setId(0L);
			rest = controller.updateDiscountDefinition(0L, request, accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// update discount definition with invalid merchant id
			request = new DiscountDefinitionRequest();
			request.setId(id);
			request.setMerchantId(0L);
			rest = controller.updateDiscountDefinition(id, request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());

			// create discount definition without name
			request = new DiscountDefinitionRequest();
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), rest.getBody().getStatus());

			// create discount definition without merchantId
			request = new DiscountDefinitionRequest();
			request.setName("Test");
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), rest.getBody().getStatus());

			// create discount definition without start date and end date
			request = new DiscountDefinitionRequest();
			request.setName("Test");
			request.setMerchantId(merchantId);
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), rest.getBody().getStatus());

			// create discount definition without start date greater then end
			// date
			request = new DiscountDefinitionRequest();
			request.setName("Test");
			request.setMerchantId(merchantId);
			request.setStartDate(discountDefinition.getEndDate());
			request.setEndDate(discountDefinition.getStartDate());
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), rest.getBody().getStatus());

			// create anothe discount definition
			DiscountDefinitionRequest request1 = new DiscountDefinitionRequest();
			BeanUtils.copyProperties(discountDefinition, request1);
			request1.setMerchantId(merchantId);
			rest = controller.createDiscountDefinition(request1, accessToken);
			Assert.assertTrue(request1.hashCode() != request.hashCode());
			Assert.assertFalse(request1.equals(request));

			// update discount definition with existing discount definition name
			response = (DiscountDefinitionResponse) rest.getBody().getData();
			discountDefinitionId = response.getId();
			request = new DiscountDefinitionRequest();
			request.setId(id);
			request.setName(response.getName());
			request.setMerchantId(merchantId);
			rest = controller.updateDiscountDefinition(id, request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());

			// Get Discount definition by id
			rest = controller.getDiscountDefinition(id);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			DiscountDefinitionResponse discountDefinitionResponse = (DiscountDefinitionResponse) rest.getBody()
					.getData();
			Assert.assertEquals(updatedName, discountDefinitionResponse.getName());

			// Get Discount definition by id with invalid id
			rest = controller.getDiscountDefinition(0L);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// Search List by status
			rest = controller.listDiscountDefinition(null, 0, 0, null, null);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			Map<String, Object> map = (HashMap<String, Object>) rest.getBody().getData();
			Paging paging = (Paging) map.get(Paging.class.getSimpleName().toLowerCase());
			Assert.assertNotNull(paging);
			Assert.assertTrue(paging.getTotalRows() >= 1);
			List<DiscountDefinitionResponse> list = (List<DiscountDefinitionResponse>) map.get(JConstants.DATA_LIST);
			DiscountDefinitionResponse DiscountDefinitionResponse = list.get(0);
			Assert.assertNotNull(DiscountDefinitionResponse);

			// soft delete
			request = new DiscountDefinitionRequest();
			request.setId(id);
			rest = controller.deleteDiscountDefinition(request.getId(), accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// soft delete with invalid
			request = new DiscountDefinitionRequest();
			request.setId(0L);
			rest = controller.deleteDiscountDefinition(request.getId(), accessToken);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// create the discount definition for existing user and inactive
			// status
			request = new DiscountDefinitionRequest();
			BeanUtils.copyProperties(discountDefinition, request);
			request.setName(updatedName);
			request.setMerchantId(merchantId);
			rest = controller.createDiscountDefinition(request, accessToken);
			Assert.assertEquals(HttpStatus.LOCKED.value(), rest.getBody().getStatus());

			// create discount level
			DiscountLevel newDiscountLeve2 = new DiscountLevel();
			newDiscountLeve2.setName(discountLevel.getName());
			discountLevel = discountLevelRepository.save(discountLevel);
			discountLevelId = discountLevel.getId();
			Assert.assertTrue(discountLevel.hashCode() != newDiscountLeve2.hashCode());
			Assert.assertFalse(discountLevel.equals(newDiscountLeve2));

			// get discount level by id
			DiscountLevel newDiscountLevel = new DiscountLevel();
			newDiscountLevel.setName(discountLevel.getName());
			newDiscountLevel.setId(discountLevelId);
			rest = controller.getDiscountLevel(newDiscountLevel.getId());
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			newDiscountLevel = (DiscountLevel) rest.getBody().getData();
			Assert.assertEquals(newDiscountLevel.getId(), discountLevelId);

			// get discount level with invalid id
			rest = controller.getDiscountLevel(0L);
			Assert.assertEquals(HttpStatus.NOT_FOUND.value(), rest.getBody().getStatus());

			// get all discount level
			rest = controller.listDiscountLevel();
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			List<DiscountLevel> discountLevelList = new ArrayList<>();
			discountLevelList = (List<DiscountLevel>) rest.getBody().getData();
			Assert.assertNotNull(discountLevelList);
			Assert.assertTrue(discountLevelList.size() >= 1);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			// Clean if existed
			if (id > 0)
				discountDefinitionRepository.delete(id);
			if (discountDefinitionId > 0)
				discountDefinitionRepository.delete(discountDefinitionId);
			if (merchantId > 0)
				merchantRepository.delete(merchantId);
			if (discountLevelId > 0)
				discountLevelRepository.delete(discountLevelId);

		}
	}
}
