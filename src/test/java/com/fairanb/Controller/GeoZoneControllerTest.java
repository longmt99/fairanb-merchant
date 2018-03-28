package com.fairanb.Controller;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.TestBase;
import com.fairanb.controller.GeoZoneController;
import com.fairanb.model.Paging;
import com.fairanb.model.request.GeoZoneRequest;
import com.fairanb.model.response.GeoZoneResponse;
import com.fairanb.repository.GeoZoneRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class GeoZoneControllerTest extends TestBase {
	@Autowired
	private GeoZoneRepository repository;

	@Autowired
	private GeoZoneController controller;

	@Before
	public void setUp() throws Exception {
	dummyAccessToken(USER_ID, MERCHANT_ID);
		super.setUp();
	}

	@Test
	public void testGeoZoneController() throws Exception {

		GeoZoneRequest request = new GeoZoneRequest();
		BeanUtils.copyProperties(geoZone, request);
		Long id = 0L;
		ResponseEntity<Rest> rest = null;
		GeoZoneResponse response = null;
		try {
			rest = controller.listGeoZone(1,10,"","");
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			log.debug(rest.getBody().getData().toString());
            request.setCode(geoZone.getCode());
			request.setName(geoZone.getName());
			request.setMerchantId(geoZone.getMerchantId());

			rest = controller.createGeoZone(request, accessToken);
			response = (GeoZoneResponse) rest.getBody().getData();
			id = response.getId();
			Assert.assertEquals(HttpStatus.CREATED.value(), rest.getBody().getStatus());
			Assert.assertNotNull(response);

			// Code already conflicted
			rest = controller.createGeoZone(request, accessToken);
			Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());

			// Update entire object with PUT method
            String name="EuropeSample2/Andorra";
			request = new GeoZoneRequest();
			request.setId(id);
            request.setName(name);
			rest = controller.updateGeoZone(request, accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

			// Get Customer
			rest = controller.getGeoZone(id);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			GeoZoneResponse geoZoneResponse = (GeoZoneResponse) rest.getBody().getData();
			Assert.assertEquals(name, geoZoneResponse.getName());

			// Search List by status
			rest = controller.listGeoZone(Paging.PAGE, Paging.SIZE, Paging.DESC, Paging.SORT);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			Map<String, Object> map = (HashMap<String, Object>) rest.getBody().getData();
			Paging paging = (Paging) map.get(Paging.class.getSimpleName().toLowerCase());
			Assert.assertNotNull(paging);
			Assert.assertTrue(paging.getTotalRows() >= 1);
			List<GeoZoneResponse> list = (List<GeoZoneResponse>) map.get(JConstants.DATA_LIST);
			GeoZoneResponse geoZoneResp = list.get(0);
			Assert.assertTrue(id.compareTo(geoZoneResp.getId()) == 0);

			// soft delete
			request = new GeoZoneRequest();
			request.setId(id);
			rest = controller.deleteGeoZone(request.getId(), accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			// Clean if existed
			if (id > 0) {
				log.info("*****Delete method delete By Id ******** ");
				repository.delete(id);
				controller.deleteGeoZone(id, accessToken);
			}
		}
	}
}
