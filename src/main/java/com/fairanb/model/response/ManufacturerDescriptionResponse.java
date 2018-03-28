package com.fairanb.model.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@Setter
@Getter
public class ManufacturerDescriptionResponse implements Serializable {
    private Long id;

    private String language;

    private String name;

    private String title;

    private String description;

    private String manufacturerUrl;
}
