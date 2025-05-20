package com.om4a.ct2s.domain;

import com.om4a.ct2s.domain.enumeration.ModalType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A FactTransport.
 */
@Document(collection = "fact_transport")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "facttransport")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FactTransport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("value")
    private Double value;

    @NotNull
    @Field("type_of_mobility")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ModalType typeOfMobility;

    @DBRef
    @Field("year")
    private Year year;

    @DBRef
    @Field("country")
    private Country country;

    @DBRef
    @Field("metadata")
    private Metadata metadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public FactTransport id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public FactTransport value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ModalType getTypeOfMobility() {
        return this.typeOfMobility;
    }

    public FactTransport typeOfMobility(ModalType typeOfMobility) {
        this.setTypeOfMobility(typeOfMobility);
        return this;
    }

    public void setTypeOfMobility(ModalType typeOfMobility) {
        this.typeOfMobility = typeOfMobility;
    }

    public Year getYear() {
        return this.year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public FactTransport year(Year year) {
        this.setYear(year);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public FactTransport country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public FactTransport metadata(Metadata metadata) {
        this.setMetadata(metadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FactTransport)) {
            return false;
        }
        return getId() != null && getId().equals(((FactTransport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FactTransport{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", typeOfMobility='" + getTypeOfMobility() + "'" +
            "}";
    }
}
