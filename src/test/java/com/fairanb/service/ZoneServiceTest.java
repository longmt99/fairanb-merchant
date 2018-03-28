package com.fairanb.service;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.TestBase;
import com.fairanb.model.Zone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class ZoneServiceTest extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(ZoneServiceTest.class);

	@Autowired
	protected ZoneService service;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	private Long id=0L;
	private Long countryId=1L;
	private String code="zD";
	private String name="sampleArea/Andorra";


	@Test
	public void  zoneServiceMethods() {

		try {
			log.info("*****Create MerchantConfig******** ");
			Zone actual = service.save(zone);
			id=actual.getId();
			log.info("***** MerchantConfig  ID ::"+id);
			Assert.assertEquals(actual.getCountryId(), countryId);
			Assert.assertEquals(actual.getCode(), code);
			Assert.assertEquals(actual.getName(), name);

			log.info("*****Read MerchantConfig by Id  ******** ");
			Zone getZone = service.findById(id);
			Assert.assertEquals(getZone.getCountryId(), countryId);
			Assert.assertEquals(getZone.getCode(), code);
			Assert.assertEquals(getZone.getName(), name);

			log.info("*****Update MerchantConfig by Id  ******** ");
			Zone   update=null;
           String name="Asia/India";
			Zone   findZone=service.findById(id);
			if(findZone!=null) {
				findZone.setName(name);
				update = service.save(findZone);
			}
			Assert.assertEquals(update.getName(), name);
			log.info("***** MerchantConfig delete By ID  ******** ");
			service.delete(id);
			Assert.assertNull(service.findById(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
