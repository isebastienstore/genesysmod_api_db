package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactTradeCapacity;
import com.om4a.ct2s.domain.Metadata;
import com.om4a.ct2s.repository.FactTradeCapacityRepository;
import com.om4a.ct2s.repository.MetadataRepository;
import com.om4a.ct2s.repository.search.FactTradeCapacitySearchRepository;
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

    private final MetadataRepository metadataRepository;

    private final MetadataService metadataService;

    public FactTradeCapacityResource(
        FactTradeCapacityRepository factTradeCapacityRepository,
        FactTradeCapacitySearchRepository factTradeCapacitySearchRepository,
        MetadataRepository metadataRepository,
        MetadataService metadataService
    ) {
        this.factTradeCapacityRepository = factTradeCapacityRepository;
        this.factTradeCapacitySearchRepository = factTradeCapacitySearchRepository;
        this.metadataRepository = metadataRepository;
        this.metadataService = metadataService;
    }

    @PostMapping("/{source}")
    public ResponseEntity<FactTradeCapacity> createFactTradeCapacity(
        @Valid @RequestBody FactTradeCapacity factTradeCapacity,
        @NotNull @PathVariable("source") String source
    ) throws URISyntaxException {
        LOG.debug("REST request to save FactTradeCapacity : {}", factTradeCapacity);
        if (factTradeCapacity.getId() != null) {
            throw new BadRequestAlertException("A new factTradeCapacity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        //Ajout des metadonnees: qui a cree, quand et quelle est la source de donnee
        factTradeCapacity.metadata(metadataService.generateCreationMetadata(source));

        factTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacitySearchRepository.index(factTradeCapacity);
        return ResponseEntity.created(new URI("/api/fact-trade-capacities/" + factTradeCapacity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factTradeCapacity.getId()))
            .body(factTradeCapacity);
    }

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

        //Mise a jour des metadonnees: qui a modifie et quand
        metadataService.updateMetadata(factTradeCapacity.getMetadata());

        factTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacitySearchRepository.index(factTradeCapacity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factTradeCapacity.getId()))
            .body(factTradeCapacity);
    }

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
                if (factTradeCapacity.getMetadata() != null) {
                    //Met à jour les métadonnées via le service comme dans update
                    metadataService.updateMetadata(existingFactTradeCapacity.getMetadata());
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

    @GetMapping("")
    public List<FactTradeCapacity> getAllFactTradeCapacities() {
        LOG.debug("REST request to get all FactTradeCapacities");
        List<FactTradeCapacity> result = factTradeCapacityRepository.findAll();
        for (FactTradeCapacity fact : result) {
            metadataService.updateLastInfosMetadata(fact.getMetadata());
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactTradeCapacity> getFactTradeCapacity(@PathVariable String id) {
        LOG.debug("REST request to get FactTradeCapacity : {}", id);
        Optional<FactTradeCapacity> ftcOpt = factTradeCapacityRepository.findById(id);
        if (ftcOpt.isPresent()) {
            Metadata metadata = ftcOpt.get().getMetadata();
            metadataService.updateLastInfosMetadata(metadata);
        }
        return ResponseUtil.wrapOrNotFound(ftcOpt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactTradeCapacity(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactTradeCapacity : {}", id);

        Optional<FactTradeCapacity> optionalFact = factTradeCapacityRepository.findById(id);

        if (optionalFact.isPresent()) {
            FactTradeCapacity fact = optionalFact.get();

            //Vérification de la présence des métadonnées avant suppression
            if (fact.getMetadata() != null && fact.getMetadata().getId() != null) {
                metadataService.deleteMetadataById(fact.getMetadata().getId());
            }

            factTradeCapacityRepository.deleteById(id);
            factTradeCapacitySearchRepository.deleteFromIndexById(id);

            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                .build();
        } else {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

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