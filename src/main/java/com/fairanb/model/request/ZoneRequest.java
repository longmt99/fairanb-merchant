package com.fairanb.model.request;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * @mustafamym
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class ZoneRequest {

	private Long id;

	private Long countryId;

	private String code;

	private String name;
}
