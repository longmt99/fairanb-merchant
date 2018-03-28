package com.fairanb.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


import com.fairanb.common.JConstants;
import com.fairanb.common.JsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;


@ToString
@EqualsAndHashCode
@Getter
@Setter
@Data
public class DiscountDefinitionRequest {

	@ApiModelProperty(position = 1)
	private Long id;

	private String name;

	private String description;

	private String message;

	private Short priority;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JConstants.DATE_FORMAT)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JConstants.DATE_FORMAT)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;

	private Long merchantId;

	protected Long modifiedBy;
}
