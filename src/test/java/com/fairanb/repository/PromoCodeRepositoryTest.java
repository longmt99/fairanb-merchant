package com.fairanb.repository;

import com.fairanb.common.JConstants.Status;
import com.fairanb.common.TestBase;
import com.fairanb.model.PromoCode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PromoCodeRepositoryTest extends TestBase {

	@Autowired
	PromoCodeRepository promoCodeRepository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDPromoCode() {

		Long id = 0L;
		try {
			// Create
			promoCode = promoCodeRepository.save(promoCode);

			id = promoCode.getId();
			Assert.assertTrue(id > 0);

			// Get promocode by Id
			PromoCode newPromoCode = promoCodeRepository.findOne(id);
			Assert.assertNotNull(newPromoCode);
			Assert.assertEquals(id, newPromoCode.getId());
			Assert.assertEquals(promoCode.getStatus(), newPromoCode.getStatus());

			// Update
			String code = promoCode.getCode() + ' ' + promoCode.getCode();
			promoCode.setCode(code);
			promoCode = promoCodeRepository.save(promoCode);
			Assert.assertEquals(code, promoCode.getCode());

			// Get All promocodes by status
			List<PromoCode> promoCodeList = new ArrayList<>();
			promoCodeList = promoCodeRepository.findByStatus(Status.ACTIVE.name());
			Assert.assertTrue(promoCodeList.size() > 0);
			
			//Get by code
			PromoCode promoCode1 = promoCodeRepository.findByCode(promoCode.getCode());
			Assert.assertNotNull(promoCode1);
			Assert.assertEquals(promoCode1.getCode(), promoCode.getCode());
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			promoCodeRepository.delete(id);
			Assert.assertNull(promoCodeRepository.findOne(id));
		}
	}

}
