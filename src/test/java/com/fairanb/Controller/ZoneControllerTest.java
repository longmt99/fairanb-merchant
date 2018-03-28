package com.fairanb.Controller;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.TestBase;
import com.fairanb.controller.ZoneController;
import com.fairanb.model.Paging;
import com.fairanb.model.request.ZoneRequest;
import com.fairanb.model.response.ZoneResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ZoneControllerTest extends TestBase {
	private static final Logger log = LoggerFactory.getLogger(MerchantConfigControllerTest.class);
	@Autowired
	private ZoneController controller;
	@Before
	public void setUp() throws Exception {
		// dummyAccessToken(USER_ID, MERCHANT_ID);
		super.setUp();
	}
	ModelMapper modelMapper=new ModelMapper() ;
	@Test
	public void testZoneController() throws Exception {

		ZoneRequest request = new ZoneRequest();
		//BeanUtils.copyProperties(merchantConfig, request);

		request=modelMapper.map(zone, ZoneRequest.class);
		Long id = 0L;
		ResponseEntity<Rest> rest = null;
		ZoneResponse response= null;
		try {
			log.info("*****Create MerchantConfig******** ");

			rest = controller.createZone(request, accessToken);
			//Merchant merchant = request.copyBean(request);
			response =  modelMapper.map(rest.getBody().getData(), ZoneResponse.class);
			id = response.getId();
			Assert.assertEquals(HttpStatus.CREATED.value(), rest.getBody().getStatus());
			Assert.assertNotNull(response);
			log.debug(rest.getBody().getData().toString());

			log.info("*****Update entire object with PUT method******** ");
			String updateStatus = JConstants.Status.INACTIVE.name();
			request.setId(id);
			rest = controller.updateZone(request, accessToken);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			log.debug(rest.getBody().getData().toString());

			log.info("*****Get method By Id******** ");
			// Get Merchant
			rest = controller.getZone(id);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			response =  modelMapper.map(rest.getBody().getData(), ZoneResponse.class);
			log.debug(rest.getBody().getData().toString());


			log.info("*****Get method  Search List******** ");
			// Search List
			rest = controller.listZone(Paging.PAGE, Paging.SIZE, Paging.DESC, Paging.SORT);
			Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
			Map<String, Object> map = (HashMap<String, Object>) rest.getBody().getData();
			Paging paging = (Paging) map.get(Paging.class.getSimpleName().toLowerCase());
			Assert.assertNotNull(paging);
			Assert.assertTrue(paging.getTotalRows() >= 1);
			List<ZoneResponse> list = (List<ZoneResponse>) map.get(JConstants.DATA_LIST);
			ZoneResponse zone = list.get(0);

		}catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} finally {
			// Clean if existed
			if(id>0){
				log.info("*****Delete method delete By Id ******** ");
				controller.deleteZone(id, accessToken);
			}
		}
	}
}
