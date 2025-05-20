package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactPowerProduction;
import com.om4a.ct2s.repository.FactPowerProductionRepository;
import com.om4a.ct2s.repository.search.FactPowerProductionSearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactPowerProduction}.
 */
@RestController
@RequestMapping("/api/fact-power-productions")
public class FactPowerProductionResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactPowerProductionResource.class);

    private static final String ENTITY_NAME = "factPowerProduction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactPowerProductionRepository factPowerProductionRepository;

    private final FactPowerProductionSearchRepository factPowerProductionSearchRepository;

    public FactPowerProductionResource(
        FactPowerProductionRepository factPowerProductionRepository,
        FactPowerProductionSearchRepository factPowerProductionSearchRepository
    ) {
        this.factPowerProductionRepository = factPowerProductionRepository;
        this.factPowerProductionSearchRepository = factPowerProductionSearchRepository;
    }

    /**
     * {@code POST  /fact-power-productions} : Create a new factPowerProduction.
     *
     * @param factPowerProduction the factPowerProduction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factPowerProduction, or with status {@code 400 (Bad Request)} if the factPowerProduction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactPowerProduction> createFactPowerProduction(@Valid @RequestBody FactPowerProduction factPowerProduction)
        throws URISyntaxException {
        LOG.debug("REST request to save FactPowerProduction : {}", factPowerProduction);
        if (factPowerProduction.getId() != null) {
            throw new BadRequestAlertException("A new factPowerProduction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factPowerProduction = factPowerProductionRepository.save(factPowerProduction);
        factPowerProductionSearchRepository.index(factPowerProduction);
        return ResponseEntity.created(new URI("/api/fact-power-productions/" + factPowerProduction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factPowerProduction.getId()))
            .body(factPowerProduction);
    }

    /**
     * {@code PUT  /fact-power-productions/:id} : Updates an existing factPowerProduction.
     *
     * @param id the id of the factPowerProduction to save.
     * @param factPowerProduction the factPowerProduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factPowerProduction,
     * or with status {@code 400 (Bad Request)} if the factPowerProduction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factPowerProduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactPowerProduction> updateFactPowerProduction(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactPowerProduction factPowerProduction
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactPowerProduction : {}, {}", id, factPowerProduction);
        if (factPowerProduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factPowerProduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factPowerProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factPowerProduction = factPowerProductionRepository.save(factPowerProduction);
        factPowerProductionSearchRepository.index(factPowerProduction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factPowerProduction.getId()))
            .body(factPowerProduction);
    }

    /**
     * {@code PATCH  /fact-power-productions/:id} : Partial updates given fields of an existing factPowerProduction, field will ignore if it is null
     *
     * @param id the id of the factPowerProduction to save.
     * @param factPowerProduction the factPowerProduction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factPowerProduction,
     * or with status {@code 400 (Bad Request)} if the factPowerProduction is not valid,
     * or with status {@code 404 (Not Found)} if the factPowerProduction is not found,
     * or with status {@code 500 (Internal Server Error)} if the factPowerProduction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactPowerProduction> partialUpdateFactPowerProduction(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactPowerProduction factPowerProduction
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactPowerProduction partially : {}, {}", id, factPowerProduction);
        if (factPowerProduction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factPowerProduction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factPowerProductionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactPowerProduction> result = factPowerProductionRepository
            .findById(factPowerProduction.getId())
            .map(existingFactPowerProduction -> {
                if (factPowerProduction.getValue() != null) {
                    existingFactPowerProduction.setValue(factPowerProduction.getValue());
                }

                return existingFactPowerProduction;
            })
            .map(factPowerProductionRepository::save)
            .map(savedFactPowerProduction -> {
                factPowerProductionSearchRepository.index(savedFactPowerProduction);
                return savedFactPowerProduction;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factPowerProduction.getId())
        );
    }

    /**
     * {@code GET  /fact-power-productions} : get all the factPowerProductions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factPowerProductions in body.
     */
    @GetMapping("")
    public List<FactPowerProduction> getAllFactPowerProductions() {
        LOG.debug("REST request to get all FactPowerProductions");
        return factPowerProductionRepository.findAll();
    }

    /**
     * {@code GET  /fact-power-productions/:id} : get the "id" factPowerProduction.
     *
     * @param id the id of the factPowerProduction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factPowerProduction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactPowerProduction> getFactPowerProduction(@PathVariable("id") String id) {
        LOG.debug("REST request to get FactPowerProduction : {}", id);
        Optional<FactPowerProduction> factPowerProduction = factPowerProductionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factPowerProduction);
    }

    /**
     * {@code DELETE  /fact-power-productions/:id} : delete the "id" factPowerProduction.
     *
     * @param id the id of the factPowerProduction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactPowerProduction(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactPowerProduction : {}", id);
        factPowerProductionRepository.deleteById(id);
        factPowerProductionSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fact-power-productions/_search?query=:query} : search for the factPowerProduction corresponding
     * to the query.
     *
     * @param query the query of the factPowerProduction search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactPowerProduction> searchFactPowerProductions(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactPowerProductions for query {}", query);
        try {
            return StreamSupport.stream(factPowerProductionSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
