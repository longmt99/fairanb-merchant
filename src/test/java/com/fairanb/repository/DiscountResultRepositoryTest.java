package com.fairanb.repository;

import com.fairanb.common.TestBase;
import com.fairanb.model.DiscountResult;
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
public class DiscountResultRepositoryTest extends TestBase {

	@Autowired
	DiscountDefinitionRepository discountDefinitionRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	DiscountLevelRepository discountLevelRepository;

	@Autowired
	DiscountResultRepository discountResultRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDDiscountResult() {

		Long definitionId = 0L, merchantId = 0L, resultId = 0L, levelId = 0L;
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

			discountResult.setDiscountLevelId(levelId);
			discountResult.setDiscountDefinitionId(definitionId);
			discountResult = discountResultRepository.save(discountResult);
			resultId = discountResult.getId();
			Assert.assertTrue(resultId > 0);

			// Update
			BigDecimal value = new BigDecimal(10);
			BigDecimal amountOff = discountResult.getAmountOff().add(value);
			discountResult.setAmountOff(amountOff);
			discountResult = discountResultRepository.save(discountResult);
			Assert.assertEquals(amountOff, discountResult.getAmountOff());

			// Get by code
			DiscountResult discountResult1 = discountResultRepository.findOne(discountResult.getId());
			Assert.assertNotNull(discountResult1);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			discountResultRepository.delete(resultId);
			discountDefinitionRepository.delete(definitionId);
			merchantRepository.delete(merchantId);
			discountLevelRepository.delete(levelId);
			Assert.assertNull(discountDefinitionRepository.findOne(definitionId));
			Assert.assertNull(discountLevelRepository.findOne(levelId));
			Assert.assertNull(discountResultRepository.findOne(resultId));
			Assert.assertNull(merchantRepository.findOne(merchantId));
		}
	}
}
