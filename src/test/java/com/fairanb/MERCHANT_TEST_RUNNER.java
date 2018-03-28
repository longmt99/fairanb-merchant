package com.fairanb;


import com.fairanb.Controller.MerchantSequenceTest;
import com.fairanb.repository.MerchantRepositoryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * All test cases
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

	// All Mappers
	MerchantRepositoryTest.class,

	// All Service
	MerchantSequenceTest.class,
	})
public final class MERCHANT_TEST_RUNNER {}