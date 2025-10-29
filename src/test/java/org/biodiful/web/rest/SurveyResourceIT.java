package org.biodiful.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.biodiful.domain.SurveyAsserts.*;
import static org.biodiful.web.rest.TestUtil.createUpdateProxyForBean;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.biodiful.IntegrationTest;
import org.biodiful.domain.Answer;
import org.biodiful.domain.ChallengerPool;
import org.biodiful.domain.Survey;
import org.biodiful.domain.enumeration.Language;
import org.biodiful.repository.AnswerRepository;
import org.biodiful.repository.SurveyRepository;
import org.biodiful.security.AuthoritiesConstants;
import org.biodiful.service.dto.SurveyDTO;
import org.biodiful.service.mapper.SurveyMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SurveyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SurveyResourceIT {

    private static final String DEFAULT_SURVEY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SURVEY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURVEY_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SURVEY_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTS_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_FRIENDLY_URL = "AAAAAAAAAA";
    private static final String UPDATED_FRIENDLY_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGOS_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGOS_URL = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_URL = "AAAAAAAAAA";
    private static final String UPDATED_FORM_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPEN = false;
    private static final Boolean UPDATED_OPEN = true;

    private static final Language DEFAULT_LANGUAGE = Language.FR;
    private static final Language UPDATED_LANGUAGE = Language.EN;

    private static final Boolean DEFAULT_UNIQUE_CHALLENGERS = false;
    private static final Boolean UPDATED_UNIQUE_CHALLENGERS = true;

    private static final String ENTITY_API_URL = "/api/surveys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSurveyMockMvc;

    private Survey survey;

    private Survey insertedSurvey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Survey createEntity() {
        Survey survey = new Survey()
            .surveyName(DEFAULT_SURVEY_NAME)
            .surveyDescription(DEFAULT_SURVEY_DESCRIPTION)
            .contactsDescription(DEFAULT_CONTACTS_DESCRIPTION)
            .friendlyURL(DEFAULT_FRIENDLY_URL)
            .photoURL(DEFAULT_PHOTO_URL)
            .logosURL(DEFAULT_LOGOS_URL)
            .formURL(DEFAULT_FORM_URL)
            .open(DEFAULT_OPEN)
            .language(DEFAULT_LANGUAGE)
            .uniqueChallengers(DEFAULT_UNIQUE_CHALLENGERS);

        ChallengerPool pool1 = new ChallengerPool()
            .poolOrder(1)
            .challengersURL("https://api.flickr.com/pool1")
            .numberOfMatches(10)
            .matchesDescription("Test pool 1 description");
        survey.addChallengerPool(pool1);

        return survey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Survey createUpdatedEntity() {
        Survey survey = new Survey()
            .surveyName(UPDATED_SURVEY_NAME)
            .surveyDescription(UPDATED_SURVEY_DESCRIPTION)
            .contactsDescription(UPDATED_CONTACTS_DESCRIPTION)
            .friendlyURL(UPDATED_FRIENDLY_URL)
            .photoURL(UPDATED_PHOTO_URL)
            .logosURL(UPDATED_LOGOS_URL)
            .formURL(UPDATED_FORM_URL)
            .open(UPDATED_OPEN)
            .language(UPDATED_LANGUAGE)
            .uniqueChallengers(UPDATED_UNIQUE_CHALLENGERS);

        ChallengerPool pool1 = new ChallengerPool()
            .poolOrder(1)
            .challengersURL("https://api.flickr.com/pool1-updated")
            .numberOfMatches(20)
            .matchesDescription("Updated pool 1 description");
        survey.addChallengerPool(pool1);

        return survey;
    }

    @BeforeEach
    void initTest() {
        survey = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSurvey != null) {
            surveyRepository.delete(insertedSurvey);
            insertedSurvey = null;
        }
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void createSurvey() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);
        var returnedSurveyDTO = om.readValue(
            restSurveyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SurveyDTO.class
        );

        // Validate the Survey in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSurvey = surveyMapper.toEntity(returnedSurveyDTO);
        assertSurveyUpdatableFieldsEquals(returnedSurvey, getPersistedSurvey(returnedSurvey));

        insertedSurvey = returnedSurvey;
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void createSurveyWithExistingId() throws Exception {
        // Create the Survey with an existing ID
        survey.setId(1L);
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getSurveyResponsesCount() throws Exception {
        Answer entity = AnswerResourceIT.createEntity();
        entity.setSurvey(survey);

        surveyRepository.saveAndFlush(survey);
        answerRepository.saveAndFlush(entity);

        restSurveyMockMvc
            .perform(get("/api/surveys/{id}/judges-count", survey.getId()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }

    @Test
    @Transactional
    void checkSurveyNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setSurveyName(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormURLIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setFormURL(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setOpen(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setLanguage(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUniqueChallengersIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setUniqueChallengers(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSurveys() throws Exception {
        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);

        // Get all the surveyList
        restSurveyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
            .andExpect(jsonPath("$.[*].surveyName").value(hasItem(DEFAULT_SURVEY_NAME)))
            .andExpect(jsonPath("$.[*].surveyDescription").value(hasItem(DEFAULT_SURVEY_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactsDescription").value(hasItem(DEFAULT_CONTACTS_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].friendlyURL").value(hasItem(DEFAULT_FRIENDLY_URL)))
            .andExpect(jsonPath("$.[*].photoURL").value(hasItem(DEFAULT_PHOTO_URL)))
            .andExpect(jsonPath("$.[*].logosURL").value(hasItem(DEFAULT_LOGOS_URL)))
            .andExpect(jsonPath("$.[*].formURL").value(hasItem(DEFAULT_FORM_URL)))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].uniqueChallengers").value(hasItem(DEFAULT_UNIQUE_CHALLENGERS)));
    }

    @Test
    @Transactional
    void getSurvey() throws Exception {
        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);

        // Get the survey
        restSurveyMockMvc
            .perform(get(ENTITY_API_URL_ID, survey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.surveyName").value(DEFAULT_SURVEY_NAME))
            .andExpect(jsonPath("$.surveyDescription").value(DEFAULT_SURVEY_DESCRIPTION))
            .andExpect(jsonPath("$.contactsDescription").value(DEFAULT_CONTACTS_DESCRIPTION))
            .andExpect(jsonPath("$.friendlyURL").value(DEFAULT_FRIENDLY_URL))
            .andExpect(jsonPath("$.photoURL").value(DEFAULT_PHOTO_URL))
            .andExpect(jsonPath("$.logosURL").value(DEFAULT_LOGOS_URL))
            .andExpect(jsonPath("$.formURL").value(DEFAULT_FORM_URL))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.uniqueChallengers").value(DEFAULT_UNIQUE_CHALLENGERS))
            .andExpect(jsonPath("$.challengerPools.length()").value(1));
    }

    @Test
    @Transactional
    public void getSurveyByFriendlyUrl() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);
        Survey surveyWithSameFriendlyUrl = createEntity();

        // Get the survey
        restSurveyMockMvc
            .perform(get("/api/surveys/friendly-url/{friendlyURL}", survey.getFriendlyURL()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.surveyName").value(DEFAULT_SURVEY_NAME.toString()))
            .andExpect(jsonPath("$.surveyDescription").value(DEFAULT_SURVEY_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contactsDescription").value(DEFAULT_CONTACTS_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.friendlyURL").value(DEFAULT_FRIENDLY_URL.toString()))
            .andExpect(jsonPath("$.photoURL").value(DEFAULT_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.logosURL").value(DEFAULT_LOGOS_URL.toString()))
            .andExpect(jsonPath("$.formURL").value(DEFAULT_FORM_URL.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.booleanValue()))
            .andExpect(jsonPath("$.challengerPools.length()").value(1));
    }

    @Test
    @Transactional
    void getNonExistingSurvey() throws Exception {
        // Get the survey
        restSurveyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void putExistingSurvey() throws Exception {
        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the survey
        Survey updatedSurvey = surveyRepository.findById(survey.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSurvey are not directly saved in db
        em.detach(updatedSurvey);
        updatedSurvey
            .surveyName(UPDATED_SURVEY_NAME)
            .surveyDescription(UPDATED_SURVEY_DESCRIPTION)
            .contactsDescription(UPDATED_CONTACTS_DESCRIPTION)
            .friendlyURL(UPDATED_FRIENDLY_URL)
            .photoURL(UPDATED_PHOTO_URL)
            .logosURL(UPDATED_LOGOS_URL)
            .formURL(UPDATED_FORM_URL)
            .open(UPDATED_OPEN)
            .language(UPDATED_LANGUAGE)
            .uniqueChallengers(UPDATED_UNIQUE_CHALLENGERS);
        SurveyDTO surveyDTO = surveyMapper.toDto(updatedSurvey);

        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSurveyToMatchAllProperties(updatedSurvey);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void putNonExistingSurvey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        survey.setId(longCount.incrementAndGet());

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void putWithIdMismatchSurvey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        survey.setId(longCount.incrementAndGet());

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(surveyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSurvey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        survey.setId(longCount.incrementAndGet());

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void partialUpdateSurveyWithPatch() throws Exception {
        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the survey using partial update
        Survey partialUpdatedSurvey = new Survey();
        partialUpdatedSurvey.setId(survey.getId());

        partialUpdatedSurvey
            .surveyDescription(UPDATED_SURVEY_DESCRIPTION)
            .contactsDescription(UPDATED_CONTACTS_DESCRIPTION)
            .photoURL(UPDATED_PHOTO_URL)
            .open(UPDATED_OPEN)
            .language(UPDATED_LANGUAGE);

        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurvey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSurvey))
            )
            .andExpect(status().isOk());

        // Validate the Survey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSurveyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSurvey, survey), getPersistedSurvey(survey));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void fullUpdateSurveyWithPatch() throws Exception {
        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the survey using partial update
        Survey partialUpdatedSurvey = new Survey();
        partialUpdatedSurvey.setId(survey.getId());

        partialUpdatedSurvey
            .surveyName(UPDATED_SURVEY_NAME)
            .surveyDescription(UPDATED_SURVEY_DESCRIPTION)
            .contactsDescription(UPDATED_CONTACTS_DESCRIPTION)
            .friendlyURL(UPDATED_FRIENDLY_URL)
            .photoURL(UPDATED_PHOTO_URL)
            .logosURL(UPDATED_LOGOS_URL)
            .formURL(UPDATED_FORM_URL)
            .open(UPDATED_OPEN)
            .language(UPDATED_LANGUAGE)
            .uniqueChallengers(UPDATED_UNIQUE_CHALLENGERS);

        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSurvey.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSurvey))
            )
            .andExpect(status().isOk());

        // Validate the Survey in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSurveyUpdatableFieldsEquals(partialUpdatedSurvey, getPersistedSurvey(partialUpdatedSurvey));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void patchNonExistingSurvey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        survey.setId(longCount.incrementAndGet());

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, surveyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(surveyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void patchWithIdMismatchSurvey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        survey.setId(longCount.incrementAndGet());

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(surveyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void patchWithMissingIdPathParamSurvey() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        survey.setId(longCount.incrementAndGet());

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSurveyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Survey in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void deleteSurvey() throws Exception {
        Answer answer = AnswerResourceIT.createEntity();
        answer.setSurvey(survey);

        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);
        answerRepository.saveAndFlush(answer);

        long databaseSizeBeforeDelete = getRepositoryCount();
        long answersCount = answerRepository.count();

        // Delete the survey
        restSurveyMockMvc
            .perform(delete(ENTITY_API_URL_ID, survey.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        // Validate the answer has been deleted
        assertDecrementedAnswerRepositoryCount(answersCount);
    }

    protected long getRepositoryCount() {
        return surveyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedAnswerRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(answerRepository.count());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Survey getPersistedSurvey(Survey survey) {
        return surveyRepository.findById(survey.getId()).orElseThrow();
    }

    protected void assertPersistedSurveyToMatchAllProperties(Survey expectedSurvey) {
        assertSurveyAllPropertiesEquals(expectedSurvey, getPersistedSurvey(expectedSurvey));
    }

    protected void assertPersistedSurveyToMatchUpdatableProperties(Survey expectedSurvey) {
        assertSurveyAllUpdatablePropertiesEquals(expectedSurvey, getPersistedSurvey(expectedSurvey));
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void createSurveyWithMultiplePools() throws Exception {
        // Create a survey with 3 pools
        Survey surveyWithPools = new Survey()
            .surveyName("Multi Pool Survey")
            .surveyDescription("Survey with multiple pools")
            .contactsDescription("Contact info")
            .friendlyURL("multi-pool-survey")
            .photoURL("https://example.com/photo.jpg")
            .logosURL("https://example.com/logo.jpg")
            .formURL("https://example.com/form")
            .open(true)
            .language(Language.EN)
            .uniqueChallengers(true);

        ChallengerPool pool1 = new ChallengerPool()
            .poolOrder(1)
            .challengersURL("https://api.flickr.com/pool1")
            .numberOfMatches(10)
            .matchesDescription("First pool");
        surveyWithPools.addChallengerPool(pool1);

        ChallengerPool pool2 = new ChallengerPool()
            .poolOrder(2)
            .challengersURL("https://api.flickr.com/pool2")
            .numberOfMatches(15)
            .matchesDescription("Second pool");
        surveyWithPools.addChallengerPool(pool2);

        ChallengerPool pool3 = new ChallengerPool()
            .poolOrder(3)
            .challengersURL("https://api.flickr.com/pool3")
            .numberOfMatches(20)
            .matchesDescription("Third pool");
        surveyWithPools.addChallengerPool(pool3);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // Create the Survey with multiple pools
        SurveyDTO surveyDTO = surveyMapper.toDto(surveyWithPools);
        var returnedSurveyDTO = om.readValue(
            restSurveyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.challengerPools.length()").value(3))
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SurveyDTO.class
        );

        // Validate the Survey in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSurvey = surveyMapper.toEntity(returnedSurveyDTO);
        assertThat(returnedSurvey.getChallengerPools()).hasSize(3);

        insertedSurvey = returnedSurvey;
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void updateSurveyPools() throws Exception {
        // Initialize the database with a survey with one pool
        insertedSurvey = surveyRepository.saveAndFlush(survey);
        assertThat(getPersistedSurvey(survey).getChallengerPools()).hasSize(1);

        // Update the survey to add another pool
        Survey updatedSurvey = surveyRepository.findById(survey.getId()).orElseThrow();
        em.detach(updatedSurvey);

        ChallengerPool newPool = new ChallengerPool()
            .poolOrder(2)
            .challengersURL("https://api.flickr.com/pool2")
            .numberOfMatches(15)
            .matchesDescription("New pool");
        updatedSurvey.addChallengerPool(newPool);

        SurveyDTO surveyDTO = surveyMapper.toDto(updatedSurvey);

        restSurveyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, surveyDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.challengerPools.length()").value(2));

        // Validate the Survey has 2 pools now
        Survey persistedSurvey = getPersistedSurvey(updatedSurvey);
        assertThat(persistedSurvey.getChallengerPools()).hasSize(2);
    }

    @Test
    @Transactional
    @WithMockUser(authorities = AuthoritiesConstants.ADMIN)
    void deleteSurveyCascadesDeletePools() throws Exception {
        // Create a survey with multiple pools
        ChallengerPool pool2 = new ChallengerPool()
            .poolOrder(2)
            .challengersURL("https://api.flickr.com/pool2")
            .numberOfMatches(15)
            .matchesDescription("Second pool");
        survey.addChallengerPool(pool2);

        // Initialize the database
        insertedSurvey = surveyRepository.saveAndFlush(survey);
        assertThat(getPersistedSurvey(survey).getChallengerPools()).hasSize(2);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the survey
        restSurveyMockMvc
            .perform(delete(ENTITY_API_URL_ID, survey.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the survey and its pools have been deleted
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);

        // Verify the survey no longer exists
        assertThat(surveyRepository.findById(survey.getId())).isEmpty();
    }
}
