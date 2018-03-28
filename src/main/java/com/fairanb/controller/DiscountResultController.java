package com.fairanb.controller;


import java.math.BigDecimal;
import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.common.Utils;
import com.fairanb.model.DiscountDefinition;
import com.fairanb.model.DiscountLevel;
import com.fairanb.model.DiscountResult;
import com.fairanb.model.request.DiscountResultRequest;
import com.fairanb.service.DiscountDefinitionService;
import com.fairanb.service.DiscountLevelService;
import com.fairanb.service.DiscountResultService;
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

import java.math.BigDecimal;

@RestController
public class DiscountResultController extends BaseController {

	static Logger log = LoggerFactory.getLogger(DiscountResultController.class.getName());

	@Autowired
	DiscountDefinitionService discountDefinitionService;

	@Autowired
	DiscountLevelService discountLevelService;

	@Autowired
	DiscountResultService discountResultService;

	@ApiOperation(value = "Create new Discount result")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Discount result created successfully", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.DISCOUNT_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createDiscountResult(@RequestBody DiscountResultRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		super.validateRequest(request);
		Long discountLevelId = 0L;
		boolean isFreeGift = Boolean.FALSE;
		BigDecimal amountOff = new BigDecimal(0), percentOff = new BigDecimal(0);
		Rest rest = new Rest();

		if (request.getDiscountDefinitionId() == null)
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Discount Definition id is required "));

		if (request.getDiscountLevelId() != null) {
			discountLevelId = request.getDiscountLevelId();
			DiscountLevel discountLevel = discountLevelService.findOne(discountLevelId);
			if (discountLevel == null)
				return responseEntity(
						new Rest(HttpStatus.NOT_FOUND, "Discount Level id [" + discountLevelId + "] invalid "));
		} else
			discountLevelId = null;

		if (request.getAmountOff() != null)
			amountOff = request.getAmountOff();
		if (request.getPercentOff() != null)
			percentOff = request.getPercentOff();
		if (request.getIsFreeGift() != null)
			isFreeGift = request.getIsFreeGift();

		Long discountDefinitionId = request.getDiscountDefinitionId();

		DiscountDefinition discountDefinition = discountDefinitionService.findOne(discountDefinitionId);
		if (discountDefinition == null)
			return responseEntity(
					new Rest(HttpStatus.NOT_FOUND, "Discount Definition id [" + discountDefinitionId + "] invalid "));

		DiscountResult discountResult = modelMapper.map(request, DiscountResult.class);
		log.info("Created Discount result for discount definition id is: [" + discountDefinitionId + "]");

		discountResult.setActive(Boolean.TRUE);
		discountResult.setAmountOff(amountOff);
		discountResult.setPercentOff(percentOff);
		discountResult.setIsFreeGift(isFreeGift);
		discountResult.setDiscountLevelId(discountLevelId);
		discountResult.setDiscountDefinitionId(discountDefinitionId);
		discountResult = discountResultService.save(discountResult);

		rest = new Rest(HttpStatus.CREATED);
		rest.setData(discountResult.getResponse());
		return responseEntity(rest);
	}

	@ApiOperation(value = "Update discount result")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of discount result update", response = Rest.class),
			@ApiResponse(code = 409, message = "CONFLICT, Discount result data existed", response = Rest.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.DISCOUNT_RESULT + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateDiscountResult(@ApiParam(value = "Discount result Id") @PathVariable Long id,
			@ApiParam(value = "Object json discount result") @RequestBody DiscountResultRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		if (id == null)
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Discount result id is required"));

		Long resultId = id;
		Rest rest = new Rest();
		log.info("Update discount result:" + request);

		DiscountResult existed = discountResultService.findOne(resultId);
		if (existed == null) {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Discount result id [" + resultId + "] invalid "));
		}

		try {
			updateValidation(request, existed);
		} catch (Exception e) {
			return responseEntity(new Rest(HttpStatus.CONFLICT, e.getMessage()));
		}

		Utils.copyNonNullProperties(request, existed);

		existed = discountResultService.save(existed);
		rest = new Rest(HttpStatus.OK);
		rest.setData(existed.getResponse());
		log.info("Patch discount result with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete discount result")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted discount result", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.DISCOUNT_RESULT, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteDiscountResult(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		log.info("Delete  discount result [" + id + "]");
		DiscountResult discountResult = discountResultService.findOne(id);

		if (discountResult != null) {
			log.info("Inactive  discount result [" + id + "]");
			discountResultService.delete(id);
			return responseEntity(HttpStatus.OK, "Deleted discount result [" + id + "] sucessfully");
		} else {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Discount result id [" + id + "] invalid "));
		}
	}

	private void updateValidation(DiscountResultRequest request, DiscountResult existed) throws Exception {
		if (request.getDiscountDefinitionId() != null) {
			if (request.getDiscountDefinitionId() != existed.getDiscountDefinitionId()) {
				Long definitionId = request.getDiscountDefinitionId();
				DiscountDefinition discountDefinition = discountDefinitionService.findOne(definitionId);
				if (discountDefinition == null)
					throw new Exception("Discount Definition id [" + definitionId + "] invalid ");
			}
		}

		if (request.getDiscountLevelId() != null) {
			if (request.getDiscountLevelId() != existed.getDiscountLevelId()) {
				Long levelId = request.getDiscountLevelId();
				DiscountLevel discountLevel = discountLevelService.findOne(levelId);
				if (discountLevel == null)
					throw new Exception("Discount Level id [" + levelId + "] invalid ");
			}
		}
	}

}
