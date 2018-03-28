package com.fairanb.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fairanb.common.JConstants;
import com.fairanb.common.JsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;


@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class PromoCodeRequest {

	@ApiModelProperty(position = 1)
	private Long id;

	private String code;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JConstants.DATE_FORMAT)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JConstants.DATE_FORMAT)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;

	protected Long modifiedBy;

}
