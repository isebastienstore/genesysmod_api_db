package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactElectricityConsumptionAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactElectricityConsumption;
import com.om4a.ct2s.repository.FactElectricityConsumptionRepository;
import com.om4a.ct2s.repository.search.FactElectricityConsumptionSearchRepository;
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
 * Integration tests for the {@link FactElectricityConsumptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactElectricityConsumptionResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/fact-electricity-consumptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-electricity-consumptions/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactElectricityConsumptionRepository factElectricityConsumptionRepository;

    @Autowired
    private FactElectricityConsumptionSearchRepository factElectricityConsumptionSearchRepository;

    @Autowired
    private MockMvc restFactElectricityConsumptionMockMvc;

    private FactElectricityConsumption factElectricityConsumption;

    private FactElectricityConsumption insertedFactElectricityConsumption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactElectricityConsumption createEntity() {
        return new FactElectricityConsumption().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactElectricityConsumption createUpdatedEntity() {
        return new FactElectricityConsumption().value(UPDATED_VALUE);
    }

    @BeforeEach
    void initTest() {
        factElectricityConsumption = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactElectricityConsumption != null) {
            factElectricityConsumptionRepository.delete(insertedFactElectricityConsumption);
            factElectricityConsumptionSearchRepository.delete(insertedFactElectricityConsumption);
            insertedFactElectricityConsumption = null;
        }
    }

    @Test
    void createFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        // Create the FactElectricityConsumption
        var returnedFactElectricityConsumption = om.readValue(
            restFactElectricityConsumptionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factElectricityConsumption))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactElectricityConsumption.class
        );

        // Validate the FactElectricityConsumption in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactElectricityConsumptionUpdatableFieldsEquals(
            returnedFactElectricityConsumption,
            getPersistedFactElectricityConsumption(returnedFactElectricityConsumption)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactElectricityConsumption = returnedFactElectricityConsumption;
    }

    @Test
    void createFactElectricityConsumptionWithExistingId() throws Exception {
        // Create the FactElectricityConsumption with an existing ID
        factElectricityConsumption.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactElectricityConsumptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factElectricityConsumption)))
            .andExpect(status().isBadRequest());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        // set the field null
        factElectricityConsumption.setValue(null);

        // Create the FactElectricityConsumption, which fails.

        restFactElectricityConsumptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factElectricityConsumption)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactElectricityConsumptions() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);

        // Get all the factElectricityConsumptionList
        restFactElectricityConsumptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factElectricityConsumption.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    void getFactElectricityConsumption() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);

        // Get the factElectricityConsumption
        restFactElectricityConsumptionMockMvc
            .perform(get(ENTITY_API_URL_ID, factElectricityConsumption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factElectricityConsumption.getId()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    void getNonExistingFactElectricityConsumption() throws Exception {
        // Get the factElectricityConsumption
        restFactElectricityConsumptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactElectricityConsumption() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factElectricityConsumptionSearchRepository.save(factElectricityConsumption);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());

        // Update the factElectricityConsumption
        FactElectricityConsumption updatedFactElectricityConsumption = factElectricityConsumptionRepository
            .findById(factElectricityConsumption.getId())
            .orElseThrow();
        updatedFactElectricityConsumption.value(UPDATED_VALUE);

        restFactElectricityConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactElectricityConsumption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactElectricityConsumption))
            )
            .andExpect(status().isOk());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactElectricityConsumptionToMatchAllProperties(updatedFactElectricityConsumption);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactElectricityConsumption> factElectricityConsumptionSearchList = Streamable.of(
                    factElectricityConsumptionSearchRepository.findAll()
                ).toList();
                FactElectricityConsumption testFactElectricityConsumptionSearch = factElectricityConsumptionSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertFactElectricityConsumptionAllPropertiesEquals(
                    testFactElectricityConsumptionSearch,
                    updatedFactElectricityConsumption
                );
            });
    }

    @Test
    void putNonExistingFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        factElectricityConsumption.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactElectricityConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factElectricityConsumption.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factElectricityConsumption))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        factElectricityConsumption.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactElectricityConsumptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factElectricityConsumption))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        factElectricityConsumption.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactElectricityConsumptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factElectricityConsumption)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactElectricityConsumptionWithPatch() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factElectricityConsumption using partial update
        FactElectricityConsumption partialUpdatedFactElectricityConsumption = new FactElectricityConsumption();
        partialUpdatedFactElectricityConsumption.setId(factElectricityConsumption.getId());

        restFactElectricityConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactElectricityConsumption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactElectricityConsumption))
            )
            .andExpect(status().isOk());

        // Validate the FactElectricityConsumption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactElectricityConsumptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactElectricityConsumption, factElectricityConsumption),
            getPersistedFactElectricityConsumption(factElectricityConsumption)
        );
    }

    @Test
    void fullUpdateFactElectricityConsumptionWithPatch() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factElectricityConsumption using partial update
        FactElectricityConsumption partialUpdatedFactElectricityConsumption = new FactElectricityConsumption();
        partialUpdatedFactElectricityConsumption.setId(factElectricityConsumption.getId());

        partialUpdatedFactElectricityConsumption.value(UPDATED_VALUE);

        restFactElectricityConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactElectricityConsumption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactElectricityConsumption))
            )
            .andExpect(status().isOk());

        // Validate the FactElectricityConsumption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactElectricityConsumptionUpdatableFieldsEquals(
            partialUpdatedFactElectricityConsumption,
            getPersistedFactElectricityConsumption(partialUpdatedFactElectricityConsumption)
        );
    }

    @Test
    void patchNonExistingFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        factElectricityConsumption.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactElectricityConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factElectricityConsumption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factElectricityConsumption))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        factElectricityConsumption.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactElectricityConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factElectricityConsumption))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactElectricityConsumption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        factElectricityConsumption.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactElectricityConsumptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factElectricityConsumption))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactElectricityConsumption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactElectricityConsumption() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);
        factElectricityConsumptionRepository.save(factElectricityConsumption);
        factElectricityConsumptionSearchRepository.save(factElectricityConsumption);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factElectricityConsumption
        restFactElectricityConsumptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, factElectricityConsumption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factElectricityConsumptionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactElectricityConsumption() throws Exception {
        // Initialize the database
        insertedFactElectricityConsumption = factElectricityConsumptionRepository.save(factElectricityConsumption);
        factElectricityConsumptionSearchRepository.save(factElectricityConsumption);

        // Search the factElectricityConsumption
        restFactElectricityConsumptionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factElectricityConsumption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factElectricityConsumption.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    protected long getRepositoryCount() {
        return factElectricityConsumptionRepository.count();
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

    protected FactElectricityConsumption getPersistedFactElectricityConsumption(FactElectricityConsumption factElectricityConsumption) {
        return factElectricityConsumptionRepository.findById(factElectricityConsumption.getId()).orElseThrow();
    }

    protected void assertPersistedFactElectricityConsumptionToMatchAllProperties(
        FactElectricityConsumption expectedFactElectricityConsumption
    ) {
        assertFactElectricityConsumptionAllPropertiesEquals(
            expectedFactElectricityConsumption,
            getPersistedFactElectricityConsumption(expectedFactElectricityConsumption)
        );
    }

    protected void assertPersistedFactElectricityConsumptionToMatchUpdatableProperties(
        FactElectricityConsumption expectedFactElectricityConsumption
    ) {
        assertFactElectricityConsumptionAllUpdatablePropertiesEquals(
            expectedFactElectricityConsumption,
            getPersistedFactElectricityConsumption(expectedFactElectricityConsumption)
        );
    }
}
