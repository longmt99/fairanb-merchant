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
import java.util.List;

@Entity
@Table(name="manufacturer")
@NamedQuery(name="Manufacturer.findAll", query="SELECT l FROM Manufacturer l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class Manufacturer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;

    @Column(name="name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Merchant.class, optional = true)
    @JoinColumn(name = "merchant_id", referencedColumnName = "id", updatable = true)
    private Merchant merchant;

    @Column(name="customer_id")
    private Long customerId;

    @Column(name="sort_order")
    private Integer sortOrder;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="status")
    private String status;

    @OneToMany(mappedBy = "manufacturer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManufacturerDescription> descriptions;

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
