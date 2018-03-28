package com.fairanb.model.response;

import com.fairanb.model.request.MerchantRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Setter
@Getter
public class ManufacturerResponse implements Serializable {
    private Long id;
    private String name;
    private MerchantRequest merchant;
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
