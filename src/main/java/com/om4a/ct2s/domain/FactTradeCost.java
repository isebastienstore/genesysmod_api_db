package com.om4a.ct2s.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FactTradeCost.
 */
@Document(collection = "fact_trade_cost")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facttradecost")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactTradeCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("fixed_cost")
    private Double fixedCost;

    @NotNull
    @Field("variable_cost")
    private Double variableCost;

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

    public FactTradeCost id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getFixedCost() {
        return this.fixedCost;
    }

    public FactTradeCost fixedCost(Double fixedCost) {
        this.setFixedCost(fixedCost);
        return this;
    }

    public void setFixedCost(Double fixedCost) {
        this.fixedCost = fixedCost;
    }

    public Double getVariableCost() {
        return this.variableCost;
    }

    public FactTradeCost variableCost(Double variableCost) {
        this.setVariableCost(variableCost);
        return this;
    }

    public void setVariableCost(Double variableCost) {
        this.variableCost = variableCost;
    }

    public Fuel getFuel() {
        return this.fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public FactTradeCost fuel(Fuel fuel) {
        this.setFuel(fuel);
        return this;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public FactTradeCost metadata(Metadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactTradeCost)) {
            return false;
        }
        return getId() != null && getId().equals(((FactTradeCost) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactTradeCost{" +
            "id=" + getId() +
            ", fixedCost=" + getFixedCost() +
            ", variableCost=" + getVariableCost() +
            "}";
    }
}
