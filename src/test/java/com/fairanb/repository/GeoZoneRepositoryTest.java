package com.fairanb.repository;

import com.fairanb.common.TestBase;
import com.fairanb.model.GeoZone;
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
/**
 * @mustafamym
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoZoneRepositoryTest extends TestBase {

	@Autowired
	GeoZoneRepository repository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDGeoZone() {

		Long id = 0L;
		try {
		log.info("*****Create geoZone******** ");
		geoZone = repository.save(geoZone);

		id = geoZone.getId();
		Assert.assertTrue(id > 0);


		log.info("*****Read GeoZone by Id  ******** ");
		GeoZone getgeoZoneById = repository.findOne(id);

		Assert.assertNotNull(getgeoZoneById);
		Assert.assertEquals(id, getgeoZoneById.getId());



		//
		log.info("***** GeoZone List  ******** ");
		PageRequest paging = new PageRequest(Paging.PAGE - 1, Paging.SIZE);
		Page<GeoZone> list = repository.findAll(paging);
		log.debug("Test find all, found [" + list.getTotalElements() + "]");
			GeoZone geoZone = list.getContent().get(0);
		log.debug("geoZone [" + geoZone + "]");

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		//
		log.info("*****Zone GeoZone By ID  ******** ");
		repository.delete(id);
		Assert.assertNull(repository.findOne(id));
	}
}

}
