package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.MetadataAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static com.om4a.ct2s.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.Metadata;
import com.om4a.ct2s.repository.MetadataRepository;
import com.om4a.ct2s.repository.search.MetadataSearchRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link MetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetadataResourceIT {

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/metadata/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MetadataSearchRepository metadataSearchRepository;

    @Autowired
    private MockMvc restMetadataMockMvc;

    private Metadata metadata;

    private Metadata insertedMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metadata createEntity() {
        return new Metadata()
            .createdBy(DEFAULT_CREATED_BY)
            .updatedBy(DEFAULT_UPDATED_BY)
            .action(DEFAULT_ACTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .source(DEFAULT_SOURCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Metadata createUpdatedEntity() {
        return new Metadata()
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .action(UPDATED_ACTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .source(UPDATED_SOURCE);
    }

    @BeforeEach
    void initTest() {
        metadata = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetadata != null) {
            metadataRepository.delete(insertedMetadata);
            metadataSearchRepository.delete(insertedMetadata);
            insertedMetadata = null;
        }
    }

    @Test
    void createMetadata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        // Create the Metadata
        var returnedMetadata = om.readValue(
            restMetadataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metadata)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Metadata.class
        );

        // Validate the Metadata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMetadataUpdatableFieldsEquals(returnedMetadata, getPersistedMetadata(returnedMetadata));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetadata = returnedMetadata;
    }

    @Test
    void createMetadataWithExistingId() throws Exception {
        // Create the Metadata with an existing ID
        metadata.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metadata)))
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllMetadata() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);

        // Get all the metadataList
        restMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));
    }

    @Test
    void getMetadata() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);

        // Get the metadata
        restMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metadata.getId()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE));
    }

    @Test
    void getNonExistingMetadata() throws Exception {
        // Get the metadata
        restMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMetadata() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metadataSearchRepository.save(metadata);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());

        // Update the metadata
        Metadata updatedMetadata = metadataRepository.findById(metadata.getId()).orElseThrow();
        updatedMetadata
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .action(UPDATED_ACTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .source(UPDATED_SOURCE);

        restMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMetadata.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMetadata))
            )
            .andExpect(status().isOk());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetadataToMatchAllProperties(updatedMetadata);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Metadata> metadataSearchList = Streamable.of(metadataSearchRepository.findAll()).toList();
                Metadata testMetadataSearch = metadataSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetadataAllPropertiesEquals(testMetadataSearch, updatedMetadata);
            });
    }

    @Test
    void putNonExistingMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        metadata.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metadata.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        metadata.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        metadata.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetadataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metadata)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metadata using partial update
        Metadata partialUpdatedMetadata = new Metadata();
        partialUpdatedMetadata.setId(metadata.getId());

        partialUpdatedMetadata.action(UPDATED_ACTION).updatedAt(UPDATED_UPDATED_AT).source(UPDATED_SOURCE);

        restMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetadata))
            )
            .andExpect(status().isOk());

        // Validate the Metadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetadataUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMetadata, metadata), getPersistedMetadata(metadata));
    }

    @Test
    void fullUpdateMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metadata using partial update
        Metadata partialUpdatedMetadata = new Metadata();
        partialUpdatedMetadata.setId(metadata.getId());

        partialUpdatedMetadata
            .createdBy(UPDATED_CREATED_BY)
            .updatedBy(UPDATED_UPDATED_BY)
            .action(UPDATED_ACTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .source(UPDATED_SOURCE);

        restMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetadata))
            )
            .andExpect(status().isOk());

        // Validate the Metadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetadataUpdatableFieldsEquals(partialUpdatedMetadata, getPersistedMetadata(partialUpdatedMetadata));
    }

    @Test
    void patchNonExistingMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        metadata.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        metadata.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metadata))
            )
            .andExpect(status().isBadRequest());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        metadata.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetadataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metadata)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Metadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteMetadata() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);
        metadataRepository.save(metadata);
        metadataSearchRepository.save(metadata);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metadata
        restMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, metadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchMetadata() throws Exception {
        // Initialize the database
        insertedMetadata = metadataRepository.save(metadata);
        metadataSearchRepository.save(metadata);

        // Search the metadata
        restMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metadata.getId())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));
    }

    protected long getRepositoryCount() {
        return metadataRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Metadata getPersistedMetadata(Metadata metadata) {
        return metadataRepository.findById(metadata.getId()).orElseThrow();
    }

    protected void assertPersistedMetadataToMatchAllProperties(Metadata expectedMetadata) {
        assertMetadataAllPropertiesEquals(expectedMetadata, getPersistedMetadata(expectedMetadata));
    }

    protected void assertPersistedMetadataToMatchUpdatableProperties(Metadata expectedMetadata) {
        assertMetadataAllUpdatablePropertiesEquals(expectedMetadata, getPersistedMetadata(expectedMetadata));
    }
}
