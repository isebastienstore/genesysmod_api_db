package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactPowerPlants;
import com.om4a.ct2s.repository.FactPowerPlantsRepository;
import com.om4a.ct2s.repository.search.FactPowerPlantsSearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactPowerPlants}.
 */
@RestController
@RequestMapping("/api/fact-power-plants")
public class FactPowerPlantsResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactPowerPlantsResource.class);

    private static final String ENTITY_NAME = "factPowerPlants";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactPowerPlantsRepository factPowerPlantsRepository;

    private final FactPowerPlantsSearchRepository factPowerPlantsSearchRepository;

    public FactPowerPlantsResource(
        FactPowerPlantsRepository factPowerPlantsRepository,
        FactPowerPlantsSearchRepository factPowerPlantsSearchRepository
    ) {
        this.factPowerPlantsRepository = factPowerPlantsRepository;
        this.factPowerPlantsSearchRepository = factPowerPlantsSearchRepository;
    }

    /**
     * {@code POST  /fact-power-plants} : Create a new factPowerPlants.
     *
     * @param factPowerPlants the factPowerPlants to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factPowerPlants, or with status {@code 400 (Bad Request)} if the factPowerPlants has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FactPowerPlants> createFactPowerPlants(@Valid @RequestBody FactPowerPlants factPowerPlants)
        throws URISyntaxException {
        LOG.debug("REST request to save FactPowerPlants : {}", factPowerPlants);
        if (factPowerPlants.getId() != null) {
            throw new BadRequestAlertException("A new factPowerPlants cannot already have an ID", ENTITY_NAME, "idexists");
        }
        factPowerPlants = factPowerPlantsRepository.save(factPowerPlants);
        factPowerPlantsSearchRepository.index(factPowerPlants);
        return ResponseEntity.created(new URI("/api/fact-power-plants/" + factPowerPlants.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factPowerPlants.getId()))
            .body(factPowerPlants);
    }

    /**
     * {@code PUT  /fact-power-plants/:id} : Updates an existing factPowerPlants.
     *
     * @param id the id of the factPowerPlants to save.
     * @param factPowerPlants the factPowerPlants to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factPowerPlants,
     * or with status {@code 400 (Bad Request)} if the factPowerPlants is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factPowerPlants couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactPowerPlants> updateFactPowerPlants(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactPowerPlants factPowerPlants
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactPowerPlants : {}, {}", id, factPowerPlants);
        if (factPowerPlants.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factPowerPlants.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factPowerPlantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        factPowerPlants = factPowerPlantsRepository.save(factPowerPlants);
        factPowerPlantsSearchRepository.index(factPowerPlants);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factPowerPlants.getId()))
            .body(factPowerPlants);
    }

    /**
     * {@code PATCH  /fact-power-plants/:id} : Partial updates given fields of an existing factPowerPlants, field will ignore if it is null
     *
     * @param id the id of the factPowerPlants to save.
     * @param factPowerPlants the factPowerPlants to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factPowerPlants,
     * or with status {@code 400 (Bad Request)} if the factPowerPlants is not valid,
     * or with status {@code 404 (Not Found)} if the factPowerPlants is not found,
     * or with status {@code 500 (Internal Server Error)} if the factPowerPlants couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactPowerPlants> partialUpdateFactPowerPlants(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactPowerPlants factPowerPlants
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactPowerPlants partially : {}, {}", id, factPowerPlants);
        if (factPowerPlants.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factPowerPlants.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factPowerPlantsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactPowerPlants> result = factPowerPlantsRepository
            .findById(factPowerPlants.getId())
            .map(existingFactPowerPlants -> {
                if (factPowerPlants.getName() != null) {
                    existingFactPowerPlants.setName(factPowerPlants.getName());
                }
                if (factPowerPlants.getIntalledCapacity() != null) {
                    existingFactPowerPlants.setIntalledCapacity(factPowerPlants.getIntalledCapacity());
                }
                if (factPowerPlants.getAvailabilityCapacity() != null) {
                    existingFactPowerPlants.setAvailabilityCapacity(factPowerPlants.getAvailabilityCapacity());
                }
                if (factPowerPlants.getStatus() != null) {
                    existingFactPowerPlants.setStatus(factPowerPlants.getStatus());
                }

                return existingFactPowerPlants;
            })
            .map(factPowerPlantsRepository::save)
            .map(savedFactPowerPlants -> {
                factPowerPlantsSearchRepository.index(savedFactPowerPlants);
                return savedFactPowerPlants;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factPowerPlants.getId())
        );
    }

    /**
     * {@code GET  /fact-power-plants} : get all the factPowerPlants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factPowerPlants in body.
     */
    @GetMapping("")
    public List<FactPowerPlants> getAllFactPowerPlants() {
        LOG.debug("REST request to get all FactPowerPlants");
        return factPowerPlantsRepository.findAll();
    }

    /**
     * {@code GET  /fact-power-plants/:id} : get the "id" factPowerPlants.
     *
     * @param id the id of the factPowerPlants to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factPowerPlants, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactPowerPlants> getFactPowerPlants(@PathVariable("id") String id) {
        LOG.debug("REST request to get FactPowerPlants : {}", id);
        Optional<FactPowerPlants> factPowerPlants = factPowerPlantsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(factPowerPlants);
    }

    /**
     * {@code DELETE  /fact-power-plants/:id} : delete the "id" factPowerPlants.
     *
     * @param id the id of the factPowerPlants to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactPowerPlants(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactPowerPlants : {}", id);
        factPowerPlantsRepository.deleteById(id);
        factPowerPlantsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fact-power-plants/_search?query=:query} : search for the factPowerPlants corresponding
     * to the query.
     *
     * @param query the query of the factPowerPlants search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactPowerPlants> searchFactPowerPlants(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactPowerPlants for query {}", query);
        try {
            return StreamSupport.stream(factPowerPlantsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
