package com.fairanb.repository;


import com.fairanb.common.TestBase;
import com.fairanb.model.Language;
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
public class LanguageRepositoryTest extends TestBase {

	@Autowired
	LanguageRepository repository;

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void destroy() throws Exception {
	}

	@Test
	public void testCRUDLanguage() {

		Long id = 0L;
		try {
			log.info("*****Create language******** ");
			language = repository.save(language);

			id = language.getId();
			Assert.assertTrue(id > 0);


			log.info("*****Read language by Id  ******** ");
			Language getlanguageeById = repository.findOne(id);

			Assert.assertNotNull(getlanguageeById);
			Assert.assertEquals(id, getlanguageeById.getId());



			//
			log.info("*****Update language by Id  ******** ");
			String name = "Bang";
			language.setName(name);
			language = repository.save(language);
			Assert.assertEquals(name, language.getName());

			//
			log.info("***** language List  ******** ");
			PageRequest paging = new PageRequest(Paging.PAGE - 1, Paging.SIZE);
			Page<Language> list = repository.findAll(paging);
			log.debug("Test find all, found [" + list.getTotalElements() + "]");
			Language language = list.getContent().get(0);
			log.debug("geoZone [" + language + "]");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//
			log.info("*****Zone language By ID  ******** ");
			repository.delete(id);
			Assert.assertNull(repository.findOne(id));
		}
	}
}