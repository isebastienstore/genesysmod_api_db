package com.om4a.ct2s.service;

import com.om4a.ct2s.domain.Metadata;
import com.om4a.ct2s.repository.MetadataRepository;
import com.om4a.ct2s.web.rest.FactElectricityConsumptionResource;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataService.class);

    private final MetadataRepository metadataRepository;

    public MetadataService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public Metadata generateCreationMetadata(String source) {
        Metadata metadata = new Metadata();
        metadata.setCreatedAt(ZonedDateTime.now());
        metadata.setCreatedBy(getCurrentUserLogin());
        metadata.setSource(source);
        return metadataRepository.save(metadata);
    }

    public void updateMetadata(Metadata metadata) {
        metadata.setUpdatedAt(ZonedDateTime.now());
        metadata.setUpdatedBy(getCurrentUserLogin());
        LOG.info("Update metadata : {}", metadataRepository.save(metadata));
    }

    public void updateLastInfosMetadata(Metadata metadata) {
        metadata.setLastAccessAt(ZonedDateTime.now());
        metadata.setLastAccessBy(getCurrentUserLogin());
        LOG.info("Update last access infos of metadata : {}", metadataRepository.save(metadata));
    }

    public void deleteMetadataById(String id) {
        if (id != null && metadataRepository.existsById(id)) {
            metadataRepository.deleteById(id);
        }
    }

    private String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
