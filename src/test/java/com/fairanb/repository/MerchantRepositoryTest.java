package com.fairanb.repository;

import com.fairanb.common.JConstants.Status;
import com.fairanb.common.TestBase;
import com.fairanb.model.Language;
import com.fairanb.model.Merchant;
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

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MerchantRepositoryTest extends TestBase {

    @Autowired
    protected MerchantRepository merchantRepository;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void destroy() throws Exception {
    }

    @Test
    public void testCRUDMerchant() {

        Long id = 0L;
        try {
            // Create
            C00 = merchantRepository.save(C00);

            id = C00.getId();
            Assert.assertTrue(id > 0);

            // Read Merchant by Id
            Merchant merchant = merchantRepository.findOne(id);

            Assert.assertNotNull(merchant);
            Assert.assertEquals(id, merchant.getId());
            Assert.assertEquals(C00.getStatus(), merchant.getStatus());
            String email = C00.getEmail();

            // Read Merchant by email
            merchant = merchantRepository.findByEmail(email);
            Assert.assertNotNull(merchant);
            Assert.assertEquals(id, merchant.getId());
            Assert.assertEquals(email, merchant.getEmail());

            // Update
            String updateStatus = Status.INACTIVE.name();
            C00.setStatus(updateStatus);
            C00 = merchantRepository.save(C00);
            Assert.assertEquals(updateStatus, C00.getStatus());
            // List
            PageRequest paging = new PageRequest(Paging.PAGE - 1, Paging.SIZE);
            Page<Merchant> list = merchantRepository.findAll(paging);
            log.debug("Test find all, found [" + list.getTotalElements() + "]");
            Merchant retrivedMerchant = list.getContent().get(0);
            log.debug("Merchant [" + retrivedMerchant + "]");
            List<Language> languages = retrivedMerchant.getLanguages();
            for (Language language : languages) {
                log.debug("Merchant language [" + language + "]");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            merchantRepository.delete(id);
            Assert.assertNull(merchantRepository.findOne(id));
        }
    }

}
