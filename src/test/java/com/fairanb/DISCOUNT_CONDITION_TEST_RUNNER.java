package com.fairanb;

import com.fairanb.Controller.DiscountConditionControllerTest;
import com.fairanb.repository.DiscountConditionRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		DiscountConditionRepositoryTest.class,

		// All Service
		DiscountConditionControllerTest.class, })
public class DISCOUNT_CONDITION_TEST_RUNNER {

}
