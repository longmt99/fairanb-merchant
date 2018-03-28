package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @mustafamym
 */
@Entity
@Table(name="geo_zone")
@NamedQuery(name="GeoZone.findAll", query="SELECT l FROM GeoZone l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class GeoZone extends BaseEntity{

	@Basic(optional = false)
	@Column(name="code")
	private String code;

	@Column(name="name")
	private String name;

	@Column(name="merchant_id")
	private Long merchantId;
}
