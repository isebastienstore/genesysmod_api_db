package com.om4a.ct2s.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Metadata.
 */
@Document(collection = "metadata")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "metadata")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Field("updated_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String updatedBy;

    @Field("last_access_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private ZonedDateTime lastAccessAt;

    @Field("last_access_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastAccessBy;

    @Field("created_at")
    private ZonedDateTime createdAt;

    @Field("updated_at")
    private ZonedDateTime updatedAt;

    @Field("source")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String source;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Metadata id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ZonedDateTime getLastAccessAt() {
        return lastAccessAt;
    }

    public void setLastAccessAt(ZonedDateTime lastAccessAt) {
        this.lastAccessAt = lastAccessAt;
    }

    public String getLastAccessBy() {
        return lastAccessBy;
    }

    public void setLastAccessBy(String lastAccessBy) {
        this.lastAccessBy = lastAccessBy;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Metadata createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public Metadata updatedBy(String updatedBy) {
        this.setUpdatedBy(updatedBy);
        return this;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public Metadata createdAt(ZonedDateTime createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Metadata updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSource() {
        return this.source;
    }

    public Metadata source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Metadata)) {
            return false;
        }
        return getId() != null && getId().equals(((Metadata) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Metadata{" +
            "id='" +
            id +
            '\'' +
            ", createdBy='" +
            createdBy +
            '\'' +
            ", updatedBy='" +
            updatedBy +
            '\'' +
            ", lastAccessAt=" +
            lastAccessAt +
            ", lastAccessBy='" +
            lastAccessBy +
            '\'' +
            ", createdAt=" +
            createdAt +
            ", updatedAt=" +
            updatedAt +
            ", source='" +
            source +
            '\'' +
            '}'
        );
    }
}
