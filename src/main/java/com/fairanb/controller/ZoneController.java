package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.Country;
import com.fairanb.model.Paging;
import com.fairanb.model.Zone;
import com.fairanb.model.request.ZoneRequest;
import com.fairanb.model.response.ZoneResponse;
import com.fairanb.model.response.ZoneThirdServiceResponse;
import com.fairanb.service.CountryService;
import com.fairanb.service.ZoneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ZoneController extends BaseController {

	static Logger log = LoggerFactory.getLogger(ZoneController.class.getName());

	@Autowired
	public ZoneService zoneService;
	@Autowired
	public CountryService countryService;
	private static Type LIST_TYPE = new TypeToken<List<ZoneResponse>>() {
	}.getType();
	private ModelMapper mapper = new ModelMapper();
	@ApiOperation(value = "Initialize Country")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = ResponseEntity.class),
			@ApiResponse(code = 409, message = "CONFLICT, Country data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The Country existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.ZONES + "/update /countryId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> loadZones() throws Exception {
		Rest rest = new Rest(HttpStatus.OK);

			try {
		;
				List<Zone> zones = zoneService.loadAllZones();
				if(zones.isEmpty()||zones.size()==0)
				{
					return new ResponseEntity<>(rest, HttpStatus.OK);
				}
				for (Zone z : zones) {
					Country	country=countryService.findByIsoCode2(z.getCode());

						if(country!=null)
						{
							z.setCountryId(country.getId());
						}
						zoneService.save(z);
				}
				List<Zone> zoneList = zoneService.loadAllZones();
				rest.setData(zoneList);

		} catch (Exception ex) {
			ex.printStackTrace();
			rest.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			rest.setMessage("There is an unexpected error during initialize country. Please see log for more details.");
			return new ResponseEntity<>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}





	@ApiOperation(value = "Create new zone")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, zone created successfully", response = ZoneResponse.class),
			@ApiResponse(code = 409, message = "CONFLICT, zone data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The zone existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createZone(@RequestBody ZoneRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		ModelMapper modelMapper=new ModelMapper() ;
		super.validateRequest(request);

		String name = request.getName();
		Zone zone = modelMapper.map(request, Zone.class);

		Zone existed = zoneService.findByName(name);
		Rest rest = null;
		if (existed == null) {
			log.info("Create zone code: [" + zone + "]");
			zone.setActive(Boolean.TRUE);
			zone = zoneService.save(zone);
			rest = new Rest(HttpStatus.CREATED);
			ZoneResponse zoneResponse = modelMapper.map(zone, ZoneResponse.class);
			rest.setData(zoneResponse);
		} else {

			ZoneResponse zoneResponse = modelMapper.map(existed, ZoneResponse.class);
			rest = new Rest(HttpStatus.CONFLICT);
			rest.setData(zoneResponse);
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Update Zone")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of zone update", response = ZoneResponse.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateZone(
			@ApiParam(value = "Object json country") @RequestBody ZoneRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		
		Long id = request.getId();
		log.info("Update zone:" + request);
		Zone existed = zoneService.findById(id);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "zone Id [" + id + "] invalid "));
		}
		Zone zone = modelMapper.map(request, Zone.class);
		existed = zoneService.save(zone);
		Rest rest = new Rest(HttpStatus.OK);
		ZoneResponse zoneResponse = modelMapper.map(existed, ZoneResponse.class);
		rest.setData(zoneResponse);
		log.info("Patch Zone with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Zone by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return Zone response ", response = ZoneResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ZONES+ URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getZone(
			@ApiParam(value = "Get zone by Id") @PathVariable Long id) throws Exception {
		log.info("Search Zone");

		Rest rest = new Rest();
		Zone zone = zoneService.findById(id);
		log.info("Get zone by id [" + id + "]");
		if (zone != null) {
			rest.setHttpStatus(HttpStatus.OK);
			ZoneResponse zoneResponse = modelMapper.map(zone, ZoneResponse.class);
			rest.setData(zoneResponse);
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get zone id [" + id + "]");
		}

		return responseEntity(rest);
	}
	
	@ApiOperation(value = "List Zone by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = ZoneResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listZone(
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search zone");

		Rest rest = new Rest();

		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<Zone> result = zoneService.findAll(paging);
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search zone total elements [" + result.getTotalElements() + "]");
		List<Zone> contentList = result.getContent();
		List<ZoneResponse> responseList = new ArrayList<ZoneResponse>();
		result.forEach(zone -> {
			responseList.add(modelMapper.map(zone, ZoneResponse.class));
		});

		rest.setDataList(responseList, result);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Delete zone")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted Zone", response = Rest.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and zone name. Go signin.", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteZone(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		

		log.info("Delete  country [" + id + "]");
		Zone zone = zoneService.findById(id);
		if (zone != null) {
			log.info("Inactive  zone [" + id + "]");

			zone.setActive(Boolean.FALSE);
			zoneService.delete(id);
			return responseEntity(HttpStatus.OK, "Deleted  zone [" + id + "] sucessfully");
		} else {
			return responseEntity(HttpStatus.NOT_FOUND);
		}
	}

}
