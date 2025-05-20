package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.Fuel;
import com.om4a.ct2s.repository.FuelRepository;
import com.om4a.ct2s.repository.search.FuelSearchRepository;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.Fuel}.
 */
@RestController
@RequestMapping("/api/fuels")
public class FuelResource {

    private static final Logger LOG = LoggerFactory.getLogger(FuelResource.class);

    private static final String ENTITY_NAME = "fuel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FuelRepository fuelRepository;

    private final FuelSearchRepository fuelSearchRepository;

    public FuelResource(FuelRepository fuelRepository, FuelSearchRepository fuelSearchRepository) {
        this.fuelRepository = fuelRepository;
        this.fuelSearchRepository = fuelSearchRepository;
    }

    /**
     * {@code POST  /fuels} : Create a new fuel.
     *
     * @param fuel the fuel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fuel, or with status {@code 400 (Bad Request)} if the fuel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Fuel> createFuel(@Valid @RequestBody Fuel fuel) throws URISyntaxException {
        LOG.debug("REST request to save Fuel : {}", fuel);
        if (fuel.getId() != null) {
            throw new BadRequestAlertException("A new fuel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        fuel = fuelRepository.save(fuel);
        fuelSearchRepository.index(fuel);
        return ResponseEntity.created(new URI("/api/fuels/" + fuel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, fuel.getId()))
            .body(fuel);
    }

    /**
     * {@code PUT  /fuels/:id} : Updates an existing fuel.
     *
     * @param id the id of the fuel to save.
     * @param fuel the fuel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fuel,
     * or with status {@code 400 (Bad Request)} if the fuel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fuel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fuel> updateFuel(@PathVariable(value = "id", required = false) final String id, @Valid @RequestBody Fuel fuel)
        throws URISyntaxException {
        LOG.debug("REST request to update Fuel : {}, {}", id, fuel);
        if (fuel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fuel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fuelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        fuel = fuelRepository.save(fuel);
        fuelSearchRepository.index(fuel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fuel.getId()))
            .body(fuel);
    }

    /**
     * {@code PATCH  /fuels/:id} : Partial updates given fields of an existing fuel, field will ignore if it is null
     *
     * @param id the id of the fuel to save.
     * @param fuel the fuel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fuel,
     * or with status {@code 400 (Bad Request)} if the fuel is not valid,
     * or with status {@code 404 (Not Found)} if the fuel is not found,
     * or with status {@code 500 (Internal Server Error)} if the fuel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Fuel> partialUpdateFuel(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Fuel fuel
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Fuel partially : {}, {}", id, fuel);
        if (fuel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fuel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fuelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Fuel> result = fuelRepository
            .findById(fuel.getId())
            .map(existingFuel -> {
                if (fuel.getName() != null) {
                    existingFuel.setName(fuel.getName());
                }

                return existingFuel;
            })
            .map(fuelRepository::save)
            .map(savedFuel -> {
                fuelSearchRepository.index(savedFuel);
                return savedFuel;
            });

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, fuel.getId()));
    }

    /**
     * {@code GET  /fuels} : get all the fuels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fuels in body.
     */
    @GetMapping("")
    public List<Fuel> getAllFuels() {
        LOG.debug("REST request to get all Fuels");
        return fuelRepository.findAll();
    }

    /**
     * {@code GET  /fuels/:id} : get the "id" fuel.
     *
     * @param id the id of the fuel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fuel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fuel> getFuel(@PathVariable("id") String id) {
        LOG.debug("REST request to get Fuel : {}", id);
        Optional<Fuel> fuel = fuelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fuel);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Fuel> getFuelByName(@PathVariable("name") String name) {
        LOG.debug("REST request to get Fuel by name : {}", name);
        Optional<Fuel> fuel = fuelRepository.findByName(name);
        return ResponseUtil.wrapOrNotFound(fuel);
    }

    /**
     * {@code DELETE  /fuels/:id} : delete the "id" fuel.
     *
     * @param id the id of the fuel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuel(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Fuel : {}", id);
        fuelRepository.deleteById(id);
        fuelSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /fuels/_search?query=:query} : search for the fuel corresponding
     * to the query.
     *
     * @param query the query of the fuel search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Fuel> searchFuels(@RequestParam("query") String query) {
        LOG.debug("REST request to search Fuels for query {}", query);
        try {
            return StreamSupport.stream(fuelSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
