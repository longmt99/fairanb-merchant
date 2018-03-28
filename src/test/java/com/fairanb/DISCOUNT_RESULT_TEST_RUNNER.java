package com.fairanb;

import com.fairanb.Controller.DiscountResultControllerTest;
import com.fairanb.repository.DiscountResultRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		DiscountResultRepositoryTest.class,

		// All Service
		DiscountResultControllerTest.class, })
public class DISCOUNT_RESULT_TEST_RUNNER {

}
