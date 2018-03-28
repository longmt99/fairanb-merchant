package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.Language;
import com.fairanb.model.Paging;
import com.fairanb.model.request.LanguageRequest;
import com.fairanb.model.response.LanguageResponse;
import com.fairanb.service.LanguageService;
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
import java.util.ArrayList;
import java.util.List;

@RestController
public class LanguageController extends BaseController {

	static Logger log = LoggerFactory.getLogger(LanguageController.class.getName());

	@Autowired
	public LanguageService languageService;
	private static Type LIST_TYPE = new TypeToken<List<LanguageResponse>>() {
	}.getType();

	@ApiOperation(value = "Create new Language")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Language created successfully", response = LanguageResponse.class),
			@ApiResponse(code = 409, message = "CONFLICT, Language data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The Language existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.LANGUAGES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createLanguage(@RequestBody LanguageRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		ModelMapper modelMapper=new ModelMapper() ;
		super.validateRequest(request);

		String name = request.getName();
		Language language = modelMapper.map(request, Language.class);

		Language existed = languageService.findByLanguageName(name);
		Rest rest = null;
		if (existed == null) {
			log.info("Create Language code: [" + language + "]");
			language.setActive(Boolean.TRUE);
			language = languageService.save(language);
			rest = new Rest(HttpStatus.CREATED);
			LanguageResponse languageResponse = modelMapper.map(language, LanguageResponse.class);
			rest.setData(languageResponse);
		} else {

			LanguageResponse languageResponse = modelMapper.map(existed, LanguageResponse.class);

			rest.setData(languageResponse);
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Update Language")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of Language update", response = LanguageResponse.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.LANGUAGES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateLanguage(
			@ApiParam(value = "Object json Language") @RequestBody LanguageRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {

		Long id = request.getId();
		log.info("Update Language:" + request);
		Language existed = languageService.findById(id);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Language Id [" + id + "] invalid "));
		}
		Language language = modelMapper.map(request, Language.class);
		existed = languageService.save(language);
		Rest rest = new Rest(HttpStatus.OK);
		LanguageResponse languageResponse = modelMapper.map(existed, LanguageResponse.class);
		rest.setData(languageResponse);
		log.info("Patch Language with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Language by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return Language response ", response = LanguageResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.LANGUAGES + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getLanguage(
			@ApiParam(value = "Get Language by Id") @PathVariable Long id) throws Exception {
		log.info("Search Language");

		Rest rest = new Rest();
		Language language = languageService.findById(id);
		log.info("Get Language by id [" + id + "]");
		if (language != null) {
			rest.setHttpStatus(HttpStatus.OK);
			LanguageResponse languageResponse = modelMapper.map(language, LanguageResponse.class);
			rest.setData(languageResponse);
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get Language id [" + id + "]");
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "List Language by conditions")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Returnn list by the conditions ", response = LanguageResponse.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.LANGUAGES, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listLanguage(
			@RequestParam(required = false) String code,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search Language");

		Rest rest = new Rest();

		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
		Page<Language> result = languageService.findAll(paging);
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search language total elements [" + result.getTotalElements() + "]");
		List<Language> contentList = result.getContent();
		List<LanguageResponse> responseList = new ArrayList<LanguageResponse>();
		result.forEach(language -> {
			responseList.add(modelMapper.map(language, LanguageResponse.class));
		});

		rest.setDataList(responseList, result);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Delete Language")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted Language", response = Rest.class),
			@ApiResponse(code = 201, message = "CREATED, Already registered also verified with Phone Number and Language name. Go signin.", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.LANGUAGES, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteLanguage(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {


		log.info("Delete  Language [" + id + "]");
		Language language = languageService.findById(id);
		if (language != null) {
			log.info("Inactive  language [" + id + "]");

			language.setActive(Boolean.FALSE);
			languageService.save(language);
			return responseEntity(HttpStatus.OK, "Deleted  language [" + id + "] sucessfully");
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
	@RequestMapping(value = URI.LANGUAGES, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> corsLanguage() throws IOException {
		return responseEntity(HttpStatus.OK);
	}


	@ApiOperation(value = "Option method", tags = JConstants.CORS)
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Successful", response = Rest.class, responseHeaders = {
			@ResponseHeader(name = "Access-Control-Allow-Origin", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Methods", response = String.class),
			@ResponseHeader(name = "Access-Control-Allow-Headers", response = String.class) }) })
	@CrossOrigin(origins = "*")
	@RequestMapping(value = URI.LANGUAGES + URI.ID, method = RequestMethod.OPTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> idLanguage() throws IOException {
		return responseEntity(HttpStatus.OK);
	}
}
