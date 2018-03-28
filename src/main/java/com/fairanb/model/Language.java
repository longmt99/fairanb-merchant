package com.fairanb.model;

import com.fairanb.model.response.LanguageResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @longmt99
 */
@Entity
@Table(name="language")
@NamedQuery(name="Language.findAll", query="SELECT l FROM Language l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class Language extends BaseEntity{

	@Column(name="name")
	private String name;

	public Language() {
	}

	public LanguageResponse getResponse() {
		LanguageResponse response = new LanguageResponse();
		BeanUtils.copyProperties(this, response);
		return response;
	}
	
	
}
