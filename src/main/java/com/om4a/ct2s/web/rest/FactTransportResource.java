package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactTransport;
import com.om4a.ct2s.repository.FactTransportRepository;
import com.om4a.ct2s.repository.search.FactTransportSearchRepository;
import com.om4a.ct2s.web.rest.errors.BadRequestAlertException;
import com.om4a.ct2s.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactTransport}.
 */
@RestController
@RequestMapping("/api/fact-transports")
public class FactTransportResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactTransportResource.class);

    private static final String ENTITY_NAME = "factTransport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactTransportRepository factTransportRepository;

    private final FactTransportSearchRepository factTransportSearchRepository;

    public FactTransportResource(
        FactTransportRepository factTransportRepository,
        FactTransportSearchRepository factTransportSearchRepository
    ) {
        this.factTransportRepository = factTransportRepository;
        this.factTransportSearchRepository = factTransportSearchRepository;
    }

    /**
     * {@code POST  /fact-transports} : Create a new factTransport.
     *
     * @param factTransport the factTransport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factTransport, or with status {@code 400 (Bad Request)} if the factTransport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactTransport> createFactTransport(@Valid @RequestBody FactTransport factTransport) throws URISyntaxException {
        LOG.debug("REST request to save FactTransport : {}", factTransport);
        if (factTransport.getId() != null) {
            throw new BadRequestAlertException("A new factTransport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factTransport = factTransportRepository.save(factTransport);
        factTransportSearchRepository.index(factTransport);
        return ResponseEntity.created(new URI("/api/fact-transports/" + factTransport.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factTransport.getId()))
            .body(factTransport);
    }

    /**
     * {@code PUT  /fact-transports/:id} : Updates an existing factTransport.
     *
     * @param id the id of the factTransport to save.
     * @param factTransport the factTransport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factTransport,
     * or with status {@code 400 (Bad Request)} if the factTransport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factTransport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactTransport> updateFactTransport(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactTransport factTransport
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactTransport : {}, {}", id, factTransport);
        if (factTransport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factTransport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factTransportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factTransport = factTransportRepository.save(factTransport);
        factTransportSearchRepository.index(factTransport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTransport.getId()))
            .body(factTransport);
    }

    /**
     * {@code PATCH  /fact-transports/:id} : Partial updates given fields of an existing factTransport, field will ignore if it is null
     *
     * @param id the id of the factTransport to save.
     * @param factTransport the factTransport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factTransport,
     * or with status {@code 400 (Bad Request)} if the factTransport is not valid,
     * or with status {@code 404 (Not Found)} if the factTransport is not found,
     * or with status {@code 500 (Internal Server Error)} if the factTransport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactTransport> partialUpdateFactTransport(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactTransport factTransport
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactTransport partially : {}, {}", id, factTransport);
        if (factTransport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factTransport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factTransportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactTransport> result = factTransportRepository
            .findById(factTransport.getId())
            .map(existingFactTransport -> {
                if (factTransport.getValue() != null) {
                    existingFactTransport.setValue(factTransport.getValue());
                }
                if (factTransport.getTypeOfMobility() != null) {
                    existingFactTransport.setTypeOfMobility(factTransport.getTypeOfMobility());
                }

                return existingFactTransport;
            })
            .map(factTransportRepository::save)
            .map(savedFactTransport -> {
                factTransportSearchRepository.index(savedFactTransport);
                return savedFactTransport;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTransport.getId())
        );
    }

    /**
     * {@code GET  /fact-transports} : get all the factTransports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factTransports in body.
     */
    @GetMapping("")
    public List<FactTransport> getAllFactTransports() {
        LOG.debug("REST request to get all FactTransports");
        return factTransportRepository.findAll();
    }

    /**
     * {@code GET  /fact-transports/:id} : get the "id" factTransport.
     *
     * @param id the id of the factTransport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factTransport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactTransport> getFactTransport(@PathVariable("id") String id) {
        LOG.debug("REST request to get FactTransport : {}", id);
        Optional<FactTransport> factTransport = factTransportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factTransport);
    }

    /**
     * {@code DELETE  /fact-transports/:id} : delete the "id" factTransport.
     *
     * @param id the id of the factTransport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactTransport(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactTransport : {}", id);
        factTransportRepository.deleteById(id);
        factTransportSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fact-transports/_search?query=:query} : search for the factTransport corresponding
     * to the query.
     *
     * @param query the query of the factTransport search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactTransport> searchFactTransports(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactTransports for query {}", query);
        try {
            return StreamSupport.stream(factTransportSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
