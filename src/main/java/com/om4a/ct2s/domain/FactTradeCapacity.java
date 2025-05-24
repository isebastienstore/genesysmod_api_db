package com.om4a.ct2s.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FactTradeCapacity.
 */
@Document(collection = "fact_trade_capacity")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facttradecapacity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactTradeCapacity implements Serializable {

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
    @Field("country1")
    private Country country1;

    @NotNull
    @DBRef
    @Field("country2")
    private Country country2;

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

    public FactTradeCapacity id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public FactTradeCapacity value(Double value) {
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

    public FactTradeCapacity year(Year year) {
        this.setYear(year);
        return this;
    }

    public Country getCountry1() {
        return this.country1;
    }

    public void setCountry1(Country country) {
        this.country1 = country;
    }

    public FactTradeCapacity country1(Country country) {
        this.setCountry1(country);
        return this;
    }

    public Country getCountry2() {
        return this.country2;
    }

    public void setCountry2(Country country) {
        this.country2 = country;
    }

    public FactTradeCapacity country2(Country country) {
        this.setCountry2(country);
        return this;
    }

    public Fuel getFuel() {
        return this.fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public FactTradeCapacity fuel(Fuel fuel) {
        this.setFuel(fuel);
        return this;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public FactTradeCapacity metadata(Metadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactTradeCapacity)) {
            return false;
        }
        return getId() != null && getId().equals(((FactTradeCapacity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactTradeCapacity{" +
            "id=" + getId() +
            ", value=" + getValue() +
            "}";
    }
}
