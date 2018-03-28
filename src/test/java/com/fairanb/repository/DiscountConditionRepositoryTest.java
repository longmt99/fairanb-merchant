package com.fairanb.repository;

import com.fairanb.common.TestBase;
import com.fairanb.model.DiscountCondition;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountConditionRepositoryTest extends TestBase {

	@Autowired
	DiscountDefinitionRepository discountDefinitionRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	DiscountLevelRepository discountLevelRepository;

	@Autowired
	DiscountConditionRepository discountConditionRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDDiscountCondition() {

		Long definitionId = 0L, merchantId = 0L, conditionId = 0L, levelId = 0L;
		try {
			// Create
			C00.setEmail("merchant02200@fairanb.com");
			C00.setPhone("+1444-111-2224");
			C00 = merchantRepository.save(C00);
			merchantId = C00.getId();
			discountDefinition.setMerchant(C00);
			discountDefinition = discountDefinitionRepository.save(discountDefinition);
			definitionId = discountDefinition.getId();
			discountLevel = discountLevelRepository.save(discountLevel);
			levelId = discountLevel.getId();

			discountCondition.setDiscountLevelId(levelId);
			discountCondition.setDiscountDefinitionId(definitionId);
			discountCondition = discountConditionRepository.save(discountCondition);
			conditionId = discountCondition.getId();
			Assert.assertTrue(conditionId > 0);

			// Update
			BigDecimal value = new BigDecimal(10);
			BigDecimal newAmount = discountCondition.getAmount().add(value);
			discountCondition.setAmount(newAmount);
			discountCondition = discountConditionRepository.save(discountCondition);
			Assert.assertEquals(newAmount, discountCondition.getAmount());

			// Get by code
			DiscountCondition discountCondition1 = discountConditionRepository.findOne(discountCondition.getId());
			Assert.assertNotNull(discountCondition1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			discountConditionRepository.delete(conditionId);
			discountDefinitionRepository.delete(definitionId);
			merchantRepository.delete(merchantId);
			discountLevelRepository.delete(levelId);
			Assert.assertNull(discountDefinitionRepository.findOne(definitionId));
			Assert.assertNull(discountLevelRepository.findOne(levelId));
			Assert.assertNull(discountConditionRepository.findOne(conditionId));
			Assert.assertNull(merchantRepository.findOne(merchantId));
		}
	}

}
