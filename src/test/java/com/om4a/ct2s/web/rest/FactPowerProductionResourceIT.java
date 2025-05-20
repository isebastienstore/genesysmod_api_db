package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactPowerProductionAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactPowerProduction;
import com.om4a.ct2s.repository.FactPowerProductionRepository;
import com.om4a.ct2s.repository.search.FactPowerProductionSearchRepository;
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
 * Integration tests for the {@link FactPowerProductionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactPowerProductionResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/fact-power-productions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-power-productions/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactPowerProductionRepository factPowerProductionRepository;

    @Autowired
    private FactPowerProductionSearchRepository factPowerProductionSearchRepository;

    @Autowired
    private MockMvc restFactPowerProductionMockMvc;

    private FactPowerProduction factPowerProduction;

    private FactPowerProduction insertedFactPowerProduction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactPowerProduction createEntity() {
        return new FactPowerProduction().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactPowerProduction createUpdatedEntity() {
        return new FactPowerProduction().value(UPDATED_VALUE);
    }

    @BeforeEach
    void initTest() {
        factPowerProduction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactPowerProduction != null) {
            factPowerProductionRepository.delete(insertedFactPowerProduction);
            factPowerProductionSearchRepository.delete(insertedFactPowerProduction);
            insertedFactPowerProduction = null;
        }
    }

    @Test
    void createFactPowerProduction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        // Create the FactPowerProduction
        var returnedFactPowerProduction = om.readValue(
            restFactPowerProductionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerProduction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactPowerProduction.class
        );

        // Validate the FactPowerProduction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactPowerProductionUpdatableFieldsEquals(
            returnedFactPowerProduction,
            getPersistedFactPowerProduction(returnedFactPowerProduction)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactPowerProduction = returnedFactPowerProduction;
    }

    @Test
    void createFactPowerProductionWithExistingId() throws Exception {
        // Create the FactPowerProduction with an existing ID
        factPowerProduction.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactPowerProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerProduction)))
            .andExpect(status().isBadRequest());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        // set the field null
        factPowerProduction.setValue(null);

        // Create the FactPowerProduction, which fails.

        restFactPowerProductionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerProduction)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactPowerProductions() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);

        // Get all the factPowerProductionList
        restFactPowerProductionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factPowerProduction.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    void getFactPowerProduction() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);

        // Get the factPowerProduction
        restFactPowerProductionMockMvc
            .perform(get(ENTITY_API_URL_ID, factPowerProduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factPowerProduction.getId()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    void getNonExistingFactPowerProduction() throws Exception {
        // Get the factPowerProduction
        restFactPowerProductionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactPowerProduction() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factPowerProductionSearchRepository.save(factPowerProduction);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());

        // Update the factPowerProduction
        FactPowerProduction updatedFactPowerProduction = factPowerProductionRepository.findById(factPowerProduction.getId()).orElseThrow();
        updatedFactPowerProduction.value(UPDATED_VALUE);

        restFactPowerProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactPowerProduction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactPowerProduction))
            )
            .andExpect(status().isOk());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactPowerProductionToMatchAllProperties(updatedFactPowerProduction);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactPowerProduction> factPowerProductionSearchList = Streamable.of(
                    factPowerProductionSearchRepository.findAll()
                ).toList();
                FactPowerProduction testFactPowerProductionSearch = factPowerProductionSearchList.get(searchDatabaseSizeAfter - 1);

                assertFactPowerProductionAllPropertiesEquals(testFactPowerProductionSearch, updatedFactPowerProduction);
            });
    }

    @Test
    void putNonExistingFactPowerProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        factPowerProduction.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactPowerProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factPowerProduction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factPowerProduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactPowerProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        factPowerProduction.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerProductionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factPowerProduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactPowerProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        factPowerProduction.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerProductionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerProduction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactPowerProductionWithPatch() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factPowerProduction using partial update
        FactPowerProduction partialUpdatedFactPowerProduction = new FactPowerProduction();
        partialUpdatedFactPowerProduction.setId(factPowerProduction.getId());

        restFactPowerProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactPowerProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactPowerProduction))
            )
            .andExpect(status().isOk());

        // Validate the FactPowerProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactPowerProductionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactPowerProduction, factPowerProduction),
            getPersistedFactPowerProduction(factPowerProduction)
        );
    }

    @Test
    void fullUpdateFactPowerProductionWithPatch() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factPowerProduction using partial update
        FactPowerProduction partialUpdatedFactPowerProduction = new FactPowerProduction();
        partialUpdatedFactPowerProduction.setId(factPowerProduction.getId());

        partialUpdatedFactPowerProduction.value(UPDATED_VALUE);

        restFactPowerProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactPowerProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactPowerProduction))
            )
            .andExpect(status().isOk());

        // Validate the FactPowerProduction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactPowerProductionUpdatableFieldsEquals(
            partialUpdatedFactPowerProduction,
            getPersistedFactPowerProduction(partialUpdatedFactPowerProduction)
        );
    }

    @Test
    void patchNonExistingFactPowerProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        factPowerProduction.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactPowerProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factPowerProduction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factPowerProduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactPowerProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        factPowerProduction.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerProductionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factPowerProduction))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactPowerProduction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        factPowerProduction.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerProductionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factPowerProduction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactPowerProduction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactPowerProduction() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);
        factPowerProductionRepository.save(factPowerProduction);
        factPowerProductionSearchRepository.save(factPowerProduction);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factPowerProduction
        restFactPowerProductionMockMvc
            .perform(delete(ENTITY_API_URL_ID, factPowerProduction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerProductionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactPowerProduction() throws Exception {
        // Initialize the database
        insertedFactPowerProduction = factPowerProductionRepository.save(factPowerProduction);
        factPowerProductionSearchRepository.save(factPowerProduction);

        // Search the factPowerProduction
        restFactPowerProductionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factPowerProduction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factPowerProduction.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    protected long getRepositoryCount() {
        return factPowerProductionRepository.count();
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

    protected FactPowerProduction getPersistedFactPowerProduction(FactPowerProduction factPowerProduction) {
        return factPowerProductionRepository.findById(factPowerProduction.getId()).orElseThrow();
    }

    protected void assertPersistedFactPowerProductionToMatchAllProperties(FactPowerProduction expectedFactPowerProduction) {
        assertFactPowerProductionAllPropertiesEquals(
            expectedFactPowerProduction,
            getPersistedFactPowerProduction(expectedFactPowerProduction)
        );
    }

    protected void assertPersistedFactPowerProductionToMatchUpdatableProperties(FactPowerProduction expectedFactPowerProduction) {
        assertFactPowerProductionAllUpdatablePropertiesEquals(
            expectedFactPowerProduction,
            getPersistedFactPowerProduction(expectedFactPowerProduction)
        );
    }
}
