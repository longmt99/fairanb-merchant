package com.fairanb.Controller;

import com.fairanb.MerchantRestAPI;
import com.fairanb.common.JConstants;
import com.fairanb.common.JConstants.Status;
import com.fairanb.common.Rest;
import com.fairanb.common.TestBase;
import com.fairanb.controller.MerchantController;
import com.fairanb.model.Paging;
import com.fairanb.model.request.MerchantRequest;
import com.fairanb.model.response.MerchantResponse;
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
@SpringBootTest(classes=MerchantRestAPI.class)
public class MerchantSequenceTest extends TestBase {

    @Autowired
    private MerchantController controller;
    @Before
    public void setUp() throws Exception {
       // dummyAccessToken(USER_ID, MERCHANT_ID);
        super.setUp();
    }

    @Test
    public void testServiceMerchant() throws Exception {

        MerchantRequest request = new MerchantRequest();
        BeanUtils.copyProperties(C00, request);
        Long id = 0L;
        ResponseEntity<Rest> rest = null;
        MerchantResponse response= null;
        try {
            rest = controller.listMerchant( null, 0, 0, null, null);
            Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
            log.debug(rest.getBody().getData().toString());
            rest = controller.createMerchant(request, accessToken);
            response = (MerchantResponse) rest.getBody().getData();
            id = response.getId();
            Assert.assertEquals(HttpStatus.CREATED.value(), rest.getBody().getStatus());
            Assert.assertNotNull(response);

            // Email already conflicted
            rest = controller.createMerchant(request, accessToken);
            Assert.assertEquals(HttpStatus.CONFLICT.value(), rest.getBody().getStatus());

            // Update entire object with PUT method
            String updateStatus = Status.INACTIVE.name();
            request = new MerchantRequest();
            request.setId(id);
            request.setStatus(updateStatus);
            rest = controller.updateMerchant(request, accessToken);
            Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());

            // Get Merchant
            rest = controller.getMerchant(id);
            Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
            response = (MerchantResponse) rest.getBody().getData();
            Assert.assertEquals(updateStatus, response.getStatus());


            // Search List
            rest = controller.listMerchant( null, Paging.PAGE, Paging.SIZE, Paging.DESC, Paging.SORT);
            Assert.assertEquals(HttpStatus.OK.value(), rest.getBody().getStatus());
            Map<String, Object> map = (HashMap<String, Object>) rest.getBody().getData();
            Paging paging = (Paging) map.get(Paging.class.getSimpleName().toLowerCase());
            Assert.assertNotNull(paging);
            Assert.assertTrue(paging.getTotalRows() >= 1);
            List<MerchantResponse> list = (List<MerchantResponse>) map.get(JConstants.DATA_LIST);
            MerchantResponse merchant = list.get(0);
            Assert.assertTrue(id.compareTo(merchant.getId()) == 0);
        }catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        } finally {
            // Clean if existed
            if(id>0){
                controller.deleteMerchant(id, accessToken);
            }
        }
    }
}
