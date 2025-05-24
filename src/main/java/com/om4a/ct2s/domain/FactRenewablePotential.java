package com.om4a.ct2s.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FactRenewablePotential.
 */
@Document(collection = "fact_renewable_potential")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "factrenewablepotential")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactRenewablePotential implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("max_capacity")
    private Double maxCapacity;

    @NotNull
    @Field("available_capacity")
    private Double availableCapacity;

    @NotNull
    @Field("min_capacity")
    private Double minCapacity;

    @NotNull
    @DBRef
    @Field("country")
    private Country country;

    @NotNull
    @DBRef
    @Field("year")
    private Year year;

    @NotNull
    @DBRef
    @Field("technology")
    private Technology technology;

    @DBRef
    @Field("metadata")
    private Metadata metadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FactRenewablePotential id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMaxCapacity() {
        return this.maxCapacity;
    }

    public FactRenewablePotential maxCapacity(Double maxCapacity) {
        this.setMaxCapacity(maxCapacity);
        return this;
    }

    public void setMaxCapacity(Double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Double getAvailableCapacity() {
        return this.availableCapacity;
    }

    public FactRenewablePotential availableCapacity(Double availableCapacity) {
        this.setAvailableCapacity(availableCapacity);
        return this;
    }

    public void setAvailableCapacity(Double availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public Double getMinCapacity() {
        return this.minCapacity;
    }

    public FactRenewablePotential minCapacity(Double minCapacity) {
        this.setMinCapacity(minCapacity);
        return this;
    }

    public void setMinCapacity(Double minCapacity) {
        this.minCapacity = minCapacity;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public FactRenewablePotential country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Year getYear() {
        return this.year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public FactRenewablePotential year(Year year) {
        this.setYear(year);
        return this;
    }

    public Technology getTechnology() {
        return this.technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public FactRenewablePotential technology(Technology technology) {
        this.setTechnology(technology);
        return this;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public FactRenewablePotential metadata(Metadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactRenewablePotential)) {
            return false;
        }
        return getId() != null && getId().equals(((FactRenewablePotential) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactRenewablePotential{" +
            "id=" + getId() +
            ", maxCapacity=" + getMaxCapacity() +
            ", availableCapacity=" + getAvailableCapacity() +
            ", minCapacity=" + getMinCapacity() +
            "}";
    }
}
