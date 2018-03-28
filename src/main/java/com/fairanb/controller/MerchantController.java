package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.JConstants.Status;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.common.Utils;
import com.fairanb.model.Language;
import com.fairanb.model.Merchant;
import com.fairanb.model.Paging;
import com.fairanb.model.request.MerchantRequest;
import com.fairanb.model.response.LanguageResponse;
import com.fairanb.model.response.MerchantResponse;
import com.fairanb.service.LanguageService;
import com.fairanb.service.MerchantLanguageService;
import com.fairanb.service.MerchantService;
import io.swagger.annotations.*;
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
import java.util.List;

@RestController
public class MerchantController extends BaseController {

	static Logger log = LoggerFactory.getLogger(MerchantController.class.getName());
	private static Type LIST_TYPE = new TypeToken<List<MerchantResponse>>() {
	}.getType();
	@Autowired
	public MerchantService merchantService;
	@Autowired
	MerchantLanguageService merchantLanguageService;
	@Autowired
	LanguageService languageService;
	ModelMapper modelMapper=new ModelMapper() ;
	@ApiOperation(value = "Create new Merchant")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Merchant created successfully", response = MerchantResponse.class),
			@ApiResponse(code = 409, message = "CONFLICT, Merchant data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The merchant existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.MERCHANTS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createMerchant(@RequestBody MerchantRequest request,
											   @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		Rest rest = null;
		super.validateRequest(request);
		String message =null;
				String email = request.getEmail();

		//Merchant merchant = request.copyBean(request);
		Merchant merchant = modelMapper.map(request, Merchant.class);
		if(merchant.getEmail()==null||merchant.getEmail()=="") {
			rest = new Rest(HttpStatus.BAD_REQUEST);
			message = "Email can't Empty";
			rest.setData(message);
			return responseEntity(rest);
		}
		if(merchant.getPhone()==null||merchant.getPhone()=="") {
			rest = new Rest(HttpStatus.BAD_REQUEST);
			message = "Phone Number can't Empty";
			rest.setData(message);
			return responseEntity(rest);
		}
		if(merchant.getLanguages()==null) {
			rest = new Rest(HttpStatus.BAD_REQUEST);
			message = "Language can't Empty";
			rest.setData(message);
			return responseEntity(rest);
		}

		List<Language> languages =merchant.getLanguages();
		for(Language lan:languages)
		{
			Language getLan=	languageService.findById(lan.getId());
			if(getLan==null)
			{
				rest = new Rest(HttpStatus.BAD_REQUEST);
				message = "Invalid Language";
				rest.setData(message);
				return responseEntity(rest);
			}
		}
		Merchant existed = merchantService.findByEmail(email);

		MerchantResponse response = null;

		if (existed == null) {
			log.info("Create merchant code: [" + email + "]");
			merchant.setStatus(Status.ACTIVE.name());
			Merchant createdMerchant = merchantService.save(merchant);
			if (createdMerchant!=null){
				List<LanguageResponse>  lanList=merchantLanguageService.findByMerchantId(createdMerchant.getId());
				rest = new Rest(HttpStatus.CREATED);
				response = merchant.getResponse();
				response.setLanguages(lanList);
		}else {
				rest = new Rest(HttpStatus.BAD_REQUEST);
			}

			rest.setData(response);
		} else {
			String currentStatus = existed.getStatus();
			if (currentStatus.equals(Status.INACTIVE.name())) {
				status = HttpStatus.LOCKED;
				rest = new Rest(status);
			} else {
				status = HttpStatus.CONFLICT;
				 message = "At least one merchant data existed [" + email + "]";
				rest = new Rest(status, message);
			}
			rest.setData(existed.getResponse());
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Update merchant")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of merchant update", response = MerchantResponse.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.MERCHANTS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateMerchant(
			@ApiParam(value = "Object json merchant") @RequestBody MerchantRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {

		Long id = request.getId();
		log.info("Update Merchant:" + request);
		Merchant existed = merchantService.findOne(id);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Merchant Id [" + id + "] invalid "));
		}
		Merchant merchant = request.copyBean(request);
		List<Language> languages = merchant.getLanguages();
		Utils.copyNonNullProperties(merchant, existed);
		existed.setLanguages(languages);
		existed = merchantService.save(existed);
		Rest rest = new Rest(HttpStatus.OK);
		rest.setData(existed.getResponse());
		log.info("Patch Merchant with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Merchant by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return merchant response ", response = MerchantResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.MERCHANTS + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getMerchant(
			@ApiParam(value = "Get merchant by Id") @PathVariable Long id) throws Exception {
		log.info("Search Merchant");

		Rest rest = new Rest();
		Merchant merchant = merchantService.findOne(id);
		log.info("Get merchant by id [" + id + "]");
		if (merchant != null) {
			rest.setHttpStatus(HttpStatus.OK);
			rest.setData(merchant.getResponse());
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get Merchant id [" + id + "]");
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "List merchant by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = MerchantResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.MERCHANTS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listMerchant(
			@RequestParam(required = false) String code,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search Merchant");

		Rest rest = new Rest();

		//TODO ExampleMatcher with columns name
		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<Merchant> result = merchantService.findAll(paging);
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search merchant total elements [" + result.getTotalElements() + "]");
		List<Merchant> contentList = result.getContent();
		List<MerchantResponse> responseList= modelMapper.map(contentList, LIST_TYPE);

		rest.setDataList(responseList, result);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Delete merchant")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted merchant", response = Rest.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Merchant name. Go signin.", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.MERCHANTS, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteMerchant(@RequestParam(required = true) Long id,
											   @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {


		log.info("Delete  Merchant [" + id + "]");
		Merchant merchant = merchantService.findOne(id);
		if (merchant != null) {
			log.info("Delete  Merchant [" + id + "]");
			merchantService.delete(id);
			return responseEntity(HttpStatus.OK, "Deleted  Merchant [" + id + "] sucessfully");
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
	@RequestMapping(value = URI.MERCHANTS, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> cors() throws IOException {
		return responseEntity(HttpStatus.OK);
	}


	@ApiOperation(value = "Option method", tags = JConstants.CORS)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Successful", response = Rest.class, responseHeaders = {
			@ResponseHeader(name = "Access-Control-Allow-Origin", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Methods", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Headers", response = String.class) }) })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.MERCHANTS + URI.ID, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> id() throws IOException {
		return responseEntity(HttpStatus.OK);
	}
}
