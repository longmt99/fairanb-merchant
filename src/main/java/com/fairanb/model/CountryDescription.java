package com.fairanb.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="country_description")
@NamedQuery(name="CountryDescription.findAll", query="SELECT l FROM CountryDescription l")
@EqualsAndHashCode
@Getter
@Setter
@Data
public class CountryDescription extends BaseEntity {

    @ManyToOne(targetEntity = Country.class)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(targetEntity = Language.class)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name="name")
    private String name;

    @Column(name="native_name")
    private String nativeName;

    @Column(name="description")
    private String description;
}
