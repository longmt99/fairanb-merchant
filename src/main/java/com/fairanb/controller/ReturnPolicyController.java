package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.ReturnPolicy;
import com.fairanb.model.request.ReturnPolicyRequest;
import com.fairanb.model.response.ReturnPolicyResponse;
import com.fairanb.model.response.ReturnPolicyResponse.ValueDto;
import com.fairanb.service.ReturnPolicyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ReturnPolicyController extends BaseController {
	static Logger log = LoggerFactory.getLogger(ReturnPolicyController.class);

	@Autowired
	private ReturnPolicyService returnPolicyService;

	ModelMapper modelMapper;

	public ReturnPolicyController() {
		modelMapper = new ModelMapper();

		ObjectMapper mapper = new ObjectMapper();

		Converter<String, ValueDto> policyValueConverter = context -> {
			try {
				return mapper.readValue(context.getSource(), ValueDto.class);
			} catch (IOException e) {
				log.error("Can't map ReturnPolicyResponse.ValueDto", e);
				return null;
			}
		};

		PropertyMap<ReturnPolicy, ReturnPolicyResponse> policyResponsePropertyMap = new PropertyMap<ReturnPolicy, ReturnPolicyResponse>() {
			@Override
			protected void configure() {
				using(policyValueConverter).map(source.getValue(), destination.getValue());
			}
		};

		modelMapper.addMappings(policyResponsePropertyMap);
	}

	@ApiOperation("Create return policy")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Return policy created successfully"),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@PostMapping(value = URI.RETURN_POLICY, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> createReturnPolicy(@RequestBody ReturnPolicyRequest request,
												   @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		try {
			validateRequest(request);
		} catch (IllegalArgumentException e) {
			return responseEntity(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
		}
		Rest rest = new Rest();

		try {
			ReturnPolicy returnPolicy = returnPolicyService.createReturnPolicy(request);
			ReturnPolicyResponse response = modelMapper.map(returnPolicy, ReturnPolicyResponse.class);

			rest.setData(response);
		} catch (JsonProcessingException e) {
			return responseEntity(e);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
		}

		rest.setHttpStatus(HttpStatus.CREATED);
		return responseEntity(rest);
	}

	@ApiOperation("Get merchant return policies")
	@ApiResponses({
			@ApiResponse(code = 200, response = ReturnPolicyResponse.class, message = "OK, Return merchant return policies"),
			@ApiResponse(code = 404, response = Rest.class, message = "Return policies for provided merchant id not found"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@GetMapping(value = URI.RETURN_POLICY, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getMerchantReturnPolicies(
			@ApiParam(name = "merchant", value = "Merchant id", required = true)
			@RequestParam(name = "merchant") Long merchantId) throws Exception {
		Rest rest = new Rest();
		List<ReturnPolicy> contentList;
		try {
			contentList = returnPolicyService.getMerchantReturnPolicies(merchantId);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}


		List<ReturnPolicyResponse> responseList = new ArrayList<>();
		for (ReturnPolicy returnPolicy : contentList) {
			ReturnPolicyResponse rsp = modelMapper.map(returnPolicy, ReturnPolicyResponse.class);
			responseList.add(rsp);
		}
		rest.setDataList(contentList, new PageImpl<>(responseList));

		rest.setHttpStatus(HttpStatus.OK);
		return responseEntity(rest);
	}

	@ApiOperation("Get return policy by id")
	@ApiResponses({
			@ApiResponse(code = 200, response = ReturnPolicyResponse.class, message = "OK, Return policy"),
			@ApiResponse(code = 404, response = Rest.class, message = "Policy not found"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@GetMapping(value = URI.RETURN_POLICY + "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getReturnPolicyById(
			@ApiParam(name = "id", value = "Return policy id", required = true)
			@PathVariable(name = "id") Long id) throws Exception {
		Rest rest = new Rest();

		ReturnPolicy returnPolicy;
		try {
			returnPolicy = returnPolicyService.getById(id);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		ReturnPolicyResponse policyResponse = modelMapper.map(returnPolicy, ReturnPolicyResponse.class);

		rest.setData(policyResponse);

		rest.setHttpStatus(HttpStatus.OK);
		return responseEntity(rest);
	}

	@ApiOperation("Delete return policy by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return policy deleted"),
			@ApiResponse(code = 404, response = Rest.class, message = "Policy not found"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@DeleteMapping(value = URI.RETURN_POLICY + "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> deleteReturnPolicy(
			@ApiParam(name = "id", value = "Return policy id", required = true)
			@PathVariable(name = "id") Long id) throws Exception {
		try {
			returnPolicyService.delete(id);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		return responseEntity(HttpStatus.OK);
	}

	@ApiOperation("Update return policy")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return policy updated"),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@PutMapping(value = URI.RETURN_POLICY + "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateReturnPolicy(
			@ApiParam(name = "id", value = "Return policy id", required = true)
			@PathVariable(name = "id") Long id,
			@RequestBody ReturnPolicyRequest request) throws Exception {
		Rest rest = new Rest();
		try {
			ReturnPolicy returnPolicy = returnPolicyService.update(id, request);

			ReturnPolicyResponse response = modelMapper.map(returnPolicy, ReturnPolicyResponse.class);

			rest.setData(response);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		rest.setStatus(200);
		rest.setMessage("OK, Return policy updated");
		return responseEntity(rest);
	}
}
