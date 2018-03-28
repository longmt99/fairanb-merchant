package com.fairanb;


import com.fairanb.Controller.ZoneControllerTest;
import com.fairanb.repository.ZoneRepository;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		ZoneRepository.class,

		// All Service
		ZoneControllerTest.class, })
public final class ZoneTestRunner {

}
