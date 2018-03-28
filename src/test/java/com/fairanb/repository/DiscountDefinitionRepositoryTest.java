package com.fairanb.repository;

import com.fairanb.common.JConstants.Status;
import com.fairanb.common.TestBase;
import com.fairanb.model.DiscountDefinition;
import com.fairanb.model.Paging;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountDefinitionRepositoryTest extends TestBase {

	@Autowired
	DiscountDefinitionRepository discountDefinitionRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDDiscountDefinition() {

		Long id = 0L, merchantId = 0L;
		try {
			// Create
			C00.setEmail("merchant02200@fairanb.com");
			C00.setPhone("+1444-111-2224");
			C00 = merchantRepository.save(C00);
			merchantId = C00.getId();
			discountDefinition.setMerchant(C00);
			discountDefinition = discountDefinitionRepository.save(discountDefinition);

			id = discountDefinition.getId();
			Assert.assertTrue(id > 0);

			// Get discount definition by Id
			DiscountDefinition newDiscountDefinition = discountDefinitionRepository.findOne(id);
			Assert.assertNotNull(newDiscountDefinition);
			Assert.assertEquals(id, newDiscountDefinition.getId());
			Assert.assertEquals(discountDefinition.getStatus(), newDiscountDefinition.getStatus());

			// Update
			String name = discountDefinition.getName() + ' ' + discountDefinition.getName();
			discountDefinition.setName(name);
			discountDefinition = discountDefinitionRepository.save(discountDefinition);
			Assert.assertEquals(name, discountDefinition.getName());

			// Get All discount definition by status
			List<DiscountDefinition> DiscountDefinitionList = new ArrayList<>();
			PageRequest paging = new PageRequest(Paging.PAGE - 1, Paging.SIZE);
			Page<DiscountDefinition> list = discountDefinitionRepository.findByStatus(Status.ACTIVE.name(), paging);
			log.debug("Test find all, found [" + list.getTotalElements() + "]");
			DiscountDefinition discountDefinition = list.getContent().get(0);
			log.debug("Discount Definition [" + discountDefinition + "]");
			Assert.assertTrue(list.getTotalElements() > 0);

			// Get by code
			DiscountDefinition discountDefinition1 = discountDefinitionRepository.findByName(name);
			Assert.assertNotNull(discountDefinition1);
			Assert.assertEquals(discountDefinition1.getName(), name);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			discountDefinitionRepository.delete(id);
			merchantRepository.delete(merchantId);
			Assert.assertNull(discountDefinitionRepository.findOne(id));
			Assert.assertNull(merchantRepository.findOne(id));
		}
	}
}
