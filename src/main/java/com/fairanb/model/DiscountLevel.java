package com.fairanb.model;

import com.fairanb.model.response.DiscountLevelResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "discount_level")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class DiscountLevel implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;
	
	private DiscountLevelResponse getResponse() {
		DiscountLevelResponse response = new DiscountLevelResponse();
		BeanUtils.copyProperties(this, response);
		return response;
	}
}
