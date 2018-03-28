package com.fairanb.common;

import com.fairanb.common.JConstants.Status;
import com.fairanb.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;

import java.io.InputStream;
import java.util.Date;

/**
 * @longmt99
 */
public class TestBase extends Dummy {

	protected Merchant C00 = null;

	protected PromoCode promoCode = null;

	protected MerchantConfig merchantConfig = null;
	protected Zone zone = null;
	protected GeoZone geoZone = null;

	protected DiscountDefinition discountDefinition = null;

	protected DiscountLevel discountLevel = null;

	protected Language language = null;

	protected DiscountCondition discountCondition = null;
	protected DiscountResult discountResult = null;

	@Before
	public void setUp() throws Exception {
		env = System.getenv("SPRING_PROFILES_ACTIVE");
		log.info("set Up test env [" + env + "] " + datasource);

		C00 = (Merchant) parserDummy(new Merchant());
		promoCode = (PromoCode) parserDummyDataForPromoCode(new PromoCode());

		merchantConfig = (MerchantConfig) parserDummyDataForMerchantConfig(new MerchantConfig());

		discountDefinition = (DiscountDefinition) parserDummyDataForDiscountDefinition(new DiscountDefinition());

		discountLevel = (DiscountLevel) parserDummyDataForDiscountLevel(new DiscountLevel());

		merchantConfig = (MerchantConfig) parserDummyDataForMerchantConfig(new MerchantConfig());
		zone = (Zone) parserDummyDataForZone(new Zone());
		geoZone = (GeoZone) parserDummyDataForGeoZone(new GeoZone());
		language = (Language) parserDummyDataForLanguage(new Language());
		discountCondition = (DiscountCondition) parserDummyDataForDiscountCondition(new DiscountCondition());
		discountResult = (DiscountResult) parserDummyDataForDiscountResult(new DiscountResult());
	}

	public Object parserDummy(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "Merchant";
		InputStream inputStream = null;
		if (object instanceof Merchant) {
			jsonName = Merchant.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			Merchant parser = om.readValue(inputStream, Merchant.class);
			parser.setCreatedDate(new Date());
			parser.setStatus(Status.ACTIVE.name());
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForPromoCode(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "PromoCode";
		InputStream inputStream = null;
		if (object instanceof PromoCode) {
			jsonName = PromoCode.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			PromoCode parser = om.readValue(inputStream, PromoCode.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForMerchantConfig(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "MerchantConfig";
		InputStream inputStream = null;
		if (object instanceof MerchantConfig) {
			jsonName = MerchantConfig.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			MerchantConfig parser = om.readValue(inputStream, MerchantConfig.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForZone(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "Zone";
		InputStream inputStream = null;
		if (object instanceof Zone) {
			jsonName = Zone.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			Zone parser = om.readValue(inputStream, Zone.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForGeoZone(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "GeoZone";
		InputStream inputStream = null;
		if (object instanceof GeoZone) {
			jsonName = GeoZone.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			GeoZone parser = om.readValue(inputStream, GeoZone.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForLanguage(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "Language";
		InputStream inputStream = null;
		if (object instanceof Language) {
			jsonName = Language.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			Language parser = om.readValue(inputStream, Language.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForDiscountDefinition(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "DiscountDefinition";
		InputStream inputStream = null;
		if (object instanceof DiscountDefinition) {
			jsonName = DiscountDefinition.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			DiscountDefinition parser = om.readValue(inputStream, DiscountDefinition.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForDiscountLevel(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "DiscountLevel";
		InputStream inputStream = null;
		if (object instanceof DiscountLevel) {
			jsonName = DiscountLevel.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			DiscountLevel parser = om.readValue(inputStream, DiscountLevel.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForDiscountCondition(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "DiscountCondition";
		InputStream inputStream = null;
		if (object instanceof DiscountCondition) {
			jsonName = DiscountCondition.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			DiscountCondition parser = om.readValue(inputStream, DiscountCondition.class);
			return parser;
		}
		return null;
	}

	public Object parserDummyDataForDiscountResult(Object object) throws Exception {
		ObjectMapper om = new ObjectMapper();
		String jsonName = "DiscountResult";
		InputStream inputStream = null;
		if (object instanceof DiscountResult) {
			jsonName = DiscountResult.class.getSimpleName();
			inputStream = this.getClass().getResourceAsStream("/dummy/" + jsonName + ".json");
			DiscountResult parser = om.readValue(inputStream, DiscountResult.class);
			return parser;
		}
		return null;
	}

}
