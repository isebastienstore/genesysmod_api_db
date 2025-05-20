package com.om4a.ct2s.web.rest;

import static com.om4a.ct2s.domain.TechnologyAsserts.*;
import static com.om4a.ct2s.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.om4a.ct2s.IntegrationTest;
import com.om4a.ct2s.domain.Technology;
import com.om4a.ct2s.domain.enumeration.CategoryType;
import com.om4a.ct2s.repository.TechnologyRepository;
import com.om4a.ct2s.repository.search.TechnologySearchRepository;
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
 * Integration tests for the {@link TechnologyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TechnologyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final CategoryType DEFAULT_CATEGORY = CategoryType.THERMAL;
    private static final CategoryType UPDATED_CATEGORY = CategoryType.RENEWABLE;

    private static final String ENTITY_API_URL = "/api/technologies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/technologies/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TechnologyRepository technologyRepository;

    @Autowired
    private TechnologySearchRepository technologySearchRepository;

    @Autowired
    private MockMvc restTechnologyMockMvc;

    private Technology technology;

    private Technology insertedTechnology;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technology createEntity() {
        return new Technology().name(DEFAULT_NAME).category(DEFAULT_CATEGORY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Technology createUpdatedEntity() {
        return new Technology().name(UPDATED_NAME).category(UPDATED_CATEGORY);
    }

    @BeforeEach
    void initTest() {
        technology = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTechnology != null) {
            technologyRepository.delete(insertedTechnology);
            technologySearchRepository.delete(insertedTechnology);
            insertedTechnology = null;
        }
    }

    @Test
    void createTechnology() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        // Create the Technology
        var returnedTechnology = om.readValue(
            restTechnologyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technology)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Technology.class
        );

        // Validate the Technology in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTechnologyUpdatableFieldsEquals(returnedTechnology, getPersistedTechnology(returnedTechnology));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTechnology = returnedTechnology;
    }

    @Test
    void createTechnologyWithExistingId() throws Exception {
        // Create the Technology with an existing ID
        technology.setId("existing_id");

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechnologyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technology)))
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        // set the field null
        technology.setName(null);

        // Create the Technology, which fails.

        restTechnologyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technology)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        // set the field null
        technology.setCategory(null);

        // Create the Technology, which fails.

        restTechnologyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technology)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllTechnologies() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);

        // Get all the technologyList
        restTechnologyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technology.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    void getTechnology() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);

        // Get the technology
        restTechnologyMockMvc
            .perform(get(ENTITY_API_URL_ID, technology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(technology.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    void getNonExistingTechnology() throws Exception {
        // Get the technology
        restTechnologyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingTechnology() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        technologySearchRepository.save(technology);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());

        // Update the technology
        Technology updatedTechnology = technologyRepository.findById(technology.getId()).orElseThrow();
        updatedTechnology.name(UPDATED_NAME).category(UPDATED_CATEGORY);

        restTechnologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTechnology.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTechnology))
            )
            .andExpect(status().isOk());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTechnologyToMatchAllProperties(updatedTechnology);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Technology> technologySearchList = Streamable.of(technologySearchRepository.findAll()).toList();
                Technology testTechnologySearch = technologySearchList.get(searchDatabaseSizeAfter - 1);

                assertTechnologyAllPropertiesEquals(testTechnologySearch, updatedTechnology);
            });
    }

    @Test
    void putNonExistingTechnology() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        technology.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, technology.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technology))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchTechnology() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        technology.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnologyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(technology))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamTechnology() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        technology.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnologyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(technology)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateTechnologyWithPatch() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the technology using partial update
        Technology partialUpdatedTechnology = new Technology();
        partialUpdatedTechnology.setId(technology.getId());

        partialUpdatedTechnology.name(UPDATED_NAME).category(UPDATED_CATEGORY);

        restTechnologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechnology.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechnology))
            )
            .andExpect(status().isOk());

        // Validate the Technology in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechnologyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTechnology, technology),
            getPersistedTechnology(technology)
        );
    }

    @Test
    void fullUpdateTechnologyWithPatch() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the technology using partial update
        Technology partialUpdatedTechnology = new Technology();
        partialUpdatedTechnology.setId(technology.getId());

        partialUpdatedTechnology.name(UPDATED_NAME).category(UPDATED_CATEGORY);

        restTechnologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTechnology.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTechnology))
            )
            .andExpect(status().isOk());

        // Validate the Technology in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTechnologyUpdatableFieldsEquals(partialUpdatedTechnology, getPersistedTechnology(partialUpdatedTechnology));
    }

    @Test
    void patchNonExistingTechnology() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        technology.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechnologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, technology.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(technology))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchTechnology() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        technology.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnologyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(technology))
            )
            .andExpect(status().isBadRequest());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamTechnology() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        technology.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTechnologyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(technology)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Technology in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteTechnology() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);
        technologyRepository.save(technology);
        technologySearchRepository.save(technology);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the technology
        restTechnologyMockMvc
            .perform(delete(ENTITY_API_URL_ID, technology.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(technologySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchTechnology() throws Exception {
        // Initialize the database
        insertedTechnology = technologyRepository.save(technology);
        technologySearchRepository.save(technology);

        // Search the technology
        restTechnologyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + technology.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(technology.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    protected long getRepositoryCount() {
        return technologyRepository.count();
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

    protected Technology getPersistedTechnology(Technology technology) {
        return technologyRepository.findById(technology.getId()).orElseThrow();
    }

    protected void assertPersistedTechnologyToMatchAllProperties(Technology expectedTechnology) {
        assertTechnologyAllPropertiesEquals(expectedTechnology, getPersistedTechnology(expectedTechnology));
    }

    protected void assertPersistedTechnologyToMatchUpdatableProperties(Technology expectedTechnology) {
        assertTechnologyAllUpdatablePropertiesEquals(expectedTechnology, getPersistedTechnology(expectedTechnology));
    }
}
