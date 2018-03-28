package com.fairanb.model.request;

import com.fairanb.model.Language;
import lombok.*;
import org.springframework.beans.BeanUtils;

/**
 * @longmt99
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class LanguageRequest  {
	private Long id;
	private String name;
	public LanguageRequest() {
	}

	public Language copyBean(LanguageRequest request) {
		Language language = new Language();
		BeanUtils.copyProperties(request, language);
		return language;
	}

	public LanguageRequest copyRequest(Language language) {
		BeanUtils.copyProperties(language, this);
		return this;
	}
}
