package com.fairanb.model.response;

import com.fairanb.model.Country;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryThirdServiceResponse implements Serializable {

    private boolean IsSuccess;

    @JsonProperty("UserMessage")
    private String UserMessage;

    @JsonProperty("TechnicalMessage")
    private String TechnicalMessage;

    @JsonProperty("TotalCount")
    private Integer TotalCount;

    @JsonProperty("Response")
    private List<Country> response;
}
