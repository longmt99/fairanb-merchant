package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.Country;
import com.fairanb.model.CountryDescription;
import com.fairanb.model.Language;
import com.fairanb.model.Paging;
import com.fairanb.model.request.CountryRequest;
import com.fairanb.model.response.CountryDescriptionResponse;
import com.fairanb.model.response.CountryResponse;
import com.fairanb.model.response.CountryThirdServiceResponse;
import com.fairanb.service.CountryService;
import com.fairanb.service.LanguageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class CountryController extends BaseController {

	static Logger log = LoggerFactory.getLogger(CountryController.class.getName());

	@Autowired
	private CountryService countryService;

	@Autowired
	private LanguageService languageService;

	private ModelMapper mapper = new ModelMapper();

	private static Type LIST_TYPE = new TypeToken<List<CountryResponse>>() {
	}.getType();

	@ApiOperation(value = "Initialize Country")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = ResponseEntity.class),
			@ApiResponse(code = 409, message = "CONFLICT, Country data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The Country existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.COUNTRYES + "/init/reload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> loadCountries() throws Exception {
		Rest rest = new Rest(HttpStatus.OK);
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet("http://countryapi.gear.host/v1/Country/getCountries");
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			try {
				HttpEntity entity = response1.getEntity();
				String json = EntityUtils.toString(entity);
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Object> m = mapper.readValue(json, Map.class);
				CountryThirdServiceResponse res = new CountryThirdServiceResponse();
				res.setIsSuccess((Boolean) m.get("IsSuccess"));
				res.setUserMessage((String) m.get("UserMessage"));
				res.setTechnicalMessage((String) m.get("TechnicalMessage"));
				res.setTotalCount((Integer) m.get("TotalCount"));
				res.setResponse(convert((List<Map>) m.get("Response")));
				countryService.deleteAll();
				countryService.save(res.getResponse());
				List<CountryRequest> returnLst = new ArrayList<CountryRequest>();
				for (Country c : res.getResponse()) {
                    CountryRequest cr = this.mapper.map(c, CountryRequest.class);
                    returnLst.add(cr);
                }
				rest.setData(returnLst);
			} finally {
				response1.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rest.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			rest.setMessage("There is an unexpected error during initialize country. Please see log for more details.");
			return new ResponseEntity<>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Create new Country")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Country created successfully", response = CountryResponse.class),
			@ApiResponse(code = 409, message = "CONFLICT, Country data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The Country existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.COUNTRYES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> createCountry(@RequestBody CountryRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		ModelMapper modelMapper=new ModelMapper() ;
        ObjectMapper objectMapper = new ObjectMapper();
		super.validateRequest(request);

		//check require field
		if (StringUtils.isBlank(request.getName())) {
            Rest err = new Rest(HttpStatus.BAD_REQUEST, "Country name is required");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        if (StringUtils.isBlank(request.getIsoCode2())) {
            Rest err = new Rest(HttpStatus.BAD_REQUEST, "Country isoCode2 is required");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        if (StringUtils.isBlank(request.getIsoCode3())) {
            Rest err = new Rest(HttpStatus.BAD_REQUEST, "Country isoCode3 is required");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }

		String isoCode3 = request.getIsoCode3();
        String isoCode2 = request.getIsoCode2();
		Country country = modelMapper.map(request, Country.class);

		Country existedIsoCode3 = countryService.findByIsoCode3(isoCode3);
        Country existedIsoCode2 = countryService.findByIsoCode2(isoCode2);
        Country existedName = countryService.findByCountryName(request.getName());
		Rest rest = new Rest();
		if (existedIsoCode2 == null && existedIsoCode2 == null && existedName == null) {
		    try {
                log.info("Create country code: [" + country + "]");
                if (request.getDescriptions() != null) {
                    List<CountryDescription> countryDescriptionList = new ArrayList<CountryDescription>();
                    for (CountryDescriptionResponse cdRequest : request.getDescriptions()) {
                        CountryDescription cd = new CountryDescription();
                        cd.setCountry(country);
                        cd.setName(cdRequest.getName());
                        cd.setNativeName(cdRequest.getNativeName());
                        Language lang = languageService.findByLanguageName(cdRequest.getLanguageName());
                        cd.setLanguage(lang);
                        cd.setDescription(cdRequest.getDescription());
                        cd.setCreatedDate(new Date());
                        cd.setModifiedDate(new Date());
                        countryDescriptionList.add(cd);
                    }
                    country.setDescriptions(countryDescriptionList);
                }
                country.setActive(Boolean.TRUE);
                country.setStatus("NEW");
                country = countryService.save(country);
                rest.setStatus(HttpStatus.CREATED.value());
                rest.setMessage("Country is created successfully");
                CountryResponse countryResponse = modelMapper.map(country, CountryResponse.class);
                rest.setData(countryResponse);
            } catch (Exception ex) {
                Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when creating country. Please see log for more details.");
                String json = objectMapper.writeValueAsString(err);
                return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
            }
		} else {
            CountryResponse countryResponse = null;
		    if (existedIsoCode2 != null) {
                countryResponse = modelMapper.map(existedIsoCode2, CountryResponse.class);
            }
			if (existedIsoCode3 != null) {
                countryResponse = modelMapper.map(existedIsoCode3, CountryResponse.class);
            }
            if (existedIsoCode3 != null) {
                countryResponse = modelMapper.map(existedName, CountryResponse.class);
            }
            rest.setStatus(HttpStatus.CONFLICT.value());
            rest.setMessage("Country with isoCode2/isoCode3 is already existed");
			rest.setData(countryResponse);
		}

        String json = objectMapper.writeValueAsString(rest);
		return new ResponseEntity<String>(json, HttpStatus.valueOf(rest.getStatus()));
	}

	@ApiOperation(value = "Update Country")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of country update", response = CountryResponse.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.COUNTRYES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateCountry(
			@ApiParam(value = "Object json country") @RequestBody CountryRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {

        log.info("Update country:" + request);
		Long id = request.getId();

		//check valid id
		if (id == null || id.longValue() == 0) {
            return responseEntity(new Rest(HttpStatus.NOT_FOUND, "country Id is not found "));
        }

        //check valid country
		Country existed = countryService.findById(id);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "country Id [" + id + "] invalid "));
		}

		//check exist iso code 3 & iso code 2
        Country existedIsoCode3 = countryService.findByIsoCode3(request.getIsoCode3());
        Country existedIsoCode2 = countryService.findByIsoCode2(request.getIsoCode2());
        if ((existedIsoCode2 != null && existedIsoCode2.getId() != request.getId()) ||
                (existedIsoCode3 != null && existedIsoCode2.getId() != request.getId())) {
            return responseEntity(new Rest(HttpStatus.CONFLICT, "Your update isoCode2/isoCode3 is already existed "));
        }

        Rest rest = new Rest(HttpStatus.OK);
		try {
            Country country = this.mapper.map(request, Country.class);
            existed = countryService.save(country);
            CountryResponse countryResponse = this.mapper.map(existed, CountryResponse.class);
            List<CountryDescriptionResponse> countryDescriptionResponseList = new ArrayList<CountryDescriptionResponse>();
            for (CountryDescription cd : country.getDescriptions()) {
                CountryDescriptionResponse countryDescriptionResponse = new CountryDescriptionResponse();
                countryDescriptionResponse.setCountryName(cd.getCountry().getName());
                countryDescriptionResponse.setLanguageName(cd.getLanguage().getName());
                countryDescriptionResponse.setName(cd.getName());
                countryDescriptionResponse.setNativeName(cd.getNativeName());
                countryDescriptionResponse.setDescription(cd.getDescription());
                countryDescriptionResponseList.add(countryDescriptionResponse);
            }
            countryResponse.setDescriptions(countryDescriptionResponseList);

            rest.setData(countryResponse);
            log.info("Patch Country with existed = " + existed);
        } catch (Exception ex) {
            return responseEntity(new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when creating country. Please see log for more details."));
        }
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Country by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return country response ", response = CountryResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.COUNTRYES + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> getCountry(
			@ApiParam(value = "Get country by Id") @PathVariable Long id) throws Exception {
		log.info("Search country");
		ObjectMapper objectMapper = new ObjectMapper();

		Rest rest = new Rest();
		try {
            Country country = countryService.findById(id);
            log.info("Get country by id [" + id + "]");
            if (country != null) {
                rest.setHttpStatus(HttpStatus.OK);
                CountryResponse countryResponse = this.mapper.map(country, CountryResponse.class);
                List<CountryDescriptionResponse> countryDescriptionResponseList = new ArrayList<CountryDescriptionResponse>();
                for (CountryDescription cd : country.getDescriptions()) {
                    CountryDescriptionResponse countryDescriptionResponse = new CountryDescriptionResponse();
                    countryDescriptionResponse.setCountryName(cd.getCountry().getName());
                    countryDescriptionResponse.setLanguageName(cd.getLanguage().getName());
                    countryDescriptionResponse.setName(cd.getName());
                    countryDescriptionResponse.setNativeName(cd.getNativeName());
                    countryDescriptionResponse.setDescription(cd.getDescription());
                    countryDescriptionResponseList.add(countryDescriptionResponse);
                }
                countryResponse.setDescriptions(countryDescriptionResponseList);

                rest.setData(countryResponse);
            } else {
                rest.setHttpStatus(HttpStatus.NOT_FOUND);
                rest.setMessage("Can not get Country id [" + id + "]");
            }
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when creating country. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        String json = objectMapper.writeValueAsString(rest);
		return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
	}
	
	@ApiOperation(value = "List Country by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = CountryResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.COUNTRYES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> listCountry(
			@RequestParam(required = false) String code, 
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search country");
        ObjectMapper objectMapper = new ObjectMapper();

		Rest rest = new Rest();

		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		try {
            Page<Country> result = countryService.findAll(paging);
            rest.setHttpStatus(HttpStatus.OK);
            log.info("Search country total elements [" + result.getTotalElements() + "]");
            List<Country> contentList = result.getContent();
            List<CountryResponse> responseList = new ArrayList<CountryResponse>();
			for (Country country : contentList) {
                CountryResponse cr = this.mapper.map(country, CountryResponse.class);
                List<CountryDescriptionResponse> countryDescriptionResponseList = new ArrayList<>();
                cr.setDescriptions(countryDescriptionResponseList);
                for (CountryDescription cd : country.getDescriptions()) {
                    CountryDescriptionResponse countryDescriptionResponse = new CountryDescriptionResponse();
                    countryDescriptionResponse.setCountryName(cd.getCountry().getName());
                    countryDescriptionResponse.setLanguageName(cd.getLanguage().getName());
                    countryDescriptionResponse.setName(cd.getName());
                    countryDescriptionResponse.setNativeName(cd.getNativeName());
                    countryDescriptionResponse.setDescription(cd.getDescription());
                    countryDescriptionResponseList.add(countryDescriptionResponse);
                }
                responseList.add(cr);
            }

            rest.setDataList(responseList, result);
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when creating country. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        String json = objectMapper.writeValueAsString(rest);
        return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
	}

	@ApiOperation(value = "Delete Country")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted country", response = Rest.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and country name. Go signin.", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.COUNTRYES + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteCountry(@PathVariable("id") Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		

		log.info("Delete  country [" + id + "]");
		Country country = countryService.findById(id);
		if (country != null) {
			log.info("Inactive  country [" + id + "]");

			country.setActive(Boolean.FALSE);
			countryService.save(country);
			return responseEntity(HttpStatus.OK, "Deleted  country [" + id + "] sucessfully");
		} else {
			return responseEntity(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Option method", tags = JConstants.CORS)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Successful", response = Rest.class, responseHeaders = {
			@ResponseHeader(name = "Access-Control-Allow-Origin", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Methods", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Headers", response = String.class) }) })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.COUNTRYES, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> cors() throws IOException {
		return responseEntity(HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Option method", tags = JConstants.CORS)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Successful", response = Rest.class, responseHeaders = {
			@ResponseHeader(name = "Access-Control-Allow-Origin", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Methods", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Headers", response = String.class) }) })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.COUNTRYES + URI.ID, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> id() throws IOException {
		return responseEntity(HttpStatus.OK);
	}

    private List<Country> convert (List<Map> obj) {
        List<Country> returnLst = new ArrayList<Country>();
		Language lang = languageService.findByLanguageName("en");
        for (Map o : obj) {
            Country c = new Country();
            c.setIsoCode2((String)o.get("Alpha2Code"));
            c.setIsoCode3((String)o.get("Alpha3Code"));
            c.setName((String)o.get("Name"));
            c.setAddress((String)o.get("SubRegion"));
            c.setStatus("NEW");
            c.setActive(true);
            c.setCreatedDate(new Date());
            c.setModifiedDate(new Date());
			CountryDescription description = new CountryDescription();
			description.setCountry(c);
			description.setLanguage(lang);
			description.setName((String)o.get("Name"));
            description.setNativeName((String)o.get("NativeName"));
			description.setDescription((String)o.get("Name") + " has total area of " + (Integer)o.get("Area") +
					"km2. Its currency name is " + (String)o.get("CurrencyName"));
            description.setCreatedDate(new Date());
            description.setModifiedDate(new Date());
			List<CountryDescription> descriptions = new ArrayList<CountryDescription>();
			descriptions.add(description);
			c.setDescriptions(descriptions);
            returnLst.add(c);
        }
        return returnLst;
    }
}
