package com.fairanb.model.request;

import com.fairanb.model.BaseEntity;
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
public class GeoZoneRequest extends BaseEntity{

	private Long id;

	private String code;

	private String name;

	private Long merchantId;
}
