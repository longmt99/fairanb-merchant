package com.fairanb.model.request;

import com.fairanb.model.Language;
import com.fairanb.model.Merchant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Longmt99
 */
@EqualsAndHashCode
@Getter
@Setter
@Data
public class MerchantRequest  {
	@ApiModelProperty(position =1)
	private Long id;
	@ApiModelProperty(position =2)
	private String email;
	@ApiModelProperty(position =3)
	private String phone;

	private String domainName;

	private String name;

	private String logo;

	private Long countryId;


	private Long addressId;

	private Long currencyId;


	private String sizeUnit;


	private String weightUnit;

	@Temporal(TemporalType.TIMESTAMP)
	private Date businessSince;

	private String status;

	private Boolean deleted;

	@Temporal(TemporalType.TIMESTAMP)

	private Date deletedAt;


	private List<LanguageRequest> languages;


	public MerchantRequest() {
	}

	public Merchant copyBean(MerchantRequest request) {
		Merchant merchant = new Merchant();

		List<LanguageRequest> requestLanguages = request.getLanguages();
		List<Language> languageList = null;
		if(requestLanguages!=null){
			languageList = new ArrayList<>(requestLanguages.size());
			for (LanguageRequest language : requestLanguages) {
				languageList.add(language.copyBean(language));
			}
		}
		request.setLanguages(null);
		BeanUtils.copyProperties(request, merchant);
		merchant.setLanguages(languageList);
		return merchant;
	}
}
