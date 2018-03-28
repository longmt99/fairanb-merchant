package com.fairanb.service;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.TestBase;
import com.fairanb.model.GeoZone;
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
public class GeoZoneServiceTest extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(GeoZoneServiceTest.class);

	@Autowired
	protected GeoZoneService service;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	private Long id=0L;
	private String code="Ad";
	private String name="EuropeSample/Andorra";
    private Long merchantId=1L;

	@Test
	public void  geoZoneServiceMethodsTest() {

		try {
			log.info("*****Create MerchantConfig******** ");
			GeoZone actual = service.save(geoZone);
			id=actual.getId();
			log.info("***** MerchantConfig  ID ::"+id);
			Assert.assertEquals(actual.getCode(), code);
			Assert.assertEquals(actual.getName(), name);
            Assert.assertEquals(actual.getMerchantId(), merchantId);

			log.info("*****Read MerchantConfig by Id  ******** ");
			GeoZone getGeoZone = service.findById(id);
			Assert.assertEquals(getGeoZone.getCode(), code);
			Assert.assertEquals(getGeoZone.getName(), name);
            Assert.assertEquals(getGeoZone.getMerchantId(), merchantId);

			log.info("*****Update MerchantConfig by Id  ******** ");
			GeoZone   updateGeoZone=null;
            String name="EuropeSample2/Andorra";
			GeoZone   findGeoZone=service.findById(id);
			if(findGeoZone!=null) {
				findGeoZone.setName(name);
				updateGeoZone = service.save(findGeoZone);
			}
			Assert.assertEquals(updateGeoZone.getName(), name);
			log.info("***** MerchantConfig delete By ID  ******** ");
			service.delete(id);
			Assert.assertNull(service.findById(id));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
