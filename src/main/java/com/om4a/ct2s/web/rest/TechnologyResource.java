package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.Technology;
import com.om4a.ct2s.repository.TechnologyRepository;
import com.om4a.ct2s.repository.search.TechnologySearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.Technology}.
 */
@RestController
@RequestMapping("/api/technologies")
public class TechnologyResource {

    private static final Logger LOG = LoggerFactory.getLogger(TechnologyResource.class);

    private static final String ENTITY_NAME = "technology";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnologyRepository technologyRepository;

    private final TechnologySearchRepository technologySearchRepository;

    public TechnologyResource(TechnologyRepository technologyRepository, TechnologySearchRepository technologySearchRepository) {
        this.technologyRepository = technologyRepository;
        this.technologySearchRepository = technologySearchRepository;
    }

    /**
     * {@code POST  /technologies} : Create a new technology.
     *
     * @param technology the technology to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technology, or with status {@code 400 (Bad Request)} if the technology has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Technology> createTechnology(@Valid @RequestBody Technology technology) throws URISyntaxException {
        LOG.debug("REST request to save Technology : {}", technology);
        if (technology.getId() != null) {
            throw new BadRequestAlertException("A new technology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        technology = technologyRepository.save(technology);
        technologySearchRepository.index(technology);
        return ResponseEntity.created(new URI("/api/technologies/" + technology.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, technology.getId()))
            .body(technology);
    }

    /**
     * {@code PUT  /technologies/:id} : Updates an existing technology.
     *
     * @param id the id of the technology to save.
     * @param technology the technology to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technology,
     * or with status {@code 400 (Bad Request)} if the technology is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technology couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Technology> updateTechnology(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Technology technology
    ) throws URISyntaxException {
        LOG.debug("REST request to update Technology : {}, {}", id, technology);
        if (technology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technology.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        technology = technologyRepository.save(technology);
        technologySearchRepository.index(technology);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, technology.getId()))
            .body(technology);
    }

    /**
     * {@code PATCH  /technologies/:id} : Partial updates given fields of an existing technology, field will ignore if it is null
     *
     * @param id the id of the technology to save.
     * @param technology the technology to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technology,
     * or with status {@code 400 (Bad Request)} if the technology is not valid,
     * or with status {@code 404 (Not Found)} if the technology is not found,
     * or with status {@code 500 (Internal Server Error)} if the technology couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Technology> partialUpdateTechnology(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Technology technology
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Technology partially : {}, {}", id, technology);
        if (technology.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technology.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technologyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Technology> result = technologyRepository
            .findById(technology.getId())
            .map(existingTechnology -> {
                if (technology.getName() != null) {
                    existingTechnology.setName(technology.getName());
                }
                if (technology.getCategory() != null) {
                    existingTechnology.setCategory(technology.getCategory());
                }

                return existingTechnology;
            })
            .map(technologyRepository::save)
            .map(savedTechnology -> {
                technologySearchRepository.index(savedTechnology);
                return savedTechnology;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, technology.getId())
        );
    }

    /**
     * {@code GET  /technologies} : get all the technologies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technologies in body.
     */
    @GetMapping("")
    public List<Technology> getAllTechnologies() {
        LOG.debug("REST request to get all Technologies");
        return technologyRepository.findAll();
    }

    /**
     * {@code GET  /technologies/:id} : get the "id" technology.
     *
     * @param id the id of the technology to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technology, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Technology> getTechnology(@PathVariable("id") String id) {
        LOG.debug("REST request to get Technology : {}", id);
        Optional<Technology> technology = technologyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(technology);
    }

    /**
     * {@code DELETE  /technologies/:id} : delete the "id" technology.
     *
     * @param id the id of the technology to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnology(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Technology : {}", id);
        technologyRepository.deleteById(id);
        technologySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /technologies/_search?query=:query} : search for the technology corresponding
     * to the query.
     *
     * @param query the query of the technology search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Technology> searchTechnologies(@RequestParam("query") String query) {
        LOG.debug("REST request to search Technologies for query {}", query);
        try {
            return StreamSupport.stream(technologySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
