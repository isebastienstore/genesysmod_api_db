package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.Metadata;
import com.om4a.ct2s.repository.MetadataRepository;
import com.om4a.ct2s.repository.search.MetadataSearchRepository;
import com.om4a.ct2s.web.rest.errors.BadRequestAlertException;
import com.om4a.ct2s.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.om4a.ct2s.domain.Metadata}.
 */
@RestController
@RequestMapping("/api/metadata")
public class MetadataResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataResource.class);

    private static final String ENTITY_NAME = "metadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetadataRepository metadataRepository;

    private final MetadataSearchRepository metadataSearchRepository;

    public MetadataResource(MetadataRepository metadataRepository, MetadataSearchRepository metadataSearchRepository) {
        this.metadataRepository = metadataRepository;
        this.metadataSearchRepository = metadataSearchRepository;
    }

    /**
     * {@code POST  /metadata} : Create a new metadata.
     *
     * @param metadata the metadata to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metadata, or with status {@code 400 (Bad Request)} if the metadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Metadata> createMetadata(@RequestBody Metadata metadata) throws URISyntaxException {
        LOG.debug("REST request to save Metadata : {}", metadata);
        if (metadata.getId() != null) {
            throw new BadRequestAlertException("A new metadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metadata = metadataRepository.save(metadata);
        metadataSearchRepository.index(metadata);
        return ResponseEntity.created(new URI("/api/metadata/" + metadata.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, metadata.getId()))
            .body(metadata);
    }

    /**
     * {@code PUT  /metadata/:id} : Updates an existing metadata.
     *
     * @param id the id of the metadata to save.
     * @param metadata the metadata to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metadata,
     * or with status {@code 400 (Bad Request)} if the metadata is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metadata couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Metadata> updateMetadata(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Metadata metadata
    ) throws URISyntaxException {
        LOG.debug("REST request to update Metadata : {}, {}", id, metadata);
        if (metadata.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metadata.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metadata = metadataRepository.save(metadata);
        metadataSearchRepository.index(metadata);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, metadata.getId()))
            .body(metadata);
    }

    /**
     * {@code PATCH  /metadata/:id} : Partial updates given fields of an existing metadata, field will ignore if it is null
     *
     * @param id the id of the metadata to save.
     * @param metadata the metadata to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metadata,
     * or with status {@code 400 (Bad Request)} if the metadata is not valid,
     * or with status {@code 404 (Not Found)} if the metadata is not found,
     * or with status {@code 500 (Internal Server Error)} if the metadata couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Metadata> partialUpdateMetadata(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Metadata metadata
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Metadata partially : {}, {}", id, metadata);
        if (metadata.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metadata.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Metadata> result = metadataRepository
            .findById(metadata.getId())
            .map(existingMetadata -> {
                if (metadata.getCreatedBy() != null) {
                    existingMetadata.setCreatedBy(metadata.getCreatedBy());
                }
                if (metadata.getUpdatedBy() != null) {
                    existingMetadata.setUpdatedBy(metadata.getUpdatedBy());
                }
                if (metadata.getAction() != null) {
                    existingMetadata.setAction(metadata.getAction());
                }
                if (metadata.getCreatedAt() != null) {
                    existingMetadata.setCreatedAt(metadata.getCreatedAt());
                }
                if (metadata.getUpdatedAt() != null) {
                    existingMetadata.setUpdatedAt(metadata.getUpdatedAt());
                }
                if (metadata.getSource() != null) {
                    existingMetadata.setSource(metadata.getSource());
                }

                return existingMetadata;
            })
            .map(metadataRepository::save)
            .map(savedMetadata -> {
                metadataSearchRepository.index(savedMetadata);
                return savedMetadata;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, metadata.getId())
        );
    }

    /**
     * {@code GET  /metadata} : get all the metadata.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metadata in body.
     */
    @GetMapping("")
    public List<Metadata> getAllMetadata() {
        LOG.debug("REST request to get all Metadata");
        return metadataRepository.findAll();
    }

    /**
     * {@code GET  /metadata/:id} : get the "id" metadata.
     *
     * @param id the id of the metadata to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metadata, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Metadata> getMetadata(@PathVariable("id") String id) {
        LOG.debug("REST request to get Metadata : {}", id);
        Optional<Metadata> metadata = metadataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(metadata);
    }

    /**
     * {@code DELETE  /metadata/:id} : delete the "id" metadata.
     *
     * @param id the id of the metadata to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetadata(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Metadata : {}", id);
        metadataRepository.deleteById(id);
        metadataSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /metadata/_search?query=:query} : search for the metadata corresponding
     * to the query.
     *
     * @param query the query of the metadata search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Metadata> searchMetadata(@RequestParam("query") String query) {
        LOG.debug("REST request to search Metadata for query {}", query);
        try {
            return StreamSupport.stream(metadataSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
