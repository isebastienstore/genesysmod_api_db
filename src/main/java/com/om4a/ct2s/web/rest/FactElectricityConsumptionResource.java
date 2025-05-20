package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactElectricityConsumption;
import com.om4a.ct2s.repository.FactElectricityConsumptionRepository;
import com.om4a.ct2s.repository.search.FactElectricityConsumptionSearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactElectricityConsumption}.
 */
@RestController
@RequestMapping("/api/fact-electricity-consumptions")
public class FactElectricityConsumptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactElectricityConsumptionResource.class);

    private static final String ENTITY_NAME = "factElectricityConsumption";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactElectricityConsumptionRepository factElectricityConsumptionRepository;

    private final FactElectricityConsumptionSearchRepository factElectricityConsumptionSearchRepository;

    public FactElectricityConsumptionResource(
        FactElectricityConsumptionRepository factElectricityConsumptionRepository,
        FactElectricityConsumptionSearchRepository factElectricityConsumptionSearchRepository
    ) {
        this.factElectricityConsumptionRepository = factElectricityConsumptionRepository;
        this.factElectricityConsumptionSearchRepository = factElectricityConsumptionSearchRepository;
    }

    /**
     * {@code POST  /fact-electricity-consumptions} : Create a new factElectricityConsumption.
     *
     * @param factElectricityConsumption the factElectricityConsumption to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factElectricityConsumption, or with status {@code 400 (Bad Request)} if the factElectricityConsumption has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactElectricityConsumption> createFactElectricityConsumption(
        @Valid @RequestBody FactElectricityConsumption factElectricityConsumption
    ) throws URISyntaxException {
        LOG.debug("REST request to save FactElectricityConsumption : {}", factElectricityConsumption);
        if (factElectricityConsumption.getId() != null) {
            throw new BadRequestAlertException("A new factElectricityConsumption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);
        factElectricityConsumptionSearchRepository.index(factElectricityConsumption);
        return ResponseEntity.created(new URI("/api/fact-electricity-consumptions/" + factElectricityConsumption.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factElectricityConsumption.getId()))
            .body(factElectricityConsumption);
    }

    /**
     * {@code PUT  /fact-electricity-consumptions/:id} : Updates an existing factElectricityConsumption.
     *
     * @param id the id of the factElectricityConsumption to save.
     * @param factElectricityConsumption the factElectricityConsumption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factElectricityConsumption,
     * or with status {@code 400 (Bad Request)} if the factElectricityConsumption is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factElectricityConsumption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactElectricityConsumption> updateFactElectricityConsumption(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactElectricityConsumption factElectricityConsumption
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactElectricityConsumption : {}, {}", id, factElectricityConsumption);
        if (factElectricityConsumption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factElectricityConsumption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factElectricityConsumptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);
        factElectricityConsumptionSearchRepository.index(factElectricityConsumption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factElectricityConsumption.getId()))
            .body(factElectricityConsumption);
    }

    /**
     * {@code PATCH  /fact-electricity-consumptions/:id} : Partial updates given fields of an existing factElectricityConsumption, field will ignore if it is null
     *
     * @param id the id of the factElectricityConsumption to save.
     * @param factElectricityConsumption the factElectricityConsumption to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factElectricityConsumption,
     * or with status {@code 400 (Bad Request)} if the factElectricityConsumption is not valid,
     * or with status {@code 404 (Not Found)} if the factElectricityConsumption is not found,
     * or with status {@code 500 (Internal Server Error)} if the factElectricityConsumption couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactElectricityConsumption> partialUpdateFactElectricityConsumption(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactElectricityConsumption factElectricityConsumption
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactElectricityConsumption partially : {}, {}", id, factElectricityConsumption);
        if (factElectricityConsumption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factElectricityConsumption.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factElectricityConsumptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactElectricityConsumption> result = factElectricityConsumptionRepository
            .findById(factElectricityConsumption.getId())
            .map(existingFactElectricityConsumption -> {
                if (factElectricityConsumption.getValue() != null) {
                    existingFactElectricityConsumption.setValue(factElectricityConsumption.getValue());
                }

                return existingFactElectricityConsumption;
            })
            .map(factElectricityConsumptionRepository::save)
            .map(savedFactElectricityConsumption -> {
                factElectricityConsumptionSearchRepository.index(savedFactElectricityConsumption);
                return savedFactElectricityConsumption;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factElectricityConsumption.getId())
        );
    }

    /**
     * {@code GET  /fact-electricity-consumptions} : get all the factElectricityConsumptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factElectricityConsumptions in body.
     */
    @GetMapping("")
    public List<FactElectricityConsumption> getAllFactElectricityConsumptions() {
        LOG.debug("REST request to get all FactElectricityConsumptions");
        return factElectricityConsumptionRepository.findAll();
    }

    /**
     * {@code GET  /fact-electricity-consumptions/:id} : get the "id" factElectricityConsumption.
     *
     * @param id the id of the factElectricityConsumption to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factElectricityConsumption, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactElectricityConsumption> getFactElectricityConsumption(@PathVariable("id") String id) {
        LOG.debug("REST request to get FactElectricityConsumption : {}", id);
        Optional<FactElectricityConsumption> factElectricityConsumption = factElectricityConsumptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factElectricityConsumption);
    }

    /**
     * {@code DELETE  /fact-electricity-consumptions/:id} : delete the "id" factElectricityConsumption.
     *
     * @param id the id of the factElectricityConsumption to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactElectricityConsumption(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactElectricityConsumption : {}", id);
        factElectricityConsumptionRepository.deleteById(id);
        factElectricityConsumptionSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fact-electricity-consumptions/_search?query=:query} : search for the factElectricityConsumption corresponding
     * to the query.
     *
     * @param query the query of the factElectricityConsumption search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactElectricityConsumption> searchFactElectricityConsumptions(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactElectricityConsumptions for query {}", query);
        try {
            return StreamSupport.stream(factElectricityConsumptionSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
