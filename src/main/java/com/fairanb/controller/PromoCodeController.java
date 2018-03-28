package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.JConstants.Status;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.common.Utils;
import com.fairanb.model.PromoCode;
import com.fairanb.model.request.PromoCodeRequest;
import com.fairanb.model.response.PromoCodeResponse;
import com.fairanb.service.PromoCodeService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PromoCodeController extends BaseController {

	static Logger log = LoggerFactory.getLogger(PromoCodeController.class.getName());

	@Autowired
	public PromoCodeService promoCodeService;

	@ApiOperation(value = "Create new Promo Code")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Promo Code created successfully", response = Rest.class),
			@ApiResponse(code = 409, message = "CONFLICT, Promo Code data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The Promo Code existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.PROMO_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createPromoCode(@RequestBody PromoCodeRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		super.validateRequest(request);

		Rest rest = new Rest();
		String code = request.getCode();
		PromoCode promoCode = modelMapper.map(request, PromoCode.class);
		if (request.getEndDate() != null && request.getStartDate() != null
				&& request.getEndDate().compareTo(request.getStartDate()) <= 0)
			return responseEntity(new Rest(HttpStatus.UNPROCESSABLE_ENTITY, "End date is greater than start date"));

		PromoCode existed = promoCodeService.findByCode(code);

		if (existed == null) {
			log.info("Created Promo code: [" + code + "]");
			promoCode.setStatus(Status.ACTIVE.name());
			promoCode = promoCodeService.save(promoCode);
			rest = new Rest(HttpStatus.CREATED);
			rest.setData(promoCode.getResponse());
		} else {
			String currentStatus = existed.getStatus();
			if (currentStatus.equals(Status.INACTIVE.name())) {
				status = HttpStatus.LOCKED;
				rest = new Rest(status);
			} else {
				status = HttpStatus.CONFLICT;
				String message = "Promo code already existed [" + code + "]";
				rest = new Rest(status, message);
			}
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Get Promo code by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return promo code response ", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.PROMO_CODE + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getPromoCode(@ApiParam(value = "Get promo code by Id") @PathVariable Long id)
			throws Exception {
		log.info("Search Promo code");

		Rest rest = new Rest();
		PromoCode promoCode = promoCodeService.findOne(id);
		log.info("Get promo code by getId [" + id + "]");
		if (promoCode != null) {
			rest.setHttpStatus(HttpStatus.OK);
			rest.setData(promoCode.getResponse());
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get promo code id [" + id + "]");
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "List promo code by conditions")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return list by the conditions ", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.PROMO_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listPromoCode() throws Exception {
		log.info("Search promo code");

		Rest rest = new Rest();

		List<PromoCode> promoCodeList = promoCodeService.findByStatus(Status.ACTIVE.name());
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search promo code total elements [" + promoCodeList.size() + "]");
		List<PromoCodeResponse> responseList = new ArrayList<PromoCodeResponse>();

		promoCodeList.forEach(promoCode -> {
			responseList.add(promoCode.getResponse());
		});
		rest.setData(responseList);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Update promo code")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of promo code update", response = Rest.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.PROMO_CODE + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updatePromoCode(@ApiParam(value = "Update promo code by Id") @PathVariable Long id,
			@ApiParam(value = "Object json promo code") @RequestBody PromoCodeRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		Long promoCodeId = id;
		log.info("Update promo code:" + request);
		PromoCode existed = promoCodeService.findOne(promoCodeId);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Promocode id [" + promoCodeId + "] invalid "));
		}

		if (request.getEndDate() != null && request.getStartDate() != null
				&& request.getEndDate().compareTo(request.getStartDate()) <= 0) {
			return responseEntity(new Rest(HttpStatus.UNPROCESSABLE_ENTITY, "End date is greater than start date"));
		} else if (request.getStartDate() != null && request.getEndDate() == null
				&& existed.getEndDate().compareTo(request.getStartDate()) <= 0) {
			return responseEntity(
					new Rest(HttpStatus.UNPROCESSABLE_ENTITY, "Start date is greater then existing end date"));
		} else if (request.getEndDate() != null && request.getStartDate() == null
				&& request.getEndDate().compareTo(existed.getStartDate()) <= 0) {
			return responseEntity(
					new Rest(HttpStatus.UNPROCESSABLE_ENTITY, "End date is less then existing start date"));
		}

		Utils.copyNonNullProperties(request, existed);
		existed = promoCodeService.save(existed);
		Rest rest = new Rest(HttpStatus.OK);
		rest.setData(existed.getResponse());
		log.info("Patch promo code with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete promo code")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted promo code", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.PROMO_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deletePromCode(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		log.info("Delete  Promo code [" + id + "]");
		PromoCode promoCode = promoCodeService.findOne(id);
		if (promoCode != null) {
			log.info("Inactive  promo code [" + id + "]");
			promoCode.setStatus("INACTIVE");
			promoCode.setActive(Boolean.FALSE);
			promoCodeService.save(promoCode);
			return responseEntity(HttpStatus.OK, "Deleted  promo code [" + id + "] sucessfully");
		} else {
			return responseEntity(HttpStatus.NOT_FOUND);
		}
	}

}
