package com.om4a.ct2s.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FactPowerProduction.
 */
@Document(collection = "fact_power_production")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "factpowerproduction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactPowerProduction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("value")
    private Double value;

    @NotNull
    @DBRef
    @Field("year")
    private Year year;

    @NotNull
    @DBRef
    @Field("country")
    private Country country;

    @NotNull
    @DBRef
    @Field("technology")
    private Technology technology;

    @NotNull
    @DBRef
    @Field("fuel")
    private Fuel fuel;

    @DBRef
    @Field("metadata")
    private Metadata metadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FactPowerProduction id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public FactPowerProduction value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Year getYear() {
        return this.year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public FactPowerProduction year(Year year) {
        this.setYear(year);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public FactPowerProduction country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Technology getTechnology() {
        return this.technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public FactPowerProduction technology(Technology technology) {
        this.setTechnology(technology);
        return this;
    }

    public Fuel getFuel() {
        return this.fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public FactPowerProduction fuel(Fuel fuel) {
        this.setFuel(fuel);
        return this;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public FactPowerProduction metadata(Metadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactPowerProduction)) {
            return false;
        }
        return getId() != null && getId().equals(((FactPowerProduction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactPowerProduction{" +
            "id=" + getId() +
            ", value=" + getValue() +
            "}";
    }
}
