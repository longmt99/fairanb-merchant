package com.fairanb.model.request;

import com.fairanb.model.response.ManufacturerDescriptionResponse;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
public class ManufacturerRequest implements Serializable {
    private Long id;
    private String name;
    private Long merchantId;
    private Long customerId;
    private Integer sortOrder;
    private String imageUrl;
    private String status;
    private Long createdBy;
    private Date createdDate;
    private Long modifiedBy;
    private Date modifiedDate;
    private List<ManufacturerDescriptionResponse> descriptions;
}
