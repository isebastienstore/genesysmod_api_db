package com.om4a.ct2s.web.rest;

import com.om4a.ct2s.domain.FactRenewablePotential;
import com.om4a.ct2s.domain.Metadata;
import com.om4a.ct2s.repository.FactRenewablePotentialRepository;
import com.om4a.ct2s.repository.MetadataRepository;
import com.om4a.ct2s.repository.search.FactRenewablePotentialSearchRepository;
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

    private final MetadataRepository metadataRepository;

    private final MetadataService metadataService;

    public FactRenewablePotentialResource(
        FactRenewablePotentialRepository factRenewablePotentialRepository,
        FactRenewablePotentialSearchRepository factRenewablePotentialSearchRepository,
        MetadataRepository metadataRepository,
        MetadataService metadataService
    ) {
        this.factRenewablePotentialRepository = factRenewablePotentialRepository;
        this.factRenewablePotentialSearchRepository = factRenewablePotentialSearchRepository;
        this.metadataRepository = metadataRepository;
        this.metadataService = metadataService;
    }

    @PostMapping("/{source}")
    public ResponseEntity<FactRenewablePotential> createFactRenewablePotential(
        @Valid @RequestBody FactRenewablePotential factRenewablePotential,
        @NotNull @PathVariable("source") String source
    ) throws URISyntaxException {
        LOG.debug("REST request to save FactRenewablePotential : {}", factRenewablePotential);
        if (factRenewablePotential.getId() != null) {
            throw new BadRequestAlertException("A new factRenewablePotential cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        //Ajout des metadonnees: qui a cree, quand et quelle est la source de donnee
        factRenewablePotential.metadata(metadataService.generateCreationMetadata(source));

        factRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialSearchRepository.index(factRenewablePotential);
        return ResponseEntity.created(new URI("/api/fact-renewable-potentials/" + factRenewablePotential.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, factRenewablePotential.getId()))
            .body(factRenewablePotential);
    }

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

        //Mise a jour des metadonnees: qui a modifie et quand
        metadataService.updateMetadata(factRenewablePotential.getMetadata());

        factRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialSearchRepository.index(factRenewablePotential);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, factRenewablePotential.getId()))
            .body(factRenewablePotential);
    }

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
                if (factRenewablePotential.getMetadata() != null) {
                    //Met à jour les métadonnées via le service comme dans update
                    metadataService.updateMetadata(existingFactRenewablePotential.getMetadata());
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

    @GetMapping("")
    public List<FactRenewablePotential> getAllFactRenewablePotentials() {
        LOG.debug("REST request to get all FactRenewablePotentials");
        List<FactRenewablePotential> result = factRenewablePotentialRepository.findAll();
        for (FactRenewablePotential fact : result) {
            metadataService.updateLastInfosMetadata(fact.getMetadata());
        }
        return result;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FactRenewablePotential> getFactRenewablePotential(@PathVariable String id) {
        LOG.debug("REST request to get FactRenewablePotential : {}", id);
        Optional<FactRenewablePotential> frpOpt = factRenewablePotentialRepository.findById(id);
        if (frpOpt.isPresent()) {
            Metadata metadata = frpOpt.get().getMetadata();
            metadataService.updateLastInfosMetadata(metadata);
        }
        return ResponseUtil.wrapOrNotFound(frpOpt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactRenewablePotential(@PathVariable("id") String id) {
        LOG.debug("REST request to delete FactRenewablePotential : {}", id);

        Optional<FactRenewablePotential> optionalFact = factRenewablePotentialRepository.findById(id);

        if (optionalFact.isPresent()) {
            FactRenewablePotential fact = optionalFact.get();

            //Vérification de la présence des métadonnées avant suppression
            if (fact.getMetadata() != null && fact.getMetadata().getId() != null) {
                metadataService.deleteMetadataById(fact.getMetadata().getId());
            }

            factRenewablePotentialRepository.deleteById(id);
            factRenewablePotentialSearchRepository.deleteFromIndexById(id);

            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                .build();
        } else {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

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
