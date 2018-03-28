package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.NamedQuery;

/**
 * @longmt99
 */
//@Entity
//@Table(name="address")
@NamedQuery(name="Address.findAll", query="SELECT l FROM Address l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class Address extends BaseEntity{

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	@Column(name="compnay")
	private String compnay;

	@Column(name="address_1")
	private String address1;

	@Column(name="address_2")
	private String address2;

	@Column(name="city")
	private String city;

	@Column(name="post_code")
	private Long postCode;

	@Column(name="zone")
	private String zone;

	@Column(name="country")
	private String country;


}
