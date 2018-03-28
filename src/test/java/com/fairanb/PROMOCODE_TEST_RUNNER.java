package com.fairanb;

import com.fairanb.Controller.PromoCodeSequenceTest;
import com.fairanb.repository.PromoCodeRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		PromoCodeRepositoryTest.class,

		// All Service
		PromoCodeSequenceTest.class, })
public final class PROMOCODE_TEST_RUNNER {

}
