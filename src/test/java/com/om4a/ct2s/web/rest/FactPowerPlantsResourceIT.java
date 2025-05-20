package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactPowerPlantsAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactPowerPlants;
import com.om4a.ct2s.domain.enumeration.StatusType;
import com.om4a.ct2s.repository.FactPowerPlantsRepository;
import com.om4a.ct2s.repository.search.FactPowerPlantsSearchRepository;
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
 * Integration tests for the {@link FactPowerPlantsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactPowerPlantsResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_INTALLED_CAPACITY = 1D;
    private static final Double UPDATED_INTALLED_CAPACITY = 2D;

    private static final Double DEFAULT_AVAILABILITY_CAPACITY = 1D;
    private static final Double UPDATED_AVAILABILITY_CAPACITY = 2D;

    private static final StatusType DEFAULT_STATUS = StatusType.OPERATIONAL;
    private static final StatusType UPDATED_STATUS = StatusType.NO_OPERATIONAL;

    private static final String ENTITY_API_URL = "/api/fact-power-plants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-power-plants/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactPowerPlantsRepository factPowerPlantsRepository;

    @Autowired
    private FactPowerPlantsSearchRepository factPowerPlantsSearchRepository;

    @Autowired
    private MockMvc restFactPowerPlantsMockMvc;

    private FactPowerPlants factPowerPlants;

    private FactPowerPlants insertedFactPowerPlants;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactPowerPlants createEntity() {
        return new FactPowerPlants()
            .name(DEFAULT_NAME)
            .intalledCapacity(DEFAULT_INTALLED_CAPACITY)
            .availabilityCapacity(DEFAULT_AVAILABILITY_CAPACITY)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactPowerPlants createUpdatedEntity() {
        return new FactPowerPlants()
            .name(UPDATED_NAME)
            .intalledCapacity(UPDATED_INTALLED_CAPACITY)
            .availabilityCapacity(UPDATED_AVAILABILITY_CAPACITY)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        factPowerPlants = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactPowerPlants != null) {
            factPowerPlantsRepository.delete(insertedFactPowerPlants);
            factPowerPlantsSearchRepository.delete(insertedFactPowerPlants);
            insertedFactPowerPlants = null;
        }
    }

    @Test
    void createFactPowerPlants() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        // Create the FactPowerPlants
        var returnedFactPowerPlants = om.readValue(
            restFactPowerPlantsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerPlants)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactPowerPlants.class
        );

        // Validate the FactPowerPlants in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactPowerPlantsUpdatableFieldsEquals(returnedFactPowerPlants, getPersistedFactPowerPlants(returnedFactPowerPlants));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactPowerPlants = returnedFactPowerPlants;
    }

    @Test
    void createFactPowerPlantsWithExistingId() throws Exception {
        // Create the FactPowerPlants with an existing ID
        factPowerPlants.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactPowerPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerPlants)))
            .andExpect(status().isBadRequest());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        // set the field null
        factPowerPlants.setName(null);

        // Create the FactPowerPlants, which fails.

        restFactPowerPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerPlants)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkIntalledCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        // set the field null
        factPowerPlants.setIntalledCapacity(null);

        // Create the FactPowerPlants, which fails.

        restFactPowerPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerPlants)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAvailabilityCapacityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        // set the field null
        factPowerPlants.setAvailabilityCapacity(null);

        // Create the FactPowerPlants, which fails.

        restFactPowerPlantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerPlants)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactPowerPlants() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);

        // Get all the factPowerPlantsList
        restFactPowerPlantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factPowerPlants.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].intalledCapacity").value(hasItem(DEFAULT_INTALLED_CAPACITY)))
            .andExpect(jsonPath("$.[*].availabilityCapacity").value(hasItem(DEFAULT_AVAILABILITY_CAPACITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    void getFactPowerPlants() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);

        // Get the factPowerPlants
        restFactPowerPlantsMockMvc
            .perform(get(ENTITY_API_URL_ID, factPowerPlants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factPowerPlants.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.intalledCapacity").value(DEFAULT_INTALLED_CAPACITY))
            .andExpect(jsonPath("$.availabilityCapacity").value(DEFAULT_AVAILABILITY_CAPACITY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    void getNonExistingFactPowerPlants() throws Exception {
        // Get the factPowerPlants
        restFactPowerPlantsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactPowerPlants() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factPowerPlantsSearchRepository.save(factPowerPlants);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());

        // Update the factPowerPlants
        FactPowerPlants updatedFactPowerPlants = factPowerPlantsRepository.findById(factPowerPlants.getId()).orElseThrow();
        updatedFactPowerPlants
            .name(UPDATED_NAME)
            .intalledCapacity(UPDATED_INTALLED_CAPACITY)
            .availabilityCapacity(UPDATED_AVAILABILITY_CAPACITY)
            .status(UPDATED_STATUS);

        restFactPowerPlantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactPowerPlants.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactPowerPlants))
            )
            .andExpect(status().isOk());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactPowerPlantsToMatchAllProperties(updatedFactPowerPlants);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactPowerPlants> factPowerPlantsSearchList = Streamable.of(factPowerPlantsSearchRepository.findAll()).toList();
                FactPowerPlants testFactPowerPlantsSearch = factPowerPlantsSearchList.get(searchDatabaseSizeAfter - 1);

                assertFactPowerPlantsAllPropertiesEquals(testFactPowerPlantsSearch, updatedFactPowerPlants);
            });
    }

    @Test
    void putNonExistingFactPowerPlants() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        factPowerPlants.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactPowerPlantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factPowerPlants.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factPowerPlants))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactPowerPlants() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        factPowerPlants.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerPlantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factPowerPlants))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactPowerPlants() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        factPowerPlants.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerPlantsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factPowerPlants)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactPowerPlantsWithPatch() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factPowerPlants using partial update
        FactPowerPlants partialUpdatedFactPowerPlants = new FactPowerPlants();
        partialUpdatedFactPowerPlants.setId(factPowerPlants.getId());

        partialUpdatedFactPowerPlants.status(UPDATED_STATUS);

        restFactPowerPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactPowerPlants.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactPowerPlants))
            )
            .andExpect(status().isOk());

        // Validate the FactPowerPlants in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactPowerPlantsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactPowerPlants, factPowerPlants),
            getPersistedFactPowerPlants(factPowerPlants)
        );
    }

    @Test
    void fullUpdateFactPowerPlantsWithPatch() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factPowerPlants using partial update
        FactPowerPlants partialUpdatedFactPowerPlants = new FactPowerPlants();
        partialUpdatedFactPowerPlants.setId(factPowerPlants.getId());

        partialUpdatedFactPowerPlants
            .name(UPDATED_NAME)
            .intalledCapacity(UPDATED_INTALLED_CAPACITY)
            .availabilityCapacity(UPDATED_AVAILABILITY_CAPACITY)
            .status(UPDATED_STATUS);

        restFactPowerPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactPowerPlants.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactPowerPlants))
            )
            .andExpect(status().isOk());

        // Validate the FactPowerPlants in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactPowerPlantsUpdatableFieldsEquals(
            partialUpdatedFactPowerPlants,
            getPersistedFactPowerPlants(partialUpdatedFactPowerPlants)
        );
    }

    @Test
    void patchNonExistingFactPowerPlants() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        factPowerPlants.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactPowerPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factPowerPlants.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factPowerPlants))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactPowerPlants() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        factPowerPlants.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerPlantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factPowerPlants))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactPowerPlants() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        factPowerPlants.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactPowerPlantsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factPowerPlants)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactPowerPlants in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactPowerPlants() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);
        factPowerPlantsRepository.save(factPowerPlants);
        factPowerPlantsSearchRepository.save(factPowerPlants);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factPowerPlants
        restFactPowerPlantsMockMvc
            .perform(delete(ENTITY_API_URL_ID, factPowerPlants.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factPowerPlantsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactPowerPlants() throws Exception {
        // Initialize the database
        insertedFactPowerPlants = factPowerPlantsRepository.save(factPowerPlants);
        factPowerPlantsSearchRepository.save(factPowerPlants);

        // Search the factPowerPlants
        restFactPowerPlantsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factPowerPlants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factPowerPlants.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].intalledCapacity").value(hasItem(DEFAULT_INTALLED_CAPACITY)))
            .andExpect(jsonPath("$.[*].availabilityCapacity").value(hasItem(DEFAULT_AVAILABILITY_CAPACITY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return factPowerPlantsRepository.count();
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

    protected FactPowerPlants getPersistedFactPowerPlants(FactPowerPlants factPowerPlants) {
        return factPowerPlantsRepository.findById(factPowerPlants.getId()).orElseThrow();
    }

    protected void assertPersistedFactPowerPlantsToMatchAllProperties(FactPowerPlants expectedFactPowerPlants) {
        assertFactPowerPlantsAllPropertiesEquals(expectedFactPowerPlants, getPersistedFactPowerPlants(expectedFactPowerPlants));
    }

    protected void assertPersistedFactPowerPlantsToMatchUpdatableProperties(FactPowerPlants expectedFactPowerPlants) {
        assertFactPowerPlantsAllUpdatablePropertiesEquals(expectedFactPowerPlants, getPersistedFactPowerPlants(expectedFactPowerPlants));
    }
}
