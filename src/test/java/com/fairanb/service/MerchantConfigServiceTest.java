package com.fairanb.service;

import com.fairanb.common.TestBase;
import com.fairanb.model.MerchantConfig;
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
@SpringBootTest
public class MerchantConfigServiceTest extends TestBase {
    private static final Logger log = LoggerFactory.getLogger(MerchantConfigServiceTest.class);

    @Autowired
    protected MerchantConfigService service;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void destroy() throws Exception {
    }
    private Long id=0L;
    private static final long merchantId  = 1;
    private String configKey="CONFIG";
    private String type="CONFIG";
    private String value="{\"protocol\":\"smtp\",\"password\":\"\",\"smtpAuth\":true}";
    private String status="ACTIVE";

    @Test
    public void merchantConfigServiceMethods() {

        try {
            log.info("*****Create MerchantConfig******** ");
            MerchantConfig actual = service.save(merchantConfig);
            id=actual.getId();
            log.info("***** MerchantConfig  ID ::"+id);
            Assert.assertEquals(actual.getConfigKey(), configKey);
            Assert.assertEquals(actual.getType(), type);
            Assert.assertEquals(actual.getStatus(), status);

            log.info("*****Read MerchantConfig by Id  ******** ");
            MerchantConfig merchantConfig = service.findOne(id);
            Assert.assertEquals(actual.getConfigKey(), configKey);
            Assert.assertEquals(actual.getType(), type);
            Assert.assertEquals(actual.getStatus(), status);

            log.info("*****Update MerchantConfig by Id  ******** ");
            MerchantConfig   update=null;
           String configKey="BDN";
            MerchantConfig   findMerchantConfig=	service.findOne(id);
            if(findMerchantConfig!=null) {
                findMerchantConfig.setConfigKey(configKey);
                update = service.save(findMerchantConfig);
            }
        Assert.assertEquals(update.getConfigKey(), configKey);
            log.info("***** MerchantConfig delete By ID  ******** ");
        service.delete(id);
        Assert.assertNull(service.findOne(id));
    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
