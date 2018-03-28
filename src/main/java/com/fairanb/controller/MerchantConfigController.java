package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.JConstants.Status;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.MerchantConfig;
import com.fairanb.model.Paging;
import com.fairanb.model.request.MerchantConfigRequest;
import com.fairanb.model.response.MerchantConfigResponse;
import com.fairanb.model.response.MerchantResponse;
import com.fairanb.service.MerchantConfigService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MerchantConfigController extends BaseController {

	static Logger log = LoggerFactory.getLogger(MerchantConfigController.class.getName());

	@Autowired
	public MerchantConfigService service;
	ModelMapper modelMapper=new ModelMapper() ;
	@ApiOperation(value = "Create new MerchantConfig")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, MerchantConfig created successfully", response = MerchantConfig.class),
			@ApiResponse(code = 409, message = "CONFLICT, MerchantConfig data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The merchant existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.MERCHANTS_CONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createMerchantConfig(@RequestBody MerchantConfigRequest request,
											   @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		MerchantConfigResponse response = null;
		super.validateRequest(request);

		//Merchant merchant = request.copyBean(request);
		MerchantConfig merchantConfig = modelMapper.map(request, MerchantConfig.class);
		Rest rest = null;
		log.info("Create merchantConfig: [" + merchantConfig + "]");
		merchantConfig.setStatus(Status.ACTIVE.name());
		merchantConfig = service.save(merchantConfig);
		//Merchant merchant = request.copyBean(request);
		response= modelMapper.map(merchantConfig, MerchantConfigResponse.class);
			rest = new Rest(HttpStatus.CREATED);
			rest.setData(response);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Update MerchantConfig")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of merchant update", response = MerchantResponse.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.MERCHANTS_CONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateMerchantConfig(
			@ApiParam(value = "Object json merchant") @RequestBody MerchantConfigRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		MerchantConfigResponse response = null;
		Long id = request.getId();
		log.info("Update MerchantConfig:" + request);
		MerchantConfig merchantConfig = service.findOne(id);
		if (merchantConfig == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Merchant Id [" + id + "] invalid "));
		}
		MerchantConfig updateMerchantConfig =modelMapper.map(request, MerchantConfig.class);

		merchantConfig = service.save(updateMerchantConfig);
		response= modelMapper.map(merchantConfig, MerchantConfigResponse.class);
		Rest rest = new Rest(HttpStatus.OK);
		rest.setData(response);
		log.info("Patch MerchantConfig with existed = " + merchantConfig);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Get MerchantConfig by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return merchant response ", response = MerchantResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.MERCHANTS_CONFIG + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getMerchantConfig(
			@ApiParam(value = "Get MerchantConfig by Id") @PathVariable Long id) throws Exception {
		log.info("Search MerchantConfig");
		MerchantConfigResponse response = null;
		Rest rest = new Rest();
		MerchantConfig merchantConfig = service.findOne(id);
		log.info("Get merchant by id [" + id + "]");
		if (merchantConfig != null) {
			rest.setHttpStatus(HttpStatus.OK);
			response= modelMapper.map(merchantConfig, MerchantConfigResponse.class);
			rest.setData(response);
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get MerchantConfig id [" + id + "]");
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Get MerchantConfig by Merchant Id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return merchant response ", response = MerchantResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.MERCHANTS_CONFIG + URI.MERCHANTS + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getMerchantConfigByMerchantId(
			@ApiParam(value = "Get MerchantConfig by Id") @PathVariable Long id) throws Exception {
		log.info("Search MerchantConfig");

		Rest rest = new Rest();
		List<MerchantConfig> merchantConfigs = service.findByMerchantId(id);
		log.info("Get merchant by id [" + id + "]");
		if (merchantConfigs != null) {
			rest.setHttpStatus(HttpStatus.OK);
			rest.setData(merchantConfigs);
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get MerchantConfig id [" + id + "]");
		}

		return responseEntity(rest);
	}



	@ApiOperation(value = "List MerchantConfig by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return list by the conditions ", response = MerchantConfigResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.MERCHANTS_CONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listMerchantConfig(
			@RequestParam(required = false) String code,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search merchantConfig");

		Rest rest = new Rest();

		//TODO ExampleMatcher with columns name
		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<MerchantConfig> result = service.findAll(paging);
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search merchantConfig total elements [" + result.getTotalElements() + "]");
		List<MerchantConfig> contentList = result.getContent();
		List<MerchantConfigResponse> responseList = new ArrayList<MerchantConfigResponse>();

		contentList.forEach(merchantConfig -> {
			responseList.add(modelMapper.map(merchantConfig, MerchantConfigResponse.class));
		});

		rest.setDataList(responseList, result);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Delete MerchantConfig")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted MerchantConfig", response = Rest.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Merchant name. Go signin.", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.MERCHANTS_CONFIG, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteMerchantConfig(@RequestParam(required = true) Long id,
											   @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {

		log.info("Delete  Merchant [" + id + "]");
		MerchantConfig merchantConfig=service.findOne(id);
		if (merchantConfig!=null) {
			log.info("Delete  MerchantConfig [" + id + "]");
			service.delete(id);
			return responseEntity(HttpStatus.OK, "Deleted  MerchantConfig [" + id + "] sucessfully");
		} else {
			return responseEntity(HttpStatus.NOT_FOUND);
		}
	}


}
