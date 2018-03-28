package com.fairanb.model;


import com.fairanb.model.response.LanguageResponse;
import com.fairanb.model.response.MerchantResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the merchant database table.
 * 
 */
@Entity
@Table(name="merchant")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class Merchant extends BaseEntity {

	@Basic(optional = false)
	@Column(name = "email")
	private String email;

	@Basic(optional = false)
	@Column(name = "phone")
	private String phone;

	@Column(name = "domain_name")
	private String domainName;

	@Column(name = "name")
	private String name;

	@Column(name = "logo")
	private String logo;

	@Column(name = "country_id")
	private Long countryId;

	@Column(name = "address_id")
	private Long addressId;

	@Column(name = "currency_id")
	private Long currencyId;


	@Column(name = "size_unit")
	private String sizeUnit;

	@Column(name = "weight_unit")
	private String weightUnit;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "business_since")
	private Date businessSince;

	@Column(name = "status")
	private String status;


	@Column(name = "deleted")
	private Boolean deleted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at")
	private Date deletedAt;
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany()
	@JoinTable(name = "merchant_language", joinColumns = {@JoinColumn(name = "merchant_id")}, inverseJoinColumns = {
			@JoinColumn(name = "language_id")})
	private List<Language> languages;

	public MerchantResponse getResponse() {
		MerchantResponse response = new MerchantResponse();
		List<LanguageResponse> languageList = null;
		if(languages!=null){
			languageList = new ArrayList<>(languages.size());
			for (Language language : languages) {
				languageList.add(language.getResponse());
			}
			// Set null temporary to skip copy
			this.setLanguages(null);
		}
		BeanUtils.copyProperties(this, response);
		response.setLanguages(languageList);
		return response;
	}

	public List<Language> getLanguages() {
		return languages;
	}

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}
}
