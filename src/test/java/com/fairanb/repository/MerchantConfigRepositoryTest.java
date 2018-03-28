package com.fairanb.repository;

import com.fairanb.common.JConstants.Status;
import com.fairanb.common.TestBase;
import com.fairanb.model.MerchantConfig;
import com.fairanb.model.Paging;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantConfigRepositoryTest extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(MerchantConfigRepositoryTest.class);
    @Autowired
    protected MerchantConfigRepository repository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void destroy() throws Exception {
    }

    @Test
    public void testCRUDMerchantConfig() {

        Long id = 0L;
        try {
            log.info("*****Create MerchantConfig******** ");
            merchantConfig = repository.save(merchantConfig);

            id = merchantConfig.getId();
            Assert.assertTrue(id > 0);


            log.info("*****Read MerchantConfig by Id  ******** ");
            MerchantConfig createdMerchantConfig = repository.findOne(id);

            Assert.assertNotNull(createdMerchantConfig);
            Assert.assertEquals(id, createdMerchantConfig.getId());
            Assert.assertEquals(createdMerchantConfig.getStatus(), merchantConfig.getStatus());


            //
            log.info("*****Update MerchantConfig by Id  ******** ");
            String updateStatus = Status.INACTIVE.name();
            merchantConfig.setStatus(updateStatus);
            merchantConfig = repository.save(merchantConfig);
            Assert.assertEquals(updateStatus, merchantConfig.getStatus());

            //
            log.info("***** MerchantConfig List  ******** ");
            PageRequest paging = new PageRequest(Paging.PAGE - 1, Paging.SIZE);
            Page<MerchantConfig> list = repository.findAll(paging);
            log.debug("Test find all, found [" + list.getTotalElements() + "]");
            MerchantConfig merchantConfigs = list.getContent().get(0);
            log.debug("merchantConfigs [" + merchantConfigs + "]");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //
            log.info("***** MerchantConfig delete By ID  ******** ");
            repository.delete(id);
            Assert.assertNull(repository.findOne(id));
        }
    }

}
