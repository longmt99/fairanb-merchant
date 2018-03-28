package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.GiftCard;
import com.fairanb.model.request.GiftCardRequest;
import com.fairanb.model.response.GiftCardResponse;
import com.fairanb.service.GiftCardService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class GiftCardController extends BaseController {

	@Autowired
	private GiftCardService service;

	private ModelMapper modelMapper;

	public GiftCardController() {
		this.modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	}

	@ApiOperation("Create gift card")
	@ApiResponses({
			@ApiResponse(code = 201, message = "CREATED, Gift card created successfully"),
			@ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@PostMapping(value = URI.GIFT_CARD, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> createGiftCard(@RequestBody GiftCardRequest request,
											   @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
		validateRequest(request);

		Rest rest = new Rest();

		GiftCard giftCard = modelMapper.map(request, GiftCard.class);
		try {
			giftCard = service.createGiftCard(giftCard);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}
		GiftCardResponse response = modelMapper.map(giftCard, GiftCardResponse.class);

		rest.setData(response);
		rest.setHttpStatus(HttpStatus.CREATED);

		return responseEntity(rest);
	}

	@ApiOperation("Get merchant gift cards")
	@ApiResponses({
			@ApiResponse(code = 200, response = GiftCardResponse.class, message = "OK, Return merchant gift cards"),
			@ApiResponse(code = 404, response = Rest.class, message = "Gift cards for provided merchant id not found"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@GetMapping(value = URI.GIFT_CARD, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getMerchantGiftCards(
			@ApiParam(name = "merchant", value = "Merchant id", required = true)
			@RequestParam(name = "merchant") Long merchantId) throws Exception {
		Rest rest = new Rest();

		List<GiftCard> contentList;
		try {
			contentList = service.getMerchantGiftCards(merchantId);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		List<GiftCardResponse> responseList = new ArrayList<>();
		for (GiftCard giftCard : contentList) {
			GiftCardResponse rsp = modelMapper.map(giftCard, GiftCardResponse.class);
			responseList.add(rsp);
		}
		rest.setDataList(contentList, new PageImpl<>(responseList));

		rest.setHttpStatus(HttpStatus.OK);
		return responseEntity(rest);
	}

	@ApiOperation("Get gift card by id")
	@ApiResponses({
			@ApiResponse(code = 200, response = GiftCardResponse.class, message = "OK, Gift card"),
			@ApiResponse(code = 404, response = Rest.class, message = "Gift card not found"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@GetMapping(value = URI.GIFT_CARD + "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> getGiftCardById(
			@ApiParam(name = "id", value = "Gift card id", required = true)
			@PathVariable(name = "id") Long id) throws Exception {
		Rest rest = new Rest();

		GiftCard giftCard;
		try {
			giftCard = service.getById(id);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		GiftCardResponse giftCardResponse = modelMapper.map(giftCard, GiftCardResponse.class);

		rest.setData(giftCardResponse);

		rest.setHttpStatus(HttpStatus.OK);
		return responseEntity(rest);
	}


	@ApiOperation("Delete gift card by id")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Gift card deleted"),
			@ApiResponse(code = 404, response = Rest.class, message = "Gift card not found"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@DeleteMapping(value = URI.GIFT_CARD + "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> deleteGiftCard(
			@ApiParam(name = "id", value = "Gift card id", required = true)
			@PathVariable(name = "id") Long id) throws Exception {
		try {
			service.delete(id);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		return responseEntity(HttpStatus.OK);
	}

	@ApiOperation("Update gift card")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK, Gift card updated"),
			@ApiResponse(code = 404, response = Rest.class, message = "Invalid data"),
			@ApiResponse(code = 500, response = Rest.class, message = "Internal server error")
	})
	@CrossOrigin("*")
	@ResponseBody
	@PutMapping(value = URI.GIFT_CARD + "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Rest> updateGiftCard(
			@ApiParam(name = "id", value = "Gift card id", required = true)
			@PathVariable(name = "id") Long id,
			@RequestBody GiftCardRequest request) throws Exception {
		Rest rest = new Rest();
		try {
			GiftCard giftCard = service.update(id, request);
			GiftCardResponse response = modelMapper.map(giftCard, GiftCardResponse.class);
			rest.setData(response);
		} catch (NullPointerException e) {
			return responseEntity(HttpStatus.NOT_FOUND, e.getMessage());
		}

		rest.setHttpStatus(HttpStatus.OK);
		return responseEntity(rest);
	}
}
