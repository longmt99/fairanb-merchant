package com.fairanb.controller;

import java.math.BigDecimal;
import com.fairanb.common.JConstants;
import com.fairanb.common.JConstants.Status;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.common.Utils;
import com.fairanb.model.DiscountCondition;
import com.fairanb.model.DiscountDefinition;
import com.fairanb.model.DiscountLevel;
import com.fairanb.model.request.DiscountConditionRequest;
import com.fairanb.service.DiscountConditionService;
import com.fairanb.service.DiscountDefinitionService;
import com.fairanb.service.DiscountLevelService;
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
public class DiscountConditionController extends BaseController {

	static Logger log = LoggerFactory.getLogger(DiscountConditionController.class.getName());

	@Autowired
	DiscountConditionService discountConditionService;

	@Autowired
	DiscountDefinitionService discountDefinitionService;

	@Autowired
	DiscountLevelService discountLevelService;

	@ApiOperation(value = "Create new Discount condition")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Discount condition created successfully", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.DISCOUNT_CONDITION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createDiscountCondition(@RequestBody DiscountConditionRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		super.validateRequest(request);
		Long discountLevelId = 0L;
		BigDecimal amount = new BigDecimal(0);
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
		if (request.getAmount() != null)
			amount = request.getAmount();

		Long discountDefinitionId = request.getDiscountDefinitionId();
		DiscountDefinition discountDefinition = discountDefinitionService.findOne(discountDefinitionId);
		if (discountDefinition == null)
			return responseEntity(
					new Rest(HttpStatus.NOT_FOUND, "Discount Definition id [" + discountDefinitionId + "] invalid "));

		DiscountCondition discountCondition = modelMapper.map(request, DiscountCondition.class);
		log.info("Created Discount condition for discount definition id is: [" + discountDefinitionId + "]");

		discountCondition.setStatus(Status.ACTIVE.name());
		discountCondition.setActive(Boolean.TRUE);
		discountCondition.setAmount(amount);
		discountCondition.setDiscountLevelId(discountLevelId);
		discountCondition.setDiscountDefinitionId(discountDefinitionId);
		discountCondition = discountConditionService.save(discountCondition);

		rest = new Rest(HttpStatus.CREATED);
		rest.setData(discountCondition.getResponse());
		return responseEntity(rest);
	}

	@ApiOperation(value = "Update discount condition")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of discount condition update", response = Rest.class),
			@ApiResponse(code = 409, message = "CONFLICT, Discount condition data existed", response = Rest.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.DISCOUNT_CONDITION + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateDiscountCondition(
			@ApiParam(value = "Discount condition Id") @PathVariable Long id,
			@ApiParam(value = "Object json discount condition") @RequestBody DiscountConditionRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		if (id == null)
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Discount Condition id is required"));

		Long conditionId = id;
		Rest rest = new Rest();
		log.info("Update discount condition:" + request);
		DiscountCondition existed = discountConditionService.findOne(conditionId);
		if (existed == null) {
			return responseEntity(
					new Rest(HttpStatus.NOT_FOUND, "Discount Condition id [" + conditionId + "] invalid "));
		}

		try {
			updateValidation(request, existed);
		} catch (Exception e) {
			return responseEntity(new Rest(HttpStatus.CONFLICT, e.getMessage()));
		}

		Utils.copyNonNullProperties(request, existed);

		existed = discountConditionService.save(existed);
		rest = new Rest(HttpStatus.OK);
		rest.setData(existed.getResponse());
		log.info("Patch discount condition with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete discount condition")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted discount condition", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.DISCOUNT_CONDITION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteDiscountCondition(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		log.info("Delete  discount condition [" + id + "]");
		DiscountCondition discountCondition = discountConditionService.findOne(id);
		if (discountCondition != null) {
			log.info("Inactive  discount condition [" + id + "]");
			discountConditionService.delete(id);
			return responseEntity(HttpStatus.OK, "Deleted discount condition [" + id + "] sucessfully");
		} else {
			return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Discount Condition id [" + id + "] invalid "));
		}
	}

	private void updateValidation(DiscountConditionRequest request, DiscountCondition existed) throws Exception {
		if (request.getDiscountDefinitionId() != null) {
			if (request.getDiscountDefinitionId() != existed.getDiscountDefinitionId()) {
				Long newConditionId = request.getDiscountDefinitionId();
				DiscountDefinition discountDefinition = discountDefinitionService.findOne(newConditionId);
				if (discountDefinition == null)
					throw new Exception("Discount Definition id [" + newConditionId + "] invalid ");
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
