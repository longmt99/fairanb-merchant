package com.fairanb.repository;

import com.fairanb.common.TestBase;
import com.fairanb.model.Paging;
import com.fairanb.model.Zone;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZoneRepositoryTest extends TestBase {

	@Autowired
	ZoneRepository repository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDZone() {
		Long id = 0L;
		try {
			log.info("*****Create Zone******** ");
			zone = repository.save(zone);

			id = zone.getId();
			Assert.assertTrue(id > 0);


			log.info("*****Read Zone by Id  ******** ");
			Zone getZoneById = repository.findOne(id);

			Assert.assertNotNull(getZoneById);
			Assert.assertEquals(id, getZoneById.getId());



			//
			log.info("*****Update Zone by Id  ******** ");
            Long countryId= 1L;
			zone.setCountryId(countryId);
			zone = repository.save(zone);
			Assert.assertNotNull(zone);
			//
			log.info("***** Zone List  ******** ");
			PageRequest paging = new PageRequest(Paging.PAGE - 1, Paging.SIZE);
			Page<Zone> list = repository.findAll(paging);
			log.debug("Test find all, found [" + list.getTotalElements() + "]");
			Zone zone = list.getContent().get(0);
			log.debug("Zone [" + zone + "]");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
			log.info("*****Zone delete By ID  ******** ");
			repository.delete(id);
			Assert.assertNull(repository.findOne(id));
		}
	}

}
