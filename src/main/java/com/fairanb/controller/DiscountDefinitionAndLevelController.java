package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.JConstants.Status;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.common.Utils;
import com.fairanb.model.DiscountDefinition;
import com.fairanb.model.DiscountLevel;
import com.fairanb.model.Merchant;
import com.fairanb.model.Paging;
import com.fairanb.model.request.DiscountDefinitionRequest;
import com.fairanb.model.response.DiscountDefinitionResponse;
import com.fairanb.service.DiscountDefinitionService;
import com.fairanb.service.DiscountLevelService;
import com.fairanb.service.MerchantService;
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
public class DiscountDefinitionAndLevelController extends BaseController {

	static Logger log = LoggerFactory.getLogger(DiscountDefinitionAndLevelController.class.getName());

	@Autowired
	DiscountDefinitionService discountDefinitionService;

	@Autowired
	DiscountLevelService discountLevelService;

	@Autowired
	MerchantService merchantService;

	@ApiOperation(value = "Create new Discount definition")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Discount definition created successfully", response = Rest.class),
			@ApiResponse(code = 409, message = "CONFLICT, Discount definition data existed", response = Rest.class),
			@ApiResponse(code = 423, message = "LOCKED, The Discount definition existed but status inactived", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PostMapping(value = URI.DISCOUNT_DEFINITION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> createDiscountDefinition(@RequestBody DiscountDefinitionRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		ModelMapper modelMapper = new ModelMapper();
		super.validateRequest(request);
		Rest rest = new Rest();
		try {
			validateCreateRequest(request);
		} catch (Exception e) {
			return responseEntity(new Rest(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()));
		}

		Long merchantId = request.getMerchantId();
		String name = request.getName();
		DiscountDefinition discountDefinition = modelMapper.map(request, DiscountDefinition.class);
		DiscountDefinition existed = discountDefinitionService.findByName(name);

		if (existed == null) {
			Merchant merchant = merchantService.findOne(merchantId);
			if (merchant != null) {
				log.info("Created Discount definition name: [" + name + "]");
				discountDefinition.setStatus(Status.ACTIVE.name());
				discountDefinition.setActive(Boolean.TRUE);
				discountDefinition.setMerchant(merchant);
				discountDefinition = discountDefinitionService.save(discountDefinition);
				rest = new Rest(HttpStatus.CREATED);
				rest.setData(discountDefinition.getResponse());
			} else {
				return responseEntity(new Rest(HttpStatus.NOT_FOUND, "Merchant id [" + merchantId + "] invalid "));
			}
		} else {
			String currentStatus = existed.getStatus();
			if (currentStatus.equals(Status.INACTIVE.name())) {
				rest = new Rest(HttpStatus.LOCKED);
			} else {
				status = HttpStatus.CONFLICT;
				String message = "Discount definition already existed [" + name + "]";
				rest = new Rest(status, message);
				rest.setData(existed.getResponse());
			}
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "Get Discount definition by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Return discount definition response ", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DISCOUNT_DEFINITION + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getDiscountDefinition(
			@ApiParam(value = "Get discount definition by Id") @PathVariable Long id) throws Exception {
		log.info("Search Discount definition");

		Rest rest = new Rest();
		DiscountDefinition discountDefinition = discountDefinitionService.findOne(id);
		log.info("Get discount definition by getId [" + id + "]");
		if (discountDefinition != null) {
			rest.setHttpStatus(HttpStatus.OK);
			rest.setData(discountDefinition.getResponse());
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get discount definition id [" + id + "]");
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "List discount definition by conditions")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return list by the conditions ", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DISCOUNT_DEFINITION, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listDiscountDefinition(@RequestParam(required = false) String code,
			@RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "10") int size,
			@RequestParam(required = false, defaultValue = "") String sortDirection,
			@RequestParam(required = false, defaultValue = "") String sortProperty) throws Exception {
		log.info("Search discount definition");

		Rest rest = new Rest();

		PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);

		Page<DiscountDefinition> result = discountDefinitionService.findByStatus(Status.ACTIVE.name(), paging);
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search discount definition total elements is [" + result.getTotalElements() + "]");
		List<DiscountDefinition> contentList = result.getContent();
		List<DiscountDefinitionResponse> responseList = new ArrayList<DiscountDefinitionResponse>();

		contentList.forEach(discountDefinition -> {
			responseList.add(discountDefinition.getResponse());
		});
		rest.setDataList(responseList, result);
		return responseEntity(rest);
	}

	@ApiOperation(value = "Update discount definition")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Successful retrieval of discount definition update", response = Rest.class),
			@ApiResponse(code = 409, message = "CONFLICT, Discount definition data existed", response = Rest.class),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@PutMapping(value = URI.DISCOUNT_DEFINITION + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateDiscountDefinition(
			@ApiParam(value = "Discount definition Id") @PathVariable Long id,
			@ApiParam(value = "Object json discount definition") @RequestBody DiscountDefinitionRequest request,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		Long definitionId = id;
		Rest rest = new Rest();
		log.info("Update discount definition:" + request);
		DiscountDefinition existed = discountDefinitionService.findOne(definitionId);
		if (existed == null) {
			return responseEntity(
					new Rest(HttpStatus.NOT_FOUND, "Discount definition id [" + definitionId + "] invalid "));
		}

		try {
			validateUpdateRequest(request, existed);
		} catch (Exception e) {
			return responseEntity(new Rest(HttpStatus.CONFLICT, e.getMessage()));
		}

		Utils.copyNonNullProperties(request, existed);

		existed = discountDefinitionService.save(existed);
		rest = new Rest(HttpStatus.OK);
		rest.setData(existed.getResponse());
		log.info("Patch discount definition with existed = " + existed);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete discount definition")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Deleted discount definition", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@DeleteMapping(value = URI.DISCOUNT_DEFINITION, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Rest> deleteDiscountDefinition(@RequestParam(required = true) Long id,
			@RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken)
			throws Exception {

		log.info("Delete  discount definition [" + id + "]");
		DiscountDefinition discountDefinition = discountDefinitionService.findOne(id);
		if (discountDefinition != null) {
			log.info("Inactive  discount definition [" + id + "]");
			discountDefinitionService.softDelete(discountDefinition);
			return responseEntity(HttpStatus.OK, "Deleted  discount definition [" + id + "] sucessfully");
		} else {
			return responseEntity(HttpStatus.NOT_FOUND);
		}
	}

	@ApiOperation(value = "Get Discount level by id")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return discount level response ", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DISCOUNT_LEVEL + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getDiscountLevel(@ApiParam(value = "Get discount level by Id") @PathVariable Long id)
			throws Exception {
		log.info("Search Discount level");

		Rest rest = new Rest();
		DiscountLevel discountlevel = discountLevelService.findOne(id);
		log.info("Get discount level by getId [" + id + "]");
		if (discountlevel != null) {
			rest.setHttpStatus(HttpStatus.OK);
			rest.setData(discountlevel);
		} else {
			rest.setHttpStatus(HttpStatus.NOT_FOUND);
			rest.setMessage("Can not get discount level id [" + id + "]");
		}

		return responseEntity(rest);
	}

	@ApiOperation(value = "List discount level by conditions")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK, Return list by the conditions ", response = Rest.class),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
	@CrossOrigin(origins = "*")
	@GetMapping(value = URI.DISCOUNT_LEVEL, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> listDiscountLevel() throws Exception {
		log.info("Search discount level");

		Rest rest = new Rest();
		List<DiscountLevel> discountListList = discountLevelService.findAll();
		rest.setHttpStatus(HttpStatus.OK);
		log.info("Search discount level total elements is [" + discountListList.size() + "]");
		rest.setData(discountListList);

		return responseEntity(rest);
	}

	private void validateCreateRequest(DiscountDefinitionRequest request) throws Exception {
		if (request.getName() == null)
			throw new Exception("Field name is required");
		if (request.getMerchantId() == null)
			throw new Exception("Field merchant id is required");
		if (request.getEndDate() == null && request.getStartDate() == null)
			throw new Exception("Start date and End date is required ");
		if (request.getEndDate().compareTo(request.getStartDate()) <= 0)
			throw new Exception("End date is greater than start date");
	}

	private void validateUpdateRequest(DiscountDefinitionRequest request, DiscountDefinition existed) throws Exception {
		if (request.getEndDate() != null && request.getStartDate() != null
				&& request.getEndDate().compareTo(request.getStartDate()) <= 0) {
			throw new Exception("End date is greater than start date");
		} else if (request.getStartDate() != null && request.getEndDate() == null
				&& existed.getEndDate().compareTo(request.getStartDate()) <= 0) {
			throw new Exception("Start date is greater then existing end date");
		} else if (request.getEndDate() != null && request.getStartDate() == null
				&& request.getEndDate().compareTo(existed.getStartDate()) <= 0) {
			throw new Exception("End date is less then existing start date");
		}

		if (request.getMerchantId() != null) {
			Long merchantId = request.getMerchantId();
			if (existed.getMerchant().getId() != merchantId) {
				Merchant merchant = merchantService.findOne(merchantId);
				if (merchant == null) {
					throw new Exception("Merchant id [" + merchantId + "] invalid ");
				}
			}
		}

		if (request.getName() != null) {
			String name = request.getName();
			if (!existed.getName().equalsIgnoreCase(name)) {
				DiscountDefinition discountDefinition = discountDefinitionService.findByName(name);
				if (discountDefinition != null) {
					String message = "Discount definition already existed [" + name + "]";
					throw new Exception(message);
				}
			}
		}
	}

}
