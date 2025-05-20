package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactRenewablePotentialAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactRenewablePotential;
import com.om4a.ct2s.repository.FactRenewablePotentialRepository;
import com.om4a.ct2s.repository.search.FactRenewablePotentialSearchRepository;
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
 * Integration tests for the {@link FactRenewablePotentialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactRenewablePotentialResourceIT {

    private static final Double DEFAULT_MAX_CAPACITY = 1D;
    private static final Double UPDATED_MAX_CAPACITY = 2D;

    private static final Double DEFAULT_AVAILABLE_CAPACITY = 1D;
    private static final Double UPDATED_AVAILABLE_CAPACITY = 2D;

    private static final Double DEFAULT_MIN_CAPACITY = 1D;
    private static final Double UPDATED_MIN_CAPACITY = 2D;

    private static final String ENTITY_API_URL = "/api/fact-renewable-potentials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-renewable-potentials/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactRenewablePotentialRepository factRenewablePotentialRepository;

    @Autowired
    private FactRenewablePotentialSearchRepository factRenewablePotentialSearchRepository;

    @Autowired
    private MockMvc restFactRenewablePotentialMockMvc;

    private FactRenewablePotential factRenewablePotential;

    private FactRenewablePotential insertedFactRenewablePotential;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactRenewablePotential createEntity() {
        return new FactRenewablePotential()
            .maxCapacity(DEFAULT_MAX_CAPACITY)
            .availableCapacity(DEFAULT_AVAILABLE_CAPACITY)
            .minCapacity(DEFAULT_MIN_CAPACITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactRenewablePotential createUpdatedEntity() {
        return new FactRenewablePotential()
            .maxCapacity(UPDATED_MAX_CAPACITY)
            .availableCapacity(UPDATED_AVAILABLE_CAPACITY)
            .minCapacity(UPDATED_MIN_CAPACITY);
    }

    @BeforeEach
    void initTest() {
        factRenewablePotential = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactRenewablePotential != null) {
            factRenewablePotentialRepository.delete(insertedFactRenewablePotential);
            factRenewablePotentialSearchRepository.delete(insertedFactRenewablePotential);
            insertedFactRenewablePotential = null;
        }
    }

    @Test
    void createFactRenewablePotential() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        // Create the FactRenewablePotential
        var returnedFactRenewablePotential = om.readValue(
            restFactRenewablePotentialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factRenewablePotential)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactRenewablePotential.class
        );

        // Validate the FactRenewablePotential in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactRenewablePotentialUpdatableFieldsEquals(
            returnedFactRenewablePotential,
            getPersistedFactRenewablePotential(returnedFactRenewablePotential)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactRenewablePotential = returnedFactRenewablePotential;
    }

    @Test
    void createFactRenewablePotentialWithExistingId() throws Exception {
        // Create the FactRenewablePotential with an existing ID
        factRenewablePotential.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactRenewablePotentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factRenewablePotential)))
            .andExpect(status().isBadRequest());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkMaxCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        // set the field null
        factRenewablePotential.setMaxCapacity(null);

        // Create the FactRenewablePotential, which fails.

        restFactRenewablePotentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factRenewablePotential)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAvailableCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        // set the field null
        factRenewablePotential.setAvailableCapacity(null);

        // Create the FactRenewablePotential, which fails.

        restFactRenewablePotentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factRenewablePotential)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkMinCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        // set the field null
        factRenewablePotential.setMinCapacity(null);

        // Create the FactRenewablePotential, which fails.

        restFactRenewablePotentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factRenewablePotential)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactRenewablePotentials() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);

        // Get all the factRenewablePotentialList
        restFactRenewablePotentialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factRenewablePotential.getId())))
            .andExpect(jsonPath("$.[*].maxCapacity").value(hasItem(DEFAULT_MAX_CAPACITY)))
            .andExpect(jsonPath("$.[*].availableCapacity").value(hasItem(DEFAULT_AVAILABLE_CAPACITY)))
            .andExpect(jsonPath("$.[*].minCapacity").value(hasItem(DEFAULT_MIN_CAPACITY)));
    }

    @Test
    void getFactRenewablePotential() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);

        // Get the factRenewablePotential
        restFactRenewablePotentialMockMvc
            .perform(get(ENTITY_API_URL_ID, factRenewablePotential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factRenewablePotential.getId()))
            .andExpect(jsonPath("$.maxCapacity").value(DEFAULT_MAX_CAPACITY))
            .andExpect(jsonPath("$.availableCapacity").value(DEFAULT_AVAILABLE_CAPACITY))
            .andExpect(jsonPath("$.minCapacity").value(DEFAULT_MIN_CAPACITY));
    }

    @Test
    void getNonExistingFactRenewablePotential() throws Exception {
        // Get the factRenewablePotential
        restFactRenewablePotentialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactRenewablePotential() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factRenewablePotentialSearchRepository.save(factRenewablePotential);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());

        // Update the factRenewablePotential
        FactRenewablePotential updatedFactRenewablePotential = factRenewablePotentialRepository
            .findById(factRenewablePotential.getId())
            .orElseThrow();
        updatedFactRenewablePotential
            .maxCapacity(UPDATED_MAX_CAPACITY)
            .availableCapacity(UPDATED_AVAILABLE_CAPACITY)
            .minCapacity(UPDATED_MIN_CAPACITY);

        restFactRenewablePotentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactRenewablePotential.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactRenewablePotential))
            )
            .andExpect(status().isOk());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactRenewablePotentialToMatchAllProperties(updatedFactRenewablePotential);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactRenewablePotential> factRenewablePotentialSearchList = Streamable.of(
                    factRenewablePotentialSearchRepository.findAll()
                ).toList();
                FactRenewablePotential testFactRenewablePotentialSearch = factRenewablePotentialSearchList.get(searchDatabaseSizeAfter - 1);

                assertFactRenewablePotentialAllPropertiesEquals(testFactRenewablePotentialSearch, updatedFactRenewablePotential);
            });
    }

    @Test
    void putNonExistingFactRenewablePotential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        factRenewablePotential.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactRenewablePotentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factRenewablePotential.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factRenewablePotential))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactRenewablePotential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        factRenewablePotential.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactRenewablePotentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factRenewablePotential))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactRenewablePotential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        factRenewablePotential.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactRenewablePotentialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factRenewablePotential)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactRenewablePotentialWithPatch() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factRenewablePotential using partial update
        FactRenewablePotential partialUpdatedFactRenewablePotential = new FactRenewablePotential();
        partialUpdatedFactRenewablePotential.setId(factRenewablePotential.getId());

        partialUpdatedFactRenewablePotential.availableCapacity(UPDATED_AVAILABLE_CAPACITY);

        restFactRenewablePotentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactRenewablePotential.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactRenewablePotential))
            )
            .andExpect(status().isOk());

        // Validate the FactRenewablePotential in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactRenewablePotentialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactRenewablePotential, factRenewablePotential),
            getPersistedFactRenewablePotential(factRenewablePotential)
        );
    }

    @Test
    void fullUpdateFactRenewablePotentialWithPatch() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factRenewablePotential using partial update
        FactRenewablePotential partialUpdatedFactRenewablePotential = new FactRenewablePotential();
        partialUpdatedFactRenewablePotential.setId(factRenewablePotential.getId());

        partialUpdatedFactRenewablePotential
            .maxCapacity(UPDATED_MAX_CAPACITY)
            .availableCapacity(UPDATED_AVAILABLE_CAPACITY)
            .minCapacity(UPDATED_MIN_CAPACITY);

        restFactRenewablePotentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactRenewablePotential.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactRenewablePotential))
            )
            .andExpect(status().isOk());

        // Validate the FactRenewablePotential in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactRenewablePotentialUpdatableFieldsEquals(
            partialUpdatedFactRenewablePotential,
            getPersistedFactRenewablePotential(partialUpdatedFactRenewablePotential)
        );
    }

    @Test
    void patchNonExistingFactRenewablePotential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        factRenewablePotential.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactRenewablePotentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factRenewablePotential.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factRenewablePotential))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactRenewablePotential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        factRenewablePotential.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactRenewablePotentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factRenewablePotential))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactRenewablePotential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        factRenewablePotential.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactRenewablePotentialMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factRenewablePotential))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactRenewablePotential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactRenewablePotential() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialSearchRepository.save(factRenewablePotential);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factRenewablePotential
        restFactRenewablePotentialMockMvc
            .perform(delete(ENTITY_API_URL_ID, factRenewablePotential.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factRenewablePotentialSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactRenewablePotential() throws Exception {
        // Initialize the database
        insertedFactRenewablePotential = factRenewablePotentialRepository.save(factRenewablePotential);
        factRenewablePotentialSearchRepository.save(factRenewablePotential);

        // Search the factRenewablePotential
        restFactRenewablePotentialMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factRenewablePotential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factRenewablePotential.getId())))
            .andExpect(jsonPath("$.[*].maxCapacity").value(hasItem(DEFAULT_MAX_CAPACITY)))
            .andExpect(jsonPath("$.[*].availableCapacity").value(hasItem(DEFAULT_AVAILABLE_CAPACITY)))
            .andExpect(jsonPath("$.[*].minCapacity").value(hasItem(DEFAULT_MIN_CAPACITY)));
    }

    protected long getRepositoryCount() {
        return factRenewablePotentialRepository.count();
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

    protected FactRenewablePotential getPersistedFactRenewablePotential(FactRenewablePotential factRenewablePotential) {
        return factRenewablePotentialRepository.findById(factRenewablePotential.getId()).orElseThrow();
    }

    protected void assertPersistedFactRenewablePotentialToMatchAllProperties(FactRenewablePotential expectedFactRenewablePotential) {
        assertFactRenewablePotentialAllPropertiesEquals(
            expectedFactRenewablePotential,
            getPersistedFactRenewablePotential(expectedFactRenewablePotential)
        );
    }

    protected void assertPersistedFactRenewablePotentialToMatchUpdatableProperties(FactRenewablePotential expectedFactRenewablePotential) {
        assertFactRenewablePotentialAllUpdatablePropertiesEquals(
            expectedFactRenewablePotential,
            getPersistedFactRenewablePotential(expectedFactRenewablePotential)
        );
    }
}
