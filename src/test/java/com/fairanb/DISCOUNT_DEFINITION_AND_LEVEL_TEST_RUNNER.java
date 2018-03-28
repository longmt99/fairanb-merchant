package com.fairanb;

import com.fairanb.Controller.DiscountDefinitionAndLevelControllerTest;
import com.fairanb.repository.DiscountDefinitionRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		DiscountDefinitionRepositoryTest.class,

		// All Service
		DiscountDefinitionAndLevelControllerTest.class, })
public class DISCOUNT_DEFINITION_AND_LEVEL_TEST_RUNNER {

}
