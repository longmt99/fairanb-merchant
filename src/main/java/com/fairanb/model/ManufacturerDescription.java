package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="manufacturer_description")
@NamedQuery(name="ManufacturerDescription.findAll", query="SELECT l FROM ManufacturerDescription l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class ManufacturerDescription {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    @ManyToOne(targetEntity = Manufacturer.class)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne(targetEntity = Language.class)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name="name")
    private String name;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="manufacturer_url")
    private String manufacturerUrl;

    @Column(name="created_by",updatable = false)
    @CreatedBy
    protected Long createdBy;

    @Column(name="created_date",updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected Date createdDate;

    @Column(name="modified_by")
    @LastModifiedBy
    protected Long modifiedBy;

    @Column(name="modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected Date modifiedDate;
}
