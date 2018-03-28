package com.fairanb;

import com.fairanb.Controller.MerchantConfigControllerTest;
import com.fairanb.repository.MerchantConfigRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		MerchantConfigRepositoryTest.class,

		// All Service
		MerchantConfigControllerTest.class, })
public final class MerchantConfig_Test_Runner {

}
