package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.FactTransportAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.FactTransport;
import com.om4a.ct2s.domain.enumeration.ModalType;
import com.om4a.ct2s.repository.FactTransportRepository;
import com.om4a.ct2s.repository.search.FactTransportSearchRepository;
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
 * Integration tests for the {@link FactTransportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FactTransportResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final ModalType DEFAULT_TYPE_OF_MOBILITY = ModalType.MOBILITY_PASSENGER_ROAD;
    private static final ModalType UPDATED_TYPE_OF_MOBILITY = ModalType.MOBILITY_PASSENGER_ROAD_CONV;

    private static final String ENTITY_API_URL = "/api/fact-transports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/fact-transports/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FactTransportRepository factTransportRepository;

    @Autowired
    private FactTransportSearchRepository factTransportSearchRepository;

    @Autowired
    private MockMvc restFactTransportMockMvc;

    private FactTransport factTransport;

    private FactTransport insertedFactTransport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactTransport createEntity() {
        return new FactTransport().value(DEFAULT_VALUE).typeOfMobility(DEFAULT_TYPE_OF_MOBILITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FactTransport createUpdatedEntity() {
        return new FactTransport().value(UPDATED_VALUE).typeOfMobility(UPDATED_TYPE_OF_MOBILITY);
    }

    @BeforeEach
    void initTest() {
        factTransport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFactTransport != null) {
            factTransportRepository.delete(insertedFactTransport);
            factTransportSearchRepository.delete(insertedFactTransport);
            insertedFactTransport = null;
        }
    }

    @Test
    void createFactTransport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        // Create the FactTransport
        var returnedFactTransport = om.readValue(
            restFactTransportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTransport)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FactTransport.class
        );

        // Validate the FactTransport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFactTransportUpdatableFieldsEquals(returnedFactTransport, getPersistedFactTransport(returnedFactTransport));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFactTransport = returnedFactTransport;
    }

    @Test
    void createFactTransportWithExistingId() throws Exception {
        // Create the FactTransport with an existing ID
        factTransport.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFactTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTransport)))
            .andExpect(status().isBadRequest());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        // set the field null
        factTransport.setValue(null);

        // Create the FactTransport, which fails.

        restFactTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTransport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkTypeOfMobilityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        // set the field null
        factTransport.setTypeOfMobility(null);

        // Create the FactTransport, which fails.

        restFactTransportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTransport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllFactTransports() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);

        // Get all the factTransportList
        restFactTransportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factTransport.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].typeOfMobility").value(hasItem(DEFAULT_TYPE_OF_MOBILITY.toString())));
    }

    @Test
    void getFactTransport() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);

        // Get the factTransport
        restFactTransportMockMvc
            .perform(get(ENTITY_API_URL_ID, factTransport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factTransport.getId()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.typeOfMobility").value(DEFAULT_TYPE_OF_MOBILITY.toString()));
    }

    @Test
    void getNonExistingFactTransport() throws Exception {
        // Get the factTransport
        restFactTransportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingFactTransport() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        factTransportSearchRepository.save(factTransport);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());

        // Update the factTransport
        FactTransport updatedFactTransport = factTransportRepository.findById(factTransport.getId()).orElseThrow();
        updatedFactTransport.value(UPDATED_VALUE).typeOfMobility(UPDATED_TYPE_OF_MOBILITY);

        restFactTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFactTransport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFactTransport))
            )
            .andExpect(status().isOk());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFactTransportToMatchAllProperties(updatedFactTransport);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FactTransport> factTransportSearchList = Streamable.of(factTransportSearchRepository.findAll()).toList();
                FactTransport testFactTransportSearch = factTransportSearchList.get(searchDatabaseSizeAfter - 1);

                assertFactTransportAllPropertiesEquals(testFactTransportSearch, updatedFactTransport);
            });
    }

    @Test
    void putNonExistingFactTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        factTransport.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, factTransport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factTransport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchFactTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        factTransport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTransportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(factTransport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamFactTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        factTransport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTransportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(factTransport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateFactTransportWithPatch() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factTransport using partial update
        FactTransport partialUpdatedFactTransport = new FactTransport();
        partialUpdatedFactTransport.setId(factTransport.getId());

        partialUpdatedFactTransport.value(UPDATED_VALUE).typeOfMobility(UPDATED_TYPE_OF_MOBILITY);

        restFactTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactTransport))
            )
            .andExpect(status().isOk());

        // Validate the FactTransport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactTransportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFactTransport, factTransport),
            getPersistedFactTransport(factTransport)
        );
    }

    @Test
    void fullUpdateFactTransportWithPatch() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the factTransport using partial update
        FactTransport partialUpdatedFactTransport = new FactTransport();
        partialUpdatedFactTransport.setId(factTransport.getId());

        partialUpdatedFactTransport.value(UPDATED_VALUE).typeOfMobility(UPDATED_TYPE_OF_MOBILITY);

        restFactTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFactTransport))
            )
            .andExpect(status().isOk());

        // Validate the FactTransport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFactTransportUpdatableFieldsEquals(partialUpdatedFactTransport, getPersistedFactTransport(partialUpdatedFactTransport));
    }

    @Test
    void patchNonExistingFactTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        factTransport.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFactTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, factTransport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factTransport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchFactTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        factTransport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTransportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(factTransport))
            )
            .andExpect(status().isBadRequest());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamFactTransport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        factTransport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFactTransportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(factTransport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FactTransport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteFactTransport() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);
        factTransportRepository.save(factTransport);
        factTransportSearchRepository.save(factTransport);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the factTransport
        restFactTransportMockMvc
            .perform(delete(ENTITY_API_URL_ID, factTransport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(factTransportSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchFactTransport() throws Exception {
        // Initialize the database
        insertedFactTransport = factTransportRepository.save(factTransport);
        factTransportSearchRepository.save(factTransport);

        // Search the factTransport
        restFactTransportMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + factTransport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factTransport.getId())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].typeOfMobility").value(hasItem(DEFAULT_TYPE_OF_MOBILITY.toString())));
    }

    protected long getRepositoryCount() {
        return factTransportRepository.count();
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

    protected FactTransport getPersistedFactTransport(FactTransport factTransport) {
        return factTransportRepository.findById(factTransport.getId()).orElseThrow();
    }

    protected void assertPersistedFactTransportToMatchAllProperties(FactTransport expectedFactTransport) {
        assertFactTransportAllPropertiesEquals(expectedFactTransport, getPersistedFactTransport(expectedFactTransport));
    }

    protected void assertPersistedFactTransportToMatchUpdatableProperties(FactTransport expectedFactTransport) {
        assertFactTransportAllUpdatablePropertiesEquals(expectedFactTransport, getPersistedFactTransport(expectedFactTransport));
    }
}
