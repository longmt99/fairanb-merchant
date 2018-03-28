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
@Table(name="zone")
@NamedQuery(name="Zone.findAll", query="SELECT l FROM Zone l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class Zone extends BaseEntity{

	@Basic(optional = false)
	@Column(name="country_id")
	private Long countryId;

	@Basic(optional = false)
	@Column(name="code")
	private String code;

	@Column(name = "name")
	private String name;

}
