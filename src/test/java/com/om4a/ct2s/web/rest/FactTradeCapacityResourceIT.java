package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactTradeCapacityAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactTradeCapacity;
import com.om4a.ct2s.repository.FactTradeCapacityRepository;
import com.om4a.ct2s.repository.search.FactTradeCapacitySearchRepository;
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
 * Integration tests for the {@link FactTradeCapacityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactTradeCapacityResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/fact-trade-capacities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-trade-capacities/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactTradeCapacityRepository factTradeCapacityRepository;

    @Autowired
    private FactTradeCapacitySearchRepository factTradeCapacitySearchRepository;

    @Autowired
    private MockMvc restFactTradeCapacityMockMvc;

    private FactTradeCapacity factTradeCapacity;

    private FactTradeCapacity insertedFactTradeCapacity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactTradeCapacity createEntity() {
        return new FactTradeCapacity().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactTradeCapacity createUpdatedEntity() {
        return new FactTradeCapacity().value(UPDATED_VALUE);
    }

    @BeforeEach
    void initTest() {
        factTradeCapacity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactTradeCapacity != null) {
            factTradeCapacityRepository.delete(insertedFactTradeCapacity);
            factTradeCapacitySearchRepository.delete(insertedFactTradeCapacity);
            insertedFactTradeCapacity = null;
        }
    }

    @Test
    void createFactTradeCapacity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        // Create the FactTradeCapacity
        var returnedFactTradeCapacity = om.readValue(
            restFactTradeCapacityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCapacity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactTradeCapacity.class
        );

        // Validate the FactTradeCapacity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactTradeCapacityUpdatableFieldsEquals(returnedFactTradeCapacity, getPersistedFactTradeCapacity(returnedFactTradeCapacity));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactTradeCapacity = returnedFactTradeCapacity;
    }

    @Test
    void createFactTradeCapacityWithExistingId() throws Exception {
        // Create the FactTradeCapacity with an existing ID
        factTradeCapacity.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactTradeCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCapacity)))
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        // set the field null
        factTradeCapacity.setValue(null);

        // Create the FactTradeCapacity, which fails.

        restFactTradeCapacityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCapacity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactTradeCapacities() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);

        // Get all the factTradeCapacityList
        restFactTradeCapacityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factTradeCapacity.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    void getFactTradeCapacity() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);

        // Get the factTradeCapacity
        restFactTradeCapacityMockMvc
            .perform(get(ENTITY_API_URL_ID, factTradeCapacity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factTradeCapacity.getId()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    void getNonExistingFactTradeCapacity() throws Exception {
        // Get the factTradeCapacity
        restFactTradeCapacityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactTradeCapacity() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factTradeCapacitySearchRepository.save(factTradeCapacity);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());

        // Update the factTradeCapacity
        FactTradeCapacity updatedFactTradeCapacity = factTradeCapacityRepository.findById(factTradeCapacity.getId()).orElseThrow();
        updatedFactTradeCapacity.value(UPDATED_VALUE);

        restFactTradeCapacityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactTradeCapacity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactTradeCapacity))
            )
            .andExpect(status().isOk());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactTradeCapacityToMatchAllProperties(updatedFactTradeCapacity);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactTradeCapacity> factTradeCapacitySearchList = Streamable.of(factTradeCapacitySearchRepository.findAll()).toList();
                FactTradeCapacity testFactTradeCapacitySearch = factTradeCapacitySearchList.get(searchDatabaseSizeAfter - 1);

                assertFactTradeCapacityAllPropertiesEquals(testFactTradeCapacitySearch, updatedFactTradeCapacity);
            });
    }

    @Test
    void putNonExistingFactTradeCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        factTradeCapacity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactTradeCapacityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factTradeCapacity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factTradeCapacity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactTradeCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        factTradeCapacity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCapacityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factTradeCapacity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactTradeCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        factTradeCapacity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCapacityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCapacity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactTradeCapacityWithPatch() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factTradeCapacity using partial update
        FactTradeCapacity partialUpdatedFactTradeCapacity = new FactTradeCapacity();
        partialUpdatedFactTradeCapacity.setId(factTradeCapacity.getId());

        partialUpdatedFactTradeCapacity.value(UPDATED_VALUE);

        restFactTradeCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactTradeCapacity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactTradeCapacity))
            )
            .andExpect(status().isOk());

        // Validate the FactTradeCapacity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactTradeCapacityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactTradeCapacity, factTradeCapacity),
            getPersistedFactTradeCapacity(factTradeCapacity)
        );
    }

    @Test
    void fullUpdateFactTradeCapacityWithPatch() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factTradeCapacity using partial update
        FactTradeCapacity partialUpdatedFactTradeCapacity = new FactTradeCapacity();
        partialUpdatedFactTradeCapacity.setId(factTradeCapacity.getId());

        partialUpdatedFactTradeCapacity.value(UPDATED_VALUE);

        restFactTradeCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactTradeCapacity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactTradeCapacity))
            )
            .andExpect(status().isOk());

        // Validate the FactTradeCapacity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactTradeCapacityUpdatableFieldsEquals(
            partialUpdatedFactTradeCapacity,
            getPersistedFactTradeCapacity(partialUpdatedFactTradeCapacity)
        );
    }

    @Test
    void patchNonExistingFactTradeCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        factTradeCapacity.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactTradeCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factTradeCapacity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factTradeCapacity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactTradeCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        factTradeCapacity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCapacityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factTradeCapacity))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactTradeCapacity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        factTradeCapacity.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCapacityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factTradeCapacity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactTradeCapacity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactTradeCapacity() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacitySearchRepository.save(factTradeCapacity);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factTradeCapacity
        restFactTradeCapacityMockMvc
            .perform(delete(ENTITY_API_URL_ID, factTradeCapacity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCapacitySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactTradeCapacity() throws Exception {
        // Initialize the database
        insertedFactTradeCapacity = factTradeCapacityRepository.save(factTradeCapacity);
        factTradeCapacitySearchRepository.save(factTradeCapacity);

        // Search the factTradeCapacity
        restFactTradeCapacityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factTradeCapacity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factTradeCapacity.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    protected long getRepositoryCount() {
        return factTradeCapacityRepository.count();
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

    protected FactTradeCapacity getPersistedFactTradeCapacity(FactTradeCapacity factTradeCapacity) {
        return factTradeCapacityRepository.findById(factTradeCapacity.getId()).orElseThrow();
    }

    protected void assertPersistedFactTradeCapacityToMatchAllProperties(FactTradeCapacity expectedFactTradeCapacity) {
        assertFactTradeCapacityAllPropertiesEquals(expectedFactTradeCapacity, getPersistedFactTradeCapacity(expectedFactTradeCapacity));
    }

    protected void assertPersistedFactTradeCapacityToMatchUpdatableProperties(FactTradeCapacity expectedFactTradeCapacity) {
        assertFactTradeCapacityAllUpdatablePropertiesEquals(
            expectedFactTradeCapacity,
            getPersistedFactTradeCapacity(expectedFactTradeCapacity)
        );
    }
}
