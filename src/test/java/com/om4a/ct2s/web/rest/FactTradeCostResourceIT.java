package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactTradeCostAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactTradeCost;
import com.om4a.ct2s.repository.FactTradeCostRepository;
import com.om4a.ct2s.repository.search.FactTradeCostSearchRepository;
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
 * Integration tests for the {@link FactTradeCostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactTradeCostResourceIT {

    private static final Double DEFAULT_FIXED_COST = 1D;
    private static final Double UPDATED_FIXED_COST = 2D;

    private static final Double DEFAULT_VARIABLE_COST = 1D;
    private static final Double UPDATED_VARIABLE_COST = 2D;

    private static final String ENTITY_API_URL = "/api/fact-trade-costs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-trade-costs/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactTradeCostRepository factTradeCostRepository;

    @Autowired
    private FactTradeCostSearchRepository factTradeCostSearchRepository;

    @Autowired
    private MockMvc restFactTradeCostMockMvc;

    private FactTradeCost factTradeCost;

    private FactTradeCost insertedFactTradeCost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactTradeCost createEntity() {
        return new FactTradeCost().fixedCost(DEFAULT_FIXED_COST).variableCost(DEFAULT_VARIABLE_COST);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactTradeCost createUpdatedEntity() {
        return new FactTradeCost().fixedCost(UPDATED_FIXED_COST).variableCost(UPDATED_VARIABLE_COST);
    }

    @BeforeEach
    void initTest() {
        factTradeCost = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactTradeCost != null) {
            factTradeCostRepository.delete(insertedFactTradeCost);
            factTradeCostSearchRepository.delete(insertedFactTradeCost);
            insertedFactTradeCost = null;
        }
    }

    @Test
    void createFactTradeCost() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        // Create the FactTradeCost
        var returnedFactTradeCost = om.readValue(
            restFactTradeCostMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCost)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactTradeCost.class
        );

        // Validate the FactTradeCost in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactTradeCostUpdatableFieldsEquals(returnedFactTradeCost, getPersistedFactTradeCost(returnedFactTradeCost));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactTradeCost = returnedFactTradeCost;
    }

    @Test
    void createFactTradeCostWithExistingId() throws Exception {
        // Create the FactTradeCost with an existing ID
        factTradeCost.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactTradeCostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCost)))
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFixedCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        // set the field null
        factTradeCost.setFixedCost(null);

        // Create the FactTradeCost, which fails.

        restFactTradeCostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCost)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkVariableCostIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        // set the field null
        factTradeCost.setVariableCost(null);

        // Create the FactTradeCost, which fails.

        restFactTradeCostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCost)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactTradeCosts() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);

        // Get all the factTradeCostList
        restFactTradeCostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factTradeCost.getId())))
            .andExpect(jsonPath("$.[*].fixedCost").value(hasItem(DEFAULT_FIXED_COST)))
            .andExpect(jsonPath("$.[*].variableCost").value(hasItem(DEFAULT_VARIABLE_COST)));
    }

    @Test
    void getFactTradeCost() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);

        // Get the factTradeCost
        restFactTradeCostMockMvc
            .perform(get(ENTITY_API_URL_ID, factTradeCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factTradeCost.getId()))
            .andExpect(jsonPath("$.fixedCost").value(DEFAULT_FIXED_COST))
            .andExpect(jsonPath("$.variableCost").value(DEFAULT_VARIABLE_COST));
    }

    @Test
    void getNonExistingFactTradeCost() throws Exception {
        // Get the factTradeCost
        restFactTradeCostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactTradeCost() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factTradeCostSearchRepository.save(factTradeCost);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());

        // Update the factTradeCost
        FactTradeCost updatedFactTradeCost = factTradeCostRepository.findById(factTradeCost.getId()).orElseThrow();
        updatedFactTradeCost.fixedCost(UPDATED_FIXED_COST).variableCost(UPDATED_VARIABLE_COST);

        restFactTradeCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactTradeCost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactTradeCost))
            )
            .andExpect(status().isOk());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactTradeCostToMatchAllProperties(updatedFactTradeCost);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactTradeCost> factTradeCostSearchList = Streamable.of(factTradeCostSearchRepository.findAll()).toList();
                FactTradeCost testFactTradeCostSearch = factTradeCostSearchList.get(searchDatabaseSizeAfter - 1);

                assertFactTradeCostAllPropertiesEquals(testFactTradeCostSearch, updatedFactTradeCost);
            });
    }

    @Test
    void putNonExistingFactTradeCost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        factTradeCost.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactTradeCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factTradeCost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factTradeCost))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactTradeCost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        factTradeCost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factTradeCost))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactTradeCost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        factTradeCost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTradeCost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactTradeCostWithPatch() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factTradeCost using partial update
        FactTradeCost partialUpdatedFactTradeCost = new FactTradeCost();
        partialUpdatedFactTradeCost.setId(factTradeCost.getId());

        partialUpdatedFactTradeCost.variableCost(UPDATED_VARIABLE_COST);

        restFactTradeCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactTradeCost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactTradeCost))
            )
            .andExpect(status().isOk());

        // Validate the FactTradeCost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactTradeCostUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactTradeCost, factTradeCost),
            getPersistedFactTradeCost(factTradeCost)
        );
    }

    @Test
    void fullUpdateFactTradeCostWithPatch() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factTradeCost using partial update
        FactTradeCost partialUpdatedFactTradeCost = new FactTradeCost();
        partialUpdatedFactTradeCost.setId(factTradeCost.getId());

        partialUpdatedFactTradeCost.fixedCost(UPDATED_FIXED_COST).variableCost(UPDATED_VARIABLE_COST);

        restFactTradeCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactTradeCost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactTradeCost))
            )
            .andExpect(status().isOk());

        // Validate the FactTradeCost in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactTradeCostUpdatableFieldsEquals(partialUpdatedFactTradeCost, getPersistedFactTradeCost(partialUpdatedFactTradeCost));
    }

    @Test
    void patchNonExistingFactTradeCost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        factTradeCost.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactTradeCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factTradeCost.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factTradeCost))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactTradeCost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        factTradeCost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factTradeCost))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactTradeCost() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        factTradeCost.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTradeCostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factTradeCost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactTradeCost in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactTradeCost() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);
        factTradeCostRepository.save(factTradeCost);
        factTradeCostSearchRepository.save(factTradeCost);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factTradeCost
        restFactTradeCostMockMvc
            .perform(delete(ENTITY_API_URL_ID, factTradeCost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTradeCostSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactTradeCost() throws Exception {
        // Initialize the database
        insertedFactTradeCost = factTradeCostRepository.save(factTradeCost);
        factTradeCostSearchRepository.save(factTradeCost);

        // Search the factTradeCost
        restFactTradeCostMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factTradeCost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factTradeCost.getId())))
            .andExpect(jsonPath("$.[*].fixedCost").value(hasItem(DEFAULT_FIXED_COST)))
            .andExpect(jsonPath("$.[*].variableCost").value(hasItem(DEFAULT_VARIABLE_COST)));
    }

    protected long getRepositoryCount() {
        return factTradeCostRepository.count();
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

    protected FactTradeCost getPersistedFactTradeCost(FactTradeCost factTradeCost) {
        return factTradeCostRepository.findById(factTradeCost.getId()).orElseThrow();
    }

    protected void assertPersistedFactTradeCostToMatchAllProperties(FactTradeCost expectedFactTradeCost) {
        assertFactTradeCostAllPropertiesEquals(expectedFactTradeCost, getPersistedFactTradeCost(expectedFactTradeCost));
    }

    protected void assertPersistedFactTradeCostToMatchUpdatableProperties(FactTradeCost expectedFactTradeCost) {
        assertFactTradeCostAllUpdatablePropertiesEquals(expectedFactTradeCost, getPersistedFactTradeCost(expectedFactTradeCost));
    }
}
