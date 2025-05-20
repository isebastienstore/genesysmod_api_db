package com.om4a.ct2s.domain;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Year.
 */
@Document(collection = "year")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "year")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Year implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Unique
    @NotNull
    @Min(value = 1900)
    @Field("year")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer year;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Year id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getYear() {
        return this.year;
    }

    public Year year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Year)) {
            return false;
        }
        return getId() != null && getId().equals(((Year) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Year{" +
            "id=" + getId() +
            ", year=" + getYear() +
            "}";
    }
}
