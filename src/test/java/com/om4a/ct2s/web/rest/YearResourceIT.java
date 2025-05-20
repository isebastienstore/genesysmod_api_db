package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.YearAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.Year;
import com.om4a.ct2s.repository.YearRepository;
import com.om4a.ct2s.repository.search.YearSearchRepository;
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
 * Integration tests for the {@link YearResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class YearResourceIT {

    private static final Integer DEFAULT_YEAR = 1900;
    private static final Integer UPDATED_YEAR = 1901;

    private static final String ENTITY_API_URL = "/api/years";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/years/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private YearRepository yearRepository;

    @Autowired
    private YearSearchRepository yearSearchRepository;

    @Autowired
    private MockMvc restYearMockMvc;

    private Year year;

    private Year insertedYear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Year createEntity() {
        return new Year().year(DEFAULT_YEAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Year createUpdatedEntity() {
        return new Year().year(UPDATED_YEAR);
    }

    @BeforeEach
    void initTest() {
        year = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedYear != null) {
            yearRepository.delete(insertedYear);
            yearSearchRepository.delete(insertedYear);
            insertedYear = null;
        }
    }

    @Test
    void createYear() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        // Create the Year
        var returnedYear = om.readValue(
            restYearMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(year)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Year.class
        );

        // Validate the Year in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertYearUpdatableFieldsEquals(returnedYear, getPersistedYear(returnedYear));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedYear = returnedYear;
    }

    @Test
    void createYearWithExistingId() throws Exception {
        // Create the Year with an existing ID
        year.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(year)))
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        // set the field null
        year.setYear(null);

        // Create the Year, which fails.

        restYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(year)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllYears() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);

        // Get all the yearList
        restYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(year.getId())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    @Test
    void getYear() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);

        // Get the year
        restYearMockMvc
            .perform(get(ENTITY_API_URL_ID, year.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(year.getId()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR));
    }

    @Test
    void getNonExistingYear() throws Exception {
        // Get the year
        restYearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingYear() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        yearSearchRepository.save(year);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());

        // Update the year
        Year updatedYear = yearRepository.findById(year.getId()).orElseThrow();
        updatedYear.year(UPDATED_YEAR);

        restYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedYear.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedYear))
            )
            .andExpect(status().isOk());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedYearToMatchAllProperties(updatedYear);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Year> yearSearchList = Streamable.of(yearSearchRepository.findAll()).toList();
                Year testYearSearch = yearSearchList.get(searchDatabaseSizeAfter - 1);

                assertYearAllPropertiesEquals(testYearSearch, updatedYear);
            });
    }

    @Test
    void putNonExistingYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        year.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYearMockMvc
            .perform(put(ENTITY_API_URL_ID, year.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(year)))
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        year.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(year))
            )
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        year.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYearMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(year)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateYearWithPatch() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the year using partial update
        Year partialUpdatedYear = new Year();
        partialUpdatedYear.setId(year.getId());

        restYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedYear))
            )
            .andExpect(status().isOk());

        // Validate the Year in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertYearUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedYear, year), getPersistedYear(year));
    }

    @Test
    void fullUpdateYearWithPatch() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the year using partial update
        Year partialUpdatedYear = new Year();
        partialUpdatedYear.setId(year.getId());

        partialUpdatedYear.year(UPDATED_YEAR);

        restYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedYear))
            )
            .andExpect(status().isOk());

        // Validate the Year in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertYearUpdatableFieldsEquals(partialUpdatedYear, getPersistedYear(partialUpdatedYear));
    }

    @Test
    void patchNonExistingYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        year.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restYearMockMvc
            .perform(patch(ENTITY_API_URL_ID, year.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(year)))
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        year.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(year))
            )
            .andExpect(status().isBadRequest());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        year.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restYearMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(year)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Year in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteYear() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);
        yearRepository.save(year);
        yearSearchRepository.save(year);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the year
        restYearMockMvc
            .perform(delete(ENTITY_API_URL_ID, year.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(yearSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchYear() throws Exception {
        // Initialize the database
        insertedYear = yearRepository.save(year);
        yearSearchRepository.save(year);

        // Search the year
        restYearMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + year.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(year.getId())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)));
    }

    protected long getRepositoryCount() {
        return yearRepository.count();
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

    protected Year getPersistedYear(Year year) {
        return yearRepository.findById(year.getId()).orElseThrow();
    }

    protected void assertPersistedYearToMatchAllProperties(Year expectedYear) {
        assertYearAllPropertiesEquals(expectedYear, getPersistedYear(expectedYear));
    }

    protected void assertPersistedYearToMatchUpdatableProperties(Year expectedYear) {
        assertYearAllUpdatablePropertiesEquals(expectedYear, getPersistedYear(expectedYear));
    }
}
