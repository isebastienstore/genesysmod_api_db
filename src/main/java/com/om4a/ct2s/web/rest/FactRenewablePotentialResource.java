package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactRenewablePotential;
import com.om4a.ct2s.repository.FactRenewablePotentialRepository;
import com.om4a.ct2s.repository.search.FactRenewablePotentialSearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactRenewablePotential}.
 */
@RestController
@RequestMapping("/api/fact-renewable-potentials")
public class FactRenewablePotentialResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactRenewablePotentialResource.class);

    private static final String ENTITY_NAME = "factRenewablePotential";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactRenewablePotentialRepository factRenewablePotentialRepository;

    private final FactRenewablePotentialSearchRepository factRenewablePotentialSearchRepository;

    public FactRenewablePotentialResource(
        FactRenewablePotentialRepository factRenewablePotentialRepository,
        FactRenewablePotentialSearchRepository factRenewablePotentialSearchRepository
    ) {
        this.factRenewablePotentialRepository = factRenewablePotentialRepository;
        this.factRenewablePotentialSearchRepository = factRenewablePotentialSearchRepository;
    }

    /**
     * {@code POST  /fact-renewable-potentials} : Create a new factRenewablePotential.
     *
     * @param factRenewablePotential the factRenewablePotential to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factRenewablePotential, or with status {@code 400 (Bad Request)} if the factRenewablePotential has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactRenewablePotential> createFactRenewablePotential(
        @Valid @RequestBody FactRenewablePotential factRenewablePotential
    ) throws URISyntaxException {
        LOG.debug("REST request to save FactRenewablePotential : {}", factRenewablePotential);
        if (factRenewablePotential.getId() != null) {
            throw new BadRequestAlertException("A new factRenewablePotential cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialSearchRepository.index(factRenewablePotential);
        return ResponseEntity.created(new URI("/api/fact-renewable-potentials/" + factRenewablePotential.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factRenewablePotential.getId()))
            .body(factRenewablePotential);
    }

    /**
     * {@code PUT  /fact-renewable-potentials/:id} : Updates an existing factRenewablePotential.
     *
     * @param id the id of the factRenewablePotential to save.
     * @param factRenewablePotential the factRenewablePotential to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factRenewablePotential,
     * or with status {@code 400 (Bad Request)} if the factRenewablePotential is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factRenewablePotential couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactRenewablePotential> updateFactRenewablePotential(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactRenewablePotential factRenewablePotential
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactRenewablePotential : {}, {}", id, factRenewablePotential);
        if (factRenewablePotential.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factRenewablePotential.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factRenewablePotentialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialSearchRepository.index(factRenewablePotential);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factRenewablePotential.getId()))
            .body(factRenewablePotential);
    }

    /**
     * {@code PATCH  /fact-renewable-potentials/:id} : Partial updates given fields of an existing factRenewablePotential, field will ignore if it is null
     *
     * @param id the id of the factRenewablePotential to save.
     * @param factRenewablePotential the factRenewablePotential to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factRenewablePotential,
     * or with status {@code 400 (Bad Request)} if the factRenewablePotential is not valid,
     * or with status {@code 404 (Not Found)} if the factRenewablePotential is not found,
     * or with status {@code 500 (Internal Server Error)} if the factRenewablePotential couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactRenewablePotential> partialUpdateFactRenewablePotential(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactRenewablePotential factRenewablePotential
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactRenewablePotential partially : {}, {}", id, factRenewablePotential);
        if (factRenewablePotential.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factRenewablePotential.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factRenewablePotentialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactRenewablePotential> result = factRenewablePotentialRepository
            .findById(factRenewablePotential.getId())
            .map(existingFactRenewablePotential -> {
                if (factRenewablePotential.getMaxCapacity() != null) {
                    existingFactRenewablePotential.setMaxCapacity(factRenewablePotential.getMaxCapacity());
                }
                if (factRenewablePotential.getAvailableCapacity() != null) {
                    existingFactRenewablePotential.setAvailableCapacity(factRenewablePotential.getAvailableCapacity());
                }
                if (factRenewablePotential.getMinCapacity() != null) {
                    existingFactRenewablePotential.setMinCapacity(factRenewablePotential.getMinCapacity());
                }

                return existingFactRenewablePotential;
            })
            .map(factRenewablePotentialRepository::save)
            .map(savedFactRenewablePotential -> {
                factRenewablePotentialSearchRepository.index(savedFactRenewablePotential);
                return savedFactRenewablePotential;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factRenewablePotential.getId())
        );
    }

    /**
     * {@code GET  /fact-renewable-potentials} : get all the factRenewablePotentials.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factRenewablePotentials in body.
     */
    @GetMapping("")
    public List<FactRenewablePotential> getAllFactRenewablePotentials() {
        LOG.debug("REST request to get all FactRenewablePotentials");
        return factRenewablePotentialRepository.findAll();
    }

    /**
     * {@code GET  /fact-renewable-potentials/:id} : get the "id" factRenewablePotential.
     *
     * @param id the id of the factRenewablePotential to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factRenewablePotential, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactRenewablePotential> getFactRenewablePotential(@PathVariable("id") String id) {
        LOG.debug("REST request to get FactRenewablePotential : {}", id);
        Optional<FactRenewablePotential> factRenewablePotential = factRenewablePotentialRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factRenewablePotential);
    }

    /**
     * {@code DELETE  /fact-renewable-potentials/:id} : delete the "id" factRenewablePotential.
     *
     * @param id the id of the factRenewablePotential to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactRenewablePotential(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactRenewablePotential : {}", id);
        factRenewablePotentialRepository.deleteById(id);
        factRenewablePotentialSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fact-renewable-potentials/_search?query=:query} : search for the factRenewablePotential corresponding
     * to the query.
     *
     * @param query the query of the factRenewablePotential search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactRenewablePotential> searchFactRenewablePotentials(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactRenewablePotentials for query {}", query);
        try {
            return StreamSupport.stream(factRenewablePotentialSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
