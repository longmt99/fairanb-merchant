package com.fairanb.model;

import com.fairanb.model.response.PromoCodeResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "promo_code")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class PromoCode extends BaseEntity {

	@Basic(optional = false)
	@Column(name = "code")
	private String code;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "status")
	private String status;

	public PromoCodeResponse getResponse() {
		PromoCodeResponse response = new PromoCodeResponse();
		BeanUtils.copyProperties(this, response);
		return response;
	}

}
