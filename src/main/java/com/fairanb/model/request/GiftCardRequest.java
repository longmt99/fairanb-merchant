package com.fairanb.model.request;

import com.fairanb.common.JConstants;
import com.fairanb.common.JsonDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCardRequest {
	@NotNull(message = "'code' is required")
	private String code;

	@NotNull(message = "'merchantId' is required")
	private Long merchantId;

	@NotNull(message = "'name' is required")
	private String name;

	private String description;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JConstants.DATE_FORMAT)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	protected Date startDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = JConstants.DATE_FORMAT)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	protected Date endDate;

	private BigDecimal balance;

	private BigDecimal purchaseAmount;

	@NotNull(message = "'securityCode' is required")
	private String securityCode;

	private String status;
}
