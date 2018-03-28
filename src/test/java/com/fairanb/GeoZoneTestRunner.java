package com.fairanb;
import com.fairanb.Controller.GeoZoneControllerTest;
import com.fairanb.repository.GeoZoneRepository;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

		// All Mappers
		GeoZoneRepository.class,

		// All Service
        GeoZoneControllerTest.class, })
public final class GeoZoneTestRunner {

}
