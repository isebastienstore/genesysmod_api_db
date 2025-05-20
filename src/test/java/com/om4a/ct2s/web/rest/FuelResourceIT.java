package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FuelAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.Fuel;
import com.om4a.ct2s.repository.FuelRepository;
import com.om4a.ct2s.repository.search.FuelSearchRepository;
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
 * Integration tests for the {@link FuelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FuelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fuels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fuels/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FuelRepository fuelRepository;

    @Autowired
    private FuelSearchRepository fuelSearchRepository;

    @Autowired
    private MockMvc restFuelMockMvc;

    private Fuel fuel;

    private Fuel insertedFuel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fuel createEntity() {
        return new Fuel().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fuel createUpdatedEntity() {
        return new Fuel().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        fuel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFuel != null) {
            fuelRepository.delete(insertedFuel);
            fuelSearchRepository.delete(insertedFuel);
            insertedFuel = null;
        }
    }

    @Test
    void createFuel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        // Create the Fuel
        var returnedFuel = om.readValue(
            restFuelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Fuel.class
        );

        // Validate the Fuel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFuelUpdatableFieldsEquals(returnedFuel, getPersistedFuel(returnedFuel));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFuel = returnedFuel;
    }

    @Test
    void createFuelWithExistingId() throws Exception {
        // Create the Fuel with an existing ID
        fuel.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFuelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuel)))
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        // set the field null
        fuel.setName(null);

        // Create the Fuel, which fails.

        restFuelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuel)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFuels() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);

        // Get all the fuelList
        restFuelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fuel.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    void getFuel() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);

        // Get the fuel
        restFuelMockMvc
            .perform(get(ENTITY_API_URL_ID, fuel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fuel.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    void getNonExistingFuel() throws Exception {
        // Get the fuel
        restFuelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFuel() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        fuelSearchRepository.save(fuel);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());

        // Update the fuel
        Fuel updatedFuel = fuelRepository.findById(fuel.getId()).orElseThrow();
        updatedFuel.name(UPDATED_NAME);

        restFuelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFuel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFuel))
            )
            .andExpect(status().isOk());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFuelToMatchAllProperties(updatedFuel);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Fuel> fuelSearchList = Streamable.of(fuelSearchRepository.findAll()).toList();
                Fuel testFuelSearch = fuelSearchList.get(searchDatabaseSizeAfter - 1);

                assertFuelAllPropertiesEquals(testFuelSearch, updatedFuel);
            });
    }

    @Test
    void putNonExistingFuel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        fuel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(put(ENTITY_API_URL_ID, fuel.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuel)))
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFuel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        fuel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(fuel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFuel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        fuel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(fuel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFuelWithPatch() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fuel using partial update
        Fuel partialUpdatedFuel = new Fuel();
        partialUpdatedFuel.setId(fuel.getId());

        partialUpdatedFuel.name(UPDATED_NAME);

        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFuel))
            )
            .andExpect(status().isOk());

        // Validate the Fuel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFuelUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFuel, fuel), getPersistedFuel(fuel));
    }

    @Test
    void fullUpdateFuelWithPatch() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the fuel using partial update
        Fuel partialUpdatedFuel = new Fuel();
        partialUpdatedFuel.setId(fuel.getId());

        partialUpdatedFuel.name(UPDATED_NAME);

        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFuel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFuel))
            )
            .andExpect(status().isOk());

        // Validate the Fuel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFuelUpdatableFieldsEquals(partialUpdatedFuel, getPersistedFuel(partialUpdatedFuel));
    }

    @Test
    void patchNonExistingFuel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        fuel.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(patch(ENTITY_API_URL_ID, fuel.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fuel)))
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFuel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        fuel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(fuel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFuel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        fuel.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFuelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(fuel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fuel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFuel() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);
        fuelRepository.save(fuel);
        fuelSearchRepository.save(fuel);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the fuel
        restFuelMockMvc
            .perform(delete(ENTITY_API_URL_ID, fuel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(fuelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFuel() throws Exception {
        // Initialize the database
        insertedFuel = fuelRepository.save(fuel);
        fuelSearchRepository.save(fuel);

        // Search the fuel
        restFuelMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + fuel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fuel.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    protected long getRepositoryCount() {
        return fuelRepository.count();
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

    protected Fuel getPersistedFuel(Fuel fuel) {
        return fuelRepository.findById(fuel.getId()).orElseThrow();
    }

    protected void assertPersistedFuelToMatchAllProperties(Fuel expectedFuel) {
        assertFuelAllPropertiesEquals(expectedFuel, getPersistedFuel(expectedFuel));
    }

    protected void assertPersistedFuelToMatchUpdatableProperties(Fuel expectedFuel) {
        assertFuelAllUpdatablePropertiesEquals(expectedFuel, getPersistedFuel(expectedFuel));
    }
}
