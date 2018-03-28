package com.fairanb.model.request;

import com.fairanb.model.response.CountryDescriptionResponse;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @mustafamym
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class CountryRequest {
	private Long id;

	private String name;

	private String isoCode2;

	private String isoCode3;

	private String address;

	private Boolean postCode;

	private String status;

	private List<CountryDescriptionResponse> descriptions;

}
