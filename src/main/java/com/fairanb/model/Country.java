package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @mustafamym
 */
@Entity
@Table(name="country")
@NamedQuery(name="Country.findAll", query="SELECT l FROM Country l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class Country extends BaseEntity{

	@Basic(optional = false)
	@Column(name="name")
	private String name;

	@Basic(optional = false)
	@Column(name="iso_code_2")
	private String isoCode2;

	@Basic(optional = false)
	@Column(name="iso_code_3")
	private String isoCode3;

	@Column(name="address")
	private String address;

	@Column(name = "post_code")
	private Boolean postCode;

	@Column(name = "status")
	private String status;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CountryDescription> descriptions;

	public String getIsoCode2() {
		return isoCode2;
	}

	public void setIsoCode2(String isoCode2) {
		this.isoCode2 = isoCode2;
	}
}
