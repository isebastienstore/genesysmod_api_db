package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactTradeCost;
import com.om4a.ct2s.domain.Metadata;
import com.om4a.ct2s.repository.FactTradeCostRepository;
import com.om4a.ct2s.repository.MetadataRepository;
import com.om4a.ct2s.repository.search.FactTradeCostSearchRepository;
import com.om4a.ct2s.service.MetadataService;
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
 * REST controller for managing {@link com.om4a.ct2s.domain.FactTradeCost}.
 */
@RestController
@RequestMapping("/api/fact-trade-costs")
public class FactTradeCostResource {

    private static final Logger LOG = LoggerFactory.getLogger(FactTradeCostResource.class);

    private static final String ENTITY_NAME = "factTradeCost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FactTradeCostRepository factTradeCostRepository;

    private final FactTradeCostSearchRepository factTradeCostSearchRepository;

    private final MetadataRepository metadataRepository;

    private final MetadataService metadataService;

    public FactTradeCostResource(
        FactTradeCostRepository factTradeCostRepository,
        FactTradeCostSearchRepository factTradeCostSearchRepository,
        MetadataRepository metadataRepository,
        MetadataService metadataService
    ) {
        this.factTradeCostRepository = factTradeCostRepository;
        this.factTradeCostSearchRepository = factTradeCostSearchRepository;
        this.metadataRepository = metadataRepository;
        this.metadataService = metadataService;
    }

    /**
     * {@code POST  /fact-trade-costs} : Create a new factTradeCost.
     *
     * @param factTradeCost the factTradeCost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new factTradeCost, or with status {@code 400 (Bad Request)} if the factTradeCost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/{source}")
    public ResponseEntity<FactTradeCost> createFactTradeCost(
        @Valid @RequestBody FactTradeCost factTradeCost,
        @NotNull @PathVariable("source") String source
    ) throws URISyntaxException {
        LOG.debug("REST request to save FactTradeCost : {}", factTradeCost);
        if (factTradeCost.getId() != null) {
            throw new BadRequestAlertException("A new factTradeCost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        //Ajout des metadonnees: qui a cree, quand et quelle est la source de donnee
        factTradeCost.metadata(metadataService.generateCreationMetadata(source));

        factTradeCost = factTradeCostRepository.save(factTradeCost);
        factTradeCostSearchRepository.index(factTradeCost);
        return ResponseEntity.created(new URI("/api/fact-trade-costs/" + factTradeCost.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factTradeCost.getId()))
            .body(factTradeCost);
    }

    /**
     * {@code PUT  /fact-trade-costs/:id} : Updates an existing factTradeCost.
     *
     * @param id the id of the factTradeCost to save.
     * @param factTradeCost the factTradeCost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factTradeCost,
     * or with status {@code 400 (Bad Request)} if the factTradeCost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the factTradeCost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FactTradeCost> updateFactTradeCost(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody FactTradeCost factTradeCost
    ) throws URISyntaxException {
        LOG.debug("REST request to update FactTradeCost : {}, {}", id, factTradeCost);
        if (factTradeCost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factTradeCost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factTradeCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        //Mise a jour des metadonnees: qui a modifie et quand
        metadataService.updateMetadata(factTradeCost.getMetadata());

        factTradeCost = factTradeCostRepository.save(factTradeCost);
        factTradeCostSearchRepository.index(factTradeCost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTradeCost.getId()))
            .body(factTradeCost);
    }

    /**
     * {@code PATCH  /fact-trade-costs/:id} : Partial updates given fields of an existing factTradeCost, field will ignore if it is null
     *
     * @param id the id of the factTradeCost to save.
     * @param factTradeCost the factTradeCost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated factTradeCost,
     * or with status {@code 400 (Bad Request)} if the factTradeCost is not valid,
     * or with status {@code 404 (Not Found)} if the factTradeCost is not found,
     * or with status {@code 500 (Internal Server Error)} if the factTradeCost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FactTradeCost> partialUpdateFactTradeCost(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody FactTradeCost factTradeCost
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FactTradeCost partially : {}, {}", id, factTradeCost);

        if (factTradeCost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, factTradeCost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!factTradeCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FactTradeCost> result = factTradeCostRepository
            .findById(factTradeCost.getId())
            .map(existingFactTradeCost -> {
                if (factTradeCost.getFixedCost() != null) {
                    existingFactTradeCost.setFixedCost(factTradeCost.getFixedCost());
                }
                if (factTradeCost.getVariableCost() != null) {
                    existingFactTradeCost.setVariableCost(factTradeCost.getVariableCost());
                }
                if (factTradeCost.getMetadata() != null) {
                    //Met à jour les métadonnées via le service comme dans update
                    metadataService.updateMetadata(existingFactTradeCost.getMetadata());
                }

                return existingFactTradeCost;
            })
            .map(factTradeCostRepository::save)
            .map(savedFactTradeCost -> {
                factTradeCostSearchRepository.index(savedFactTradeCost);
                return savedFactTradeCost;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTradeCost.getId())
        );
    }

    /**
     * {@code GET  /fact-trade-costs} : get all the factTradeCosts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of factTradeCosts in body.
     */
    @GetMapping("")
    public List<FactTradeCost> getAllFactTradeCosts() {
        LOG.debug("REST request to get all FactTradeCosts");
        List<FactTradeCost> result = factTradeCostRepository.findAll();
        for (FactTradeCost fact : result) {
            metadataService.updateLastInfosMetadata(fact.getMetadata());
        }
        return result;
    }

    /**
     * {@code GET  /fact-trade-costs/:id} : get the "id" factTradeCost.
     *
     * @param id the id of the factTradeCost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the factTradeCost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FactTradeCost> getFactTradeCost(@PathVariable String id) {
        LOG.debug("REST request to get FactTradeCost : {}", id);
        Optional<FactTradeCost> ftcOpt = factTradeCostRepository.findById(id);
        if (ftcOpt.isPresent()) {
            Metadata metadata = ftcOpt.get().getMetadata();
            metadataService.updateLastInfosMetadata(metadata);
        }
        return ResponseUtil.wrapOrNotFound(ftcOpt);
    }

    /**
     * {@code DELETE  /fact-trade-costs/:id} : delete the "id" factTradeCost.
     *
     * @param id the id of the factTradeCost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactTradeCost(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactTradeCost : {}", id);

        Optional<FactTradeCost> optionalFact = factTradeCostRepository.findById(id);

        if (optionalFact.isPresent()) {
            FactTradeCost fact = optionalFact.get();

            //Vérification de la présence des métadonnées avant suppression
            if (fact.getMetadata() != null && fact.getMetadata().getId() != null) {
                metadataService.deleteMetadataById(fact.getMetadata().getId());
            }

            factTradeCostRepository.deleteById(id);
            factTradeCostSearchRepository.deleteFromIndexById(id);

            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                .build();
        } else {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code SEARCH  /fact-trade-costs/_search?query=:query} : search for the factTradeCost corresponding
     * to the query.
     *
     * @param query the query of the factTradeCost search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<FactTradeCost> searchFactTradeCosts(@RequestParam("query") String query) {
        LOG.debug("REST request to search FactTradeCosts for query {}", query);
        try {
            return StreamSupport.stream(factTradeCostSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}