package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactTradeCapacity;
import com.om4a.ct2s.repository.FactTradeCapacityRepository;
import com.om4a.ct2s.repository.search.FactTradeCapacitySearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactTradeCapacity}.
 */
@RestController
@RequestMapping("/api/fact-trade-capacities")
public class FactTradeCapacityResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactTradeCapacityResource.class);

    private static final String ENTITY_NAME = "factTradeCapacity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactTradeCapacityRepository factTradeCapacityRepository;

    private final FactTradeCapacitySearchRepository factTradeCapacitySearchRepository;

    public FactTradeCapacityResource(
        FactTradeCapacityRepository factTradeCapacityRepository,
        FactTradeCapacitySearchRepository factTradeCapacitySearchRepository
    ) {
        this.factTradeCapacityRepository = factTradeCapacityRepository;
        this.factTradeCapacitySearchRepository = factTradeCapacitySearchRepository;
    }

    /**
     * {@code POST  /fact-trade-capacities} : Create a new factTradeCapacity.
     *
     * @param factTradeCapacity the factTradeCapacity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factTradeCapacity, or with status {@code 400 (Bad Request)} if the factTradeCapacity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactTradeCapacity> createFactTradeCapacity(@Valid @RequestBody FactTradeCapacity factTradeCapacity)
        throws URISyntaxException {
        LOG.debug("REST request to save FactTradeCapacity : {}", factTradeCapacity);
        if (factTradeCapacity.getId() != null) {
            throw new BadRequestAlertException("A new factTradeCapacity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacitySearchRepository.index(factTradeCapacity);
        return ResponseEntity.created(new URI("/api/fact-trade-capacities/" + factTradeCapacity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factTradeCapacity.getId()))
            .body(factTradeCapacity);
    }

    /**
     * {@code PUT  /fact-trade-capacities/:id} : Updates an existing factTradeCapacity.
     *
     * @param id the id of the factTradeCapacity to save.
     * @param factTradeCapacity the factTradeCapacity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factTradeCapacity,
     * or with status {@code 400 (Bad Request)} if the factTradeCapacity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factTradeCapacity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactTradeCapacity> updateFactTradeCapacity(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactTradeCapacity factTradeCapacity
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactTradeCapacity : {}, {}", id, factTradeCapacity);
        if (factTradeCapacity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factTradeCapacity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factTradeCapacityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacitySearchRepository.index(factTradeCapacity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTradeCapacity.getId()))
            .body(factTradeCapacity);
    }

    /**
     * {@code PATCH  /fact-trade-capacities/:id} : Partial updates given fields of an existing factTradeCapacity, field will ignore if it is null
     *
     * @param id the id of the factTradeCapacity to save.
     * @param factTradeCapacity the factTradeCapacity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factTradeCapacity,
     * or with status {@code 400 (Bad Request)} if the factTradeCapacity is not valid,
     * or with status {@code 404 (Not Found)} if the factTradeCapacity is not found,
     * or with status {@code 500 (Internal Server Error)} if the factTradeCapacity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactTradeCapacity> partialUpdateFactTradeCapacity(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactTradeCapacity factTradeCapacity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactTradeCapacity partially : {}, {}", id, factTradeCapacity);
        if (factTradeCapacity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factTradeCapacity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factTradeCapacityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactTradeCapacity> result = factTradeCapacityRepository
            .findById(factTradeCapacity.getId())
            .map(existingFactTradeCapacity -> {
                if (factTradeCapacity.getValue() != null) {
                    existingFactTradeCapacity.setValue(factTradeCapacity.getValue());
                }

                return existingFactTradeCapacity;
            })
            .map(factTradeCapacityRepository::save)
            .map(savedFactTradeCapacity -> {
                factTradeCapacitySearchRepository.index(savedFactTradeCapacity);
                return savedFactTradeCapacity;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTradeCapacity.getId())
        );
    }

    /**
     * {@code GET  /fact-trade-capacities} : get all the factTradeCapacities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factTradeCapacities in body.
     */
    @GetMapping("")
    public List<FactTradeCapacity> getAllFactTradeCapacities() {
        LOG.debug("REST request to get all FactTradeCapacities");
        return factTradeCapacityRepository.findAll();
    }

    /**
     * {@code GET  /fact-trade-capacities/:id} : get the "id" factTradeCapacity.
     *
     * @param id the id of the factTradeCapacity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factTradeCapacity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactTradeCapacity> getFactTradeCapacity(@PathVariable("id") String id) {
        LOG.debug("REST request to get FactTradeCapacity : {}", id);
        Optional<FactTradeCapacity> factTradeCapacity = factTradeCapacityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factTradeCapacity);
    }

    /**
     * {@code DELETE  /fact-trade-capacities/:id} : delete the "id" factTradeCapacity.
     *
     * @param id the id of the factTradeCapacity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactTradeCapacity(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactTradeCapacity : {}", id);
        factTradeCapacityRepository.deleteById(id);
        factTradeCapacitySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fact-trade-capacities/_search?query=:query} : search for the factTradeCapacity corresponding
     * to the query.
     *
     * @param query the query of the factTradeCapacity search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactTradeCapacity> searchFactTradeCapacities(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactTradeCapacities for query {}", query);
        try {
            return StreamSupport.stream(factTradeCapacitySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
