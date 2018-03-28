package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.GeoZone;
import com.fairanb.model.Paging;
import com.fairanb.model.request.GeoZoneRequest;
import com.fairanb.model.response.GeoZoneResponse;
import com.fairanb.model.response.ZoneResponse;
import com.fairanb.service.GeoZoneService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.List;

@RestController
public class GeoZoneController extends BaseController {

	static Logger log = LoggerFactory.getLogger(GeoZoneController.class.getName());

	@Autowired
	public GeoZoneService service;
	private static Type LIST_TYPE = new TypeToken<List<GeoZoneResponse>>() {
	}.getType();

	@ApiOperation(value = "Create new geo zone")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, geo zone created successfully", response = ZoneResponse.class),
			@ApiResponse(code = 409, message = "CONFLICT, geo zone data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The geo zone existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.GEO_ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createGeoZone(@RequestBody GeoZoneRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		ModelMapper modelMapper=new ModelMapper() ;
		super.validateRequest(request);

		String geoZoneName = request.getName();
		GeoZone geoZone = modelMapper.map(request, GeoZone.class);

		GeoZone existed = service.findByName(geoZoneName);
		Rest rest = null;
		if (existed == null) {
			log.info("Create zone code: [" + geoZone + "]");
			geoZone.setActive(Boolean.TRUE);
			geoZone = service.save(geoZone);
			rest = new Rest(HttpStatus.CREATED);
			GeoZoneResponse geoZoneResponse = modelMapper.map(geoZone, GeoZoneResponse.class);
			rest.setData(geoZoneResponse);
		} else {
			GeoZoneResponse geoZoneResponse = modelMapper.map(existed, GeoZoneResponse.class);
			rest = new Rest(HttpStatus.CONFLICT);
			rest.setData(geoZoneResponse);
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Update Zone")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of geo zone update", response = ZoneResponse.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.GEO_ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateGeoZone(
			@ApiParam(value = "Object json country") @RequestBody GeoZoneRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		
		Long id = request.getId();
		log.info("Update geoZone:" + request);
		GeoZone existed = service.findById(id);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "zone Id [" + id + "] invalid "));
		}
		GeoZone geoZone = modelMapper.map(request, GeoZone.class);
		existed = service.save(geoZone);
		Rest rest = new Rest(HttpStatus.OK);
		ZoneResponse zoneResponse = modelMapper.map(existed, ZoneResponse.class);
		rest.setData(zoneResponse);
		log.info("Patch Zone with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Geo Zone by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return geo zone response ", response = GeoZoneResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.GEO_ZONES + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getGeoZone(
			@ApiParam(value = "Get zone by Id") @PathVariable Long id) throws Exception {
		log.info("Search Geo Zone");

		Rest rest = new Rest();
		GeoZone geoZone = service.findById(id);
		log.info("Get zone by id [" + id + "]");
		if (geoZone != null) {
			rest.setHttpStatus(HttpStatus.OK);
			GeoZoneResponse geoZoneResponse = modelMapper.map(geoZone, GeoZoneResponse.class);
			rest.setData(geoZoneResponse);
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get zone id [" + id + "]");
		}

		return responseEntity(rest);
	}
	
	@ApiOperation(value = "List geo zone by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = GeoZoneResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.GEO_ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listGeoZone(
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search zone");

		Rest rest = new Rest();

		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<GeoZone> result = service.findAll(paging);
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search zone total elements [" + result.getTotalElements() + "]");
		List<GeoZone> contentList = result.getContent();
		List<GeoZoneResponse> responseList = new ArrayList<GeoZoneResponse>();
		result.forEach(geoZone -> {
			responseList.add(modelMapper.map(geoZone, GeoZoneResponse.class));
		});

		rest.setDataList(responseList, result);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Delete zone")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, deleted geo zone", response = Rest.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and geo zone name. Go signin.", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.GEO_ZONES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteGeoZone(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		

		log.info("Delete  country [" + id + "]");
		GeoZone geoZone = service.findById(id);
		if (geoZone != null) {
			log.info("Inactive  geo zone [" + id + "]");

			geoZone.setActive(Boolean.FALSE);
			service.save(geoZone);
			return responseEntity(HttpStatus.OK, "Deleted  geo zone [" + id + "] sucessfully");
		} else {
			return responseEntity(HttpStatus.NOT_FOUND);
		}
	}

}
