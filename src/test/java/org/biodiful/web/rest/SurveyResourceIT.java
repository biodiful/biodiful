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

    private static final String DEFAULT_CHALLENGERS_POOL_1_URL = "AAAAAAAAAA";
    private static final String UPDATED_CHALLENGERS_POOL_1_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CHALLENGERS_POOL_2_URL = "AAAAAAAAAA";
    private static final String UPDATED_CHALLENGERS_POOL_2_URL = "BBBBBBBBBB";

    private static final String DEFAULT_CHALLENGERS_POOL_3_URL = "AAAAAAAAAA";
    private static final String UPDATED_CHALLENGERS_POOL_3_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_MATCHES_PER_POOL = 1;
    private static final Integer UPDATED_NUMBER_OF_MATCHES_PER_POOL = 2;

    private static final Integer DEFAULT_NUMBER_OF_MATCHES_PER_POOL_2 = 1;
    private static final Integer UPDATED_NUMBER_OF_MATCHES_PER_POOL_2 = 2;

    private static final Integer DEFAULT_NUMBER_OF_MATCHES_PER_POOL_3 = 1;
    private static final Integer UPDATED_NUMBER_OF_MATCHES_PER_POOL_3 = 2;

    private static final String DEFAULT_MATCHES_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MATCHES_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MATCHES_DESCRIPTION_POOL_2 = "AAAAAAAAAA";
    private static final String UPDATED_MATCHES_DESCRIPTION_POOL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_MATCHES_DESCRIPTION_POOL_3 = "AAAAAAAAAA";
    private static final String UPDATED_MATCHES_DESCRIPTION_POOL_3 = "BBBBBBBBBB";

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
        return new Survey()
            .surveyName(DEFAULT_SURVEY_NAME)
            .surveyDescription(DEFAULT_SURVEY_DESCRIPTION)
            .contactsDescription(DEFAULT_CONTACTS_DESCRIPTION)
            .friendlyURL(DEFAULT_FRIENDLY_URL)
            .photoURL(DEFAULT_PHOTO_URL)
            .logosURL(DEFAULT_LOGOS_URL)
            .formURL(DEFAULT_FORM_URL)
            .challengersPool1URL(DEFAULT_CHALLENGERS_POOL_1_URL)
            .challengersPool2URL(DEFAULT_CHALLENGERS_POOL_2_URL)
            .challengersPool3URL(DEFAULT_CHALLENGERS_POOL_3_URL)
            .numberOfMatchesPerPool(DEFAULT_NUMBER_OF_MATCHES_PER_POOL)
            .numberOfMatchesPerPool2(DEFAULT_NUMBER_OF_MATCHES_PER_POOL_2)
            .numberOfMatchesPerPool3(DEFAULT_NUMBER_OF_MATCHES_PER_POOL_3)
            .matchesDescription(DEFAULT_MATCHES_DESCRIPTION)
            .matchesDescriptionPool2(DEFAULT_MATCHES_DESCRIPTION_POOL_2)
            .matchesDescriptionPool3(DEFAULT_MATCHES_DESCRIPTION_POOL_3)
            .open(DEFAULT_OPEN)
            .language(DEFAULT_LANGUAGE)
            .uniqueChallengers(DEFAULT_UNIQUE_CHALLENGERS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Survey createUpdatedEntity() {
        return new Survey()
            .surveyName(UPDATED_SURVEY_NAME)
            .surveyDescription(UPDATED_SURVEY_DESCRIPTION)
            .contactsDescription(UPDATED_CONTACTS_DESCRIPTION)
            .friendlyURL(UPDATED_FRIENDLY_URL)
            .photoURL(UPDATED_PHOTO_URL)
            .logosURL(UPDATED_LOGOS_URL)
            .formURL(UPDATED_FORM_URL)
            .challengersPool1URL(UPDATED_CHALLENGERS_POOL_1_URL)
            .challengersPool2URL(UPDATED_CHALLENGERS_POOL_2_URL)
            .challengersPool3URL(UPDATED_CHALLENGERS_POOL_3_URL)
            .numberOfMatchesPerPool(UPDATED_NUMBER_OF_MATCHES_PER_POOL)
            .numberOfMatchesPerPool2(UPDATED_NUMBER_OF_MATCHES_PER_POOL_2)
            .numberOfMatchesPerPool3(UPDATED_NUMBER_OF_MATCHES_PER_POOL_3)
            .matchesDescription(UPDATED_MATCHES_DESCRIPTION)
            .matchesDescriptionPool2(UPDATED_MATCHES_DESCRIPTION_POOL_2)
            .matchesDescriptionPool3(UPDATED_MATCHES_DESCRIPTION_POOL_3)
            .open(UPDATED_OPEN)
            .language(UPDATED_LANGUAGE)
            .uniqueChallengers(UPDATED_UNIQUE_CHALLENGERS);
    }

    @BeforeEach
    public void initTest() {
        survey = createEntity();
    }

    @AfterEach
    public void cleanup() {
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
    void checkChallengersPool1URLIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setChallengersPool1URL(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfMatchesPerPoolIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        survey.setNumberOfMatchesPerPool(null);

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
            .andExpect(jsonPath("$.[*].challengersPool1URL").value(hasItem(DEFAULT_CHALLENGERS_POOL_1_URL)))
            .andExpect(jsonPath("$.[*].challengersPool2URL").value(hasItem(DEFAULT_CHALLENGERS_POOL_2_URL)))
            .andExpect(jsonPath("$.[*].challengersPool3URL").value(hasItem(DEFAULT_CHALLENGERS_POOL_3_URL)))
            .andExpect(jsonPath("$.[*].numberOfMatchesPerPool").value(hasItem(DEFAULT_NUMBER_OF_MATCHES_PER_POOL)))
            .andExpect(jsonPath("$.[*].numberOfMatchesPerPool2").value(hasItem(DEFAULT_NUMBER_OF_MATCHES_PER_POOL_2)))
            .andExpect(jsonPath("$.[*].numberOfMatchesPerPool3").value(hasItem(DEFAULT_NUMBER_OF_MATCHES_PER_POOL_3)))
            .andExpect(jsonPath("$.[*].matchesDescription").value(hasItem(DEFAULT_MATCHES_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].matchesDescriptionPool2").value(hasItem(DEFAULT_MATCHES_DESCRIPTION_POOL_2)))
            .andExpect(jsonPath("$.[*].matchesDescriptionPool3").value(hasItem(DEFAULT_MATCHES_DESCRIPTION_POOL_3)))
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
            .andExpect(jsonPath("$.challengersPool1URL").value(DEFAULT_CHALLENGERS_POOL_1_URL))
            .andExpect(jsonPath("$.challengersPool2URL").value(DEFAULT_CHALLENGERS_POOL_2_URL))
            .andExpect(jsonPath("$.challengersPool3URL").value(DEFAULT_CHALLENGERS_POOL_3_URL))
            .andExpect(jsonPath("$.numberOfMatchesPerPool").value(DEFAULT_NUMBER_OF_MATCHES_PER_POOL))
            .andExpect(jsonPath("$.numberOfMatchesPerPool2").value(DEFAULT_NUMBER_OF_MATCHES_PER_POOL_2))
            .andExpect(jsonPath("$.numberOfMatchesPerPool3").value(DEFAULT_NUMBER_OF_MATCHES_PER_POOL_3))
            .andExpect(jsonPath("$.matchesDescription").value(DEFAULT_MATCHES_DESCRIPTION))
            .andExpect(jsonPath("$.matchesDescriptionPool2").value(DEFAULT_MATCHES_DESCRIPTION_POOL_2))
            .andExpect(jsonPath("$.matchesDescriptionPool3").value(DEFAULT_MATCHES_DESCRIPTION_POOL_3))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.uniqueChallengers").value(DEFAULT_UNIQUE_CHALLENGERS));
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
            .andExpect(jsonPath("$.challengersPool1URL").value(DEFAULT_CHALLENGERS_POOL_1_URL.toString()))
            .andExpect(jsonPath("$.numberOfMatchesPerPool").value(DEFAULT_NUMBER_OF_MATCHES_PER_POOL))
            .andExpect(jsonPath("$.matchesDescription").value(DEFAULT_MATCHES_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.booleanValue()));
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
            .challengersPool1URL(UPDATED_CHALLENGERS_POOL_1_URL)
            .challengersPool2URL(UPDATED_CHALLENGERS_POOL_2_URL)
            .challengersPool3URL(UPDATED_CHALLENGERS_POOL_3_URL)
            .numberOfMatchesPerPool(UPDATED_NUMBER_OF_MATCHES_PER_POOL)
            .numberOfMatchesPerPool2(UPDATED_NUMBER_OF_MATCHES_PER_POOL_2)
            .numberOfMatchesPerPool3(UPDATED_NUMBER_OF_MATCHES_PER_POOL_3)
            .matchesDescription(UPDATED_MATCHES_DESCRIPTION)
            .matchesDescriptionPool2(UPDATED_MATCHES_DESCRIPTION_POOL_2)
            .matchesDescriptionPool3(UPDATED_MATCHES_DESCRIPTION_POOL_3)
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
            .challengersPool2URL(UPDATED_CHALLENGERS_POOL_2_URL)
            .challengersPool3URL(UPDATED_CHALLENGERS_POOL_3_URL)
            .numberOfMatchesPerPool2(UPDATED_NUMBER_OF_MATCHES_PER_POOL_2)
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
            .challengersPool1URL(UPDATED_CHALLENGERS_POOL_1_URL)
            .challengersPool2URL(UPDATED_CHALLENGERS_POOL_2_URL)
            .challengersPool3URL(UPDATED_CHALLENGERS_POOL_3_URL)
            .numberOfMatchesPerPool(UPDATED_NUMBER_OF_MATCHES_PER_POOL)
            .numberOfMatchesPerPool2(UPDATED_NUMBER_OF_MATCHES_PER_POOL_2)
            .numberOfMatchesPerPool3(UPDATED_NUMBER_OF_MATCHES_PER_POOL_3)
            .matchesDescription(UPDATED_MATCHES_DESCRIPTION)
            .matchesDescriptionPool2(UPDATED_MATCHES_DESCRIPTION_POOL_2)
            .matchesDescriptionPool3(UPDATED_MATCHES_DESCRIPTION_POOL_3)
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
}
