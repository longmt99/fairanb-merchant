package com.fairanb.controller;

import com.fairanb.common.JConstants;
import com.fairanb.common.Rest;
import com.fairanb.common.URI;
import com.fairanb.model.Country;
import com.fairanb.model.Manufacturer;
import com.fairanb.model.ManufacturerDescription;
import com.fairanb.model.Paging;
import com.fairanb.model.request.CountryRequest;
import com.fairanb.model.request.ManufacturerRequest;
import com.fairanb.model.request.MerchantRequest;
import com.fairanb.model.response.CountryResponse;
import com.fairanb.model.response.ManufacturerDescriptionResponse;
import com.fairanb.model.response.ManufacturerResponse;
import com.fairanb.service.LanguageService;
import com.fairanb.service.ManufacturerService;
import com.fairanb.service.MerchantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Date;
import java.util.List;

@RestController
public class ManufacturerController extends BaseController {

    static Logger log = LoggerFactory.getLogger(CountryController.class.getName());

    @Autowired
    private ManufacturerService manufacturerService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private MerchantService merchantService;

    private ModelMapper mapper = new ModelMapper();

    @ApiOperation(value = "Create new manufacturer")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED, manufacturer created successfully", response = CountryResponse.class),
            @ApiResponse(code = 409, message = "CONFLICT, manufacturer data existed", response = Rest.class),
            @ApiResponse(code = 423, message = "LOCKED, The manufacturer existed but status inactived", response = Rest.class),
            @ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
            @ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
    @CrossOrigin(origins = "*")
    @PostMapping(value = URI.MANUFACTURERS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> createManufacturer(@RequestBody ManufacturerRequest request,
                                                @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
        log.info("Get manufacturer by id");
        ObjectMapper objectMapper = new ObjectMapper();
        Rest rest = new Rest();

        if (StringUtils.isBlank(request.getName())) {
            Rest err = new Rest(HttpStatus.BAD_REQUEST, "Manufacturer name is required");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        if (request.getMerchantId() == null || request.getMerchantId() == 0) {
            Rest err = new Rest(HttpStatus.BAD_REQUEST, "Merchant Id is invalid");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        if (request.getCustomerId() == null || request.getCustomerId() == 0) {
            Rest err = new Rest(HttpStatus.BAD_REQUEST, "Customer Id is invalid");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        try {
            Manufacturer manufacturer = this.mapper.map(request, Manufacturer.class);
            manufacturer.setCreatedDate(new Date());
            manufacturer.setModifiedDate(new Date());
            manufacturer.setStatus("NEW");
            List<ManufacturerDescription> manufacturerDescriptions = new ArrayList<ManufacturerDescription>();
            for (ManufacturerDescriptionResponse mr : request.getDescriptions()) {
                ManufacturerDescription md = this.mapper.map(mr, ManufacturerDescription.class);
                md.setLanguage(languageService.findByLanguageName(mr.getLanguage()));
                md.setCreatedDate(new Date());
                md.setModifiedDate(new Date());
                md.setManufacturer(manufacturer);
                manufacturerDescriptions.add(md);
            }
            manufacturer.setDescriptions(manufacturerDescriptions);
            manufacturer.setMerchant(merchantService.findOne(request.getMerchantId()));
            manufacturer = manufacturerService.save(manufacturer);
            rest.setStatus(HttpStatus.CREATED.value());
            rest.setMessage("Manufacturer is created successfully");
            ManufacturerResponse manufacturerResponse = this.mapper.map(manufacturer, ManufacturerResponse.class);
            toResponse(manufacturer, manufacturerResponse);
            rest.setHttpStatus(HttpStatus.OK);
            rest.setData(manufacturerResponse);
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when saving manufacturer. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }

        String json = objectMapper.writeValueAsString(rest);
        return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
    }

    @ApiOperation(value = "Update new manufacturer")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED, manufacturer created successfully", response = CountryResponse.class),
            @ApiResponse(code = 409, message = "CONFLICT, manufacturer data existed", response = Rest.class),
            @ApiResponse(code = 423, message = "LOCKED, The manufacturer existed but status inactived", response = Rest.class),
            @ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
            @ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
    @CrossOrigin(origins = "*")
    @PutMapping(value = URI.MANUFACTURERS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> updateManufacturer(@RequestBody ManufacturerRequest request,
                                                     @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
        log.info("Get manufacturer by id");
        ObjectMapper objectMapper = new ObjectMapper();
        Rest rest = new Rest();
        Long id = request.getId();

        //check valid id
        if (id == null || id.longValue() == 0) {
            Rest err = new Rest(HttpStatus.NOT_FOUND, "Manufacturer id is invalid");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }

        try {
            Manufacturer exist =  manufacturerService.getById(id);
            if (exist == null) {
                Rest err = new Rest(HttpStatus.NOT_FOUND, "Manufacturer is not found");
                String json = objectMapper.writeValueAsString(err);
                return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
            }
            Manufacturer manufacturer = this.mapper.map(request, Manufacturer.class);
            manufacturer = manufacturerService.save(manufacturer);
            ManufacturerResponse manufacturerResponse = this.mapper.map(manufacturer, ManufacturerResponse.class);
            toResponse(manufacturer, manufacturerResponse);
            rest.setHttpStatus(HttpStatus.OK);
            rest.setData(manufacturerResponse);
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when updating manufacturer. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }

        String json = objectMapper.writeValueAsString(rest);
        return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
    }

    @ApiOperation(value = "Delete manufacturer by id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED, manufacturer created successfully", response = CountryResponse.class),
            @ApiResponse(code = 409, message = "CONFLICT, manufacturer data existed", response = Rest.class),
            @ApiResponse(code = 423, message = "LOCKED, The manufacturer existed but status inactived", response = Rest.class),
            @ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
            @ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
    @CrossOrigin(origins = "*")
    @DeleteMapping(value = URI.MANUFACTURERS + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> deleteManufacturer(@PathVariable Long id, @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {

        log.info("Get manufacturer by id");
        ObjectMapper objectMapper = new ObjectMapper();
        Rest rest = new Rest();

        try {
            manufacturerService.deleteById(id);
            rest.setHttpStatus(HttpStatus.OK);
            rest.setMessage("Manufacturer id [" + id + "] is deleted successfully");
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when deleting manufacturer. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }

        String json = objectMapper.writeValueAsString(rest);
        return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
    }

    @ApiOperation(value = "Get all manufacturer")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED, manufacturer created successfully", response = CountryResponse.class),
            @ApiResponse(code = 409, message = "CONFLICT, manufacturer data existed", response = Rest.class),
            @ApiResponse(code = 423, message = "LOCKED, The manufacturer existed but status inactived", response = Rest.class),
            @ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
            @ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
    @CrossOrigin(origins = "*")
    @GetMapping(value = URI.MANUFACTURERS, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getAllManufacturer(@RequestParam(required = false) String code,
                                                     @RequestParam(required = false, defaultValue = "1") int page,
                                                     @RequestParam(required = false, defaultValue = "10") int size,
                                                     @RequestParam(required = false, defaultValue = "") String sortDirection,
                                                     @RequestParam(required = false, defaultValue = "") String sortProperty,
                                                     @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {
        log.info("Get manufacturer by id");
        ObjectMapper objectMapper = new ObjectMapper();
        Rest rest = new Rest();

        PageRequest paging = new Paging().getPageRequest(page, size, sortDirection, sortProperty);
        try {
            Page<Manufacturer> manufacturerPage = manufacturerService.getAll(paging);
            List<Manufacturer> manufacturerList = manufacturerPage.getContent();
            if (manufacturerList == null || manufacturerList.size() == 0) {
                rest.setHttpStatus(HttpStatus.NOT_FOUND);
                rest.setMessage("There is no manufacturer on database");
            }
            List<ManufacturerResponse> manufacturerResponseList = new ArrayList<ManufacturerResponse>();
            for (Manufacturer m : manufacturerList) {
                ManufacturerResponse mr = this.mapper.map(m, ManufacturerResponse.class);
                toResponse(m, mr);
                manufacturerResponseList.add(mr);
            }
            rest.setHttpStatus(HttpStatus.OK);
            rest.setDataList(manufacturerResponseList, manufacturerPage);
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when getting manufacturer. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }

        String json = objectMapper.writeValueAsString(rest);
        return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
    }

    @ApiOperation(value = "Get manufacturer by id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED, manufacturer created successfully", response = CountryResponse.class),
            @ApiResponse(code = 409, message = "CONFLICT, manufacturer data existed", response = Rest.class),
            @ApiResponse(code = 423, message = "LOCKED, The manufacturer existed but status inactived", response = Rest.class),
            @ApiResponse(code = 422, response = Rest.class, message = "Invalid data"),
            @ApiResponse(code = 500, response = Rest.class, message = "Internal server error") })
    @CrossOrigin(origins = "*")
    @GetMapping(value = URI.MANUFACTURERS + URI.ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getManufacturerById(@PathVariable Long id, @RequestHeader(name = JConstants.AUTH_HEADER, defaultValue = JConstants.BEARER) String accessToken) throws Exception {

        log.info("Get manufacturer by id");
        ObjectMapper objectMapper = new ObjectMapper();

        Rest rest = new Rest();
        try {
            Manufacturer manufacturer = manufacturerService.getById(id);
            if (manufacturer == null) {
                rest.setHttpStatus(HttpStatus.NOT_FOUND);
                rest.setMessage("Couldn't find manufacturer [" + id + "]");
            } else {
                ManufacturerResponse manufacturerResponse = this.mapper.map(manufacturer, ManufacturerResponse.class);
                toResponse(manufacturer, manufacturerResponse);
                rest.setData(manufacturerResponse);
                rest.setHttpStatus(HttpStatus.OK);
            }
        } catch (Exception ex) {
            Rest err = new Rest(HttpStatus.INTERNAL_SERVER_ERROR, "There is an error when getting all manufacturer. Please see log for more details.");
            String json = objectMapper.writeValueAsString(err);
            return new ResponseEntity<String>(json, HttpStatus.valueOf(err.getStatus()));
        }
        String json = objectMapper.writeValueAsString(rest);
        return new ResponseEntity<String>(json, HttpStatus.valueOf(rest
                .getStatus()));
    }

    private void toResponse(Manufacturer manufacturer, ManufacturerResponse manufacturerResponse) throws JSONException {
        MerchantRequest merchant = new MerchantRequest();
        merchant.setId(manufacturer.getMerchant().getId());
        merchant.setName(manufacturer.getMerchant().getName());
        merchant.setPhone(manufacturer.getMerchant().getPhone());
        merchant.setEmail(manufacturer.getMerchant().getEmail());
        manufacturerResponse.setMerchant(merchant);
        List<ManufacturerDescriptionResponse> descriptions = new ArrayList<>();
        manufacturerResponse.setDescriptions(descriptions);
        if (manufacturer.getDescriptions() != null && CollectionUtils.isNotEmpty(manufacturer.getDescriptions())) {
            for (ManufacturerDescription md : manufacturer.getDescriptions()) {
                ManufacturerDescriptionResponse mdr = new ManufacturerDescriptionResponse();
                mdr.setId(md.getId());
                mdr.setName(md.getName());
                mdr.setTitle(md.getTitle());
                mdr.setDescription(md.getDescription());
                mdr.setManufacturerUrl(md.getManufacturerUrl());
                mdr.setLanguage(md.getLanguage().getName());
                descriptions.add(mdr);
            }
        }
    }
}
