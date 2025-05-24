package com.om4a.ct2s.domain;

import com.om4a.ct2s.domain.enumeration.StatusType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FactPowerPlants.
 */
@Document(collection = "fact_power_plants")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "factpowerplants")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactPowerPlants implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull
    @Field("intalled_capacity")
    private Double intalledCapacity;

    @NotNull
    @Field("availability_capacity")
    private Double availabilityCapacity;

    @NotNull
    @Field("status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private StatusType status;

    @NotNull
    @DBRef
    @Field("commissioningDate")
    private Year commissioningDate;

    @NotNull
    @DBRef
    @Field("decommissioningDate")
    private Year decommissioningDate;

    @NotNull
    @DBRef
    @Field("country")
    private Country country;

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

    public FactPowerPlants id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FactPowerPlants name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getIntalledCapacity() {
        return this.intalledCapacity;
    }

    public FactPowerPlants intalledCapacity(Double intalledCapacity) {
        this.setIntalledCapacity(intalledCapacity);
        return this;
    }

    public void setIntalledCapacity(Double intalledCapacity) {
        this.intalledCapacity = intalledCapacity;
    }

    public Double getAvailabilityCapacity() {
        return this.availabilityCapacity;
    }

    public FactPowerPlants availabilityCapacity(Double availabilityCapacity) {
        this.setAvailabilityCapacity(availabilityCapacity);
        return this;
    }

    public void setAvailabilityCapacity(Double availabilityCapacity) {
        this.availabilityCapacity = availabilityCapacity;
    }

    public StatusType getStatus() {
        return this.status;
    }

    public FactPowerPlants status(StatusType status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public Year getCommissioningDate() {
        return this.commissioningDate;
    }

    public void setCommissioningDate(Year year) {
        this.commissioningDate = year;
    }

    public FactPowerPlants commissioningDate(Year year) {
        this.setCommissioningDate(year);
        return this;
    }

    public Year getDecommissioningDate() {
        return this.decommissioningDate;
    }

    public void setDecommissioningDate(Year year) {
        this.decommissioningDate = year;
    }

    public FactPowerPlants decommissioningDate(Year year) {
        this.setDecommissioningDate(year);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public FactPowerPlants country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Technology getTechnology() {
        return this.technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public FactPowerPlants technology(Technology technology) {
        this.setTechnology(technology);
        return this;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public FactPowerPlants metadata(Metadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactPowerPlants)) {
            return false;
        }
        return getId() != null && getId().equals(((FactPowerPlants) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactPowerPlants{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", intalledCapacity=" + getIntalledCapacity() +
            ", availabilityCapacity=" + getAvailabilityCapacity() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
