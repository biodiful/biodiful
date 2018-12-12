package org.biodiful.web.rest;

import org.biodiful.BiodifulApp;

import org.biodiful.domain.Answer;
import org.biodiful.domain.Survey;
import org.biodiful.repository.AnswerRepository;
import org.biodiful.repository.SurveyRepository;
import org.biodiful.service.SurveyService;
import org.biodiful.service.dto.SurveyDTO;
import org.biodiful.service.mapper.SurveyMapper;
import org.biodiful.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static org.biodiful.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.biodiful.domain.enumeration.Language;
/**
 * Test class for the SurveyResource REST controller.
 *
 * @see SurveyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiodifulApp.class)
public class SurveyResourceIntTest {

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

    private static final String DEFAULT_MATCHES_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_MATCHES_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_OPEN = false;
    private static final Boolean UPDATED_OPEN = true;

    private static final Language DEFAULT_LANGUAGE = Language.FR;
    private static final Language UPDATED_LANGUAGE = Language.EN;

    private static final Boolean DEFAULT_UNIQUE_CHALLENGERS = false;
    private static final Boolean UPDATED_UNIQUE_CHALLENGERS = true;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SurveyMapper surveyMapper;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSurveyMockMvc;

    private Survey survey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SurveyResource surveyResource = new SurveyResource(surveyService);
        this.restSurveyMockMvc = MockMvcBuilders.standaloneSetup(surveyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Survey createEntity(EntityManager em) {
        Survey survey = new Survey()
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
            .matchesDescription(DEFAULT_MATCHES_DESCRIPTION)
            .open(DEFAULT_OPEN)
            .language(DEFAULT_LANGUAGE)
            .uniqueChallengers(DEFAULT_UNIQUE_CHALLENGERS);
        return survey;
    }

    @Before
    public void initTest() {
        survey = createEntity(em);
    }

    @Test
    @Transactional
    public void createSurvey() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);
        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isCreated());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeCreate + 1);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getSurveyName()).isEqualTo(DEFAULT_SURVEY_NAME);
        assertThat(testSurvey.getSurveyDescription()).isEqualTo(DEFAULT_SURVEY_DESCRIPTION);
        assertThat(testSurvey.getContactsDescription()).isEqualTo(DEFAULT_CONTACTS_DESCRIPTION);
        assertThat(testSurvey.getFriendlyURL()).isEqualTo(DEFAULT_FRIENDLY_URL);
        assertThat(testSurvey.getPhotoURL()).isEqualTo(DEFAULT_PHOTO_URL);
        assertThat(testSurvey.getLogosURL()).isEqualTo(DEFAULT_LOGOS_URL);
        assertThat(testSurvey.getFormURL()).isEqualTo(DEFAULT_FORM_URL);
        assertThat(testSurvey.getChallengersPool1URL()).isEqualTo(DEFAULT_CHALLENGERS_POOL_1_URL);
        assertThat(testSurvey.getChallengersPool2URL()).isEqualTo(DEFAULT_CHALLENGERS_POOL_2_URL);
        assertThat(testSurvey.getChallengersPool3URL()).isEqualTo(DEFAULT_CHALLENGERS_POOL_3_URL);
        assertThat(testSurvey.getNumberOfMatchesPerPool()).isEqualTo(DEFAULT_NUMBER_OF_MATCHES_PER_POOL);
        assertThat(testSurvey.getMatchesDescription()).isEqualTo(DEFAULT_MATCHES_DESCRIPTION);
        assertThat(testSurvey.isOpen()).isEqualTo(DEFAULT_OPEN);
        assertThat(testSurvey.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testSurvey.isUniqueChallengers()).isEqualTo(DEFAULT_UNIQUE_CHALLENGERS);
    }

    @Test
    @Transactional
    public void createSurveyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = surveyRepository.findAll().size();

        // Create the Survey with an existing ID
        survey.setId(1L);
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getSurveyResponsesCount() throws Exception {
        Answer entity = AnswerResourceIntTest.createEntity(em);
        entity.setSurvey(survey);

        surveyRepository.saveAndFlush(survey);
        answerRepository.saveAndFlush(entity);

        restSurveyMockMvc.perform(get("/api/surveys/{id}/judges-count", survey.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk()).andExpect(content().string("1"));
    }

    @Test
    @Transactional
    public void checkSurveyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setSurveyName(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFormURLIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setFormURL(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChallengersPool1URLIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setChallengersPool1URL(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberOfMatchesPerPoolIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setNumberOfMatchesPerPool(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOpenIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setOpen(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setLanguage(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUniqueChallengersIsRequired() throws Exception {
        int databaseSizeBeforeTest = surveyRepository.findAll().size();
        // set the field null
        survey.setUniqueChallengers(null);

        // Create the Survey, which fails.
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        restSurveyMockMvc.perform(post("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSurveys() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get all the surveyList
        restSurveyMockMvc.perform(get("/api/surveys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(survey.getId().intValue())))
            .andExpect(jsonPath("$.[*].surveyName").value(hasItem(DEFAULT_SURVEY_NAME.toString())))
            .andExpect(jsonPath("$.[*].surveyDescription").value(hasItem(DEFAULT_SURVEY_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contactsDescription").value(hasItem(DEFAULT_CONTACTS_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].friendlyURL").value(hasItem(DEFAULT_FRIENDLY_URL.toString())))
            .andExpect(jsonPath("$.[*].photoURL").value(hasItem(DEFAULT_PHOTO_URL.toString())))
            .andExpect(jsonPath("$.[*].logosURL").value(hasItem(DEFAULT_LOGOS_URL.toString())))
            .andExpect(jsonPath("$.[*].formURL").value(hasItem(DEFAULT_FORM_URL.toString())))
            .andExpect(jsonPath("$.[*].challengersPool1URL").value(hasItem(DEFAULT_CHALLENGERS_POOL_1_URL.toString())))
            .andExpect(jsonPath("$.[*].challengersPool2URL").value(hasItem(DEFAULT_CHALLENGERS_POOL_2_URL.toString())))
            .andExpect(jsonPath("$.[*].challengersPool3URL").value(hasItem(DEFAULT_CHALLENGERS_POOL_3_URL.toString())))
            .andExpect(jsonPath("$.[*].numberOfMatchesPerPool").value(hasItem(DEFAULT_NUMBER_OF_MATCHES_PER_POOL)))
            .andExpect(jsonPath("$.[*].matchesDescription").value(hasItem(DEFAULT_MATCHES_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].open").value(hasItem(DEFAULT_OPEN.booleanValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].uniqueChallengers").value(hasItem(DEFAULT_UNIQUE_CHALLENGERS.booleanValue())));
    }

    @Test
    @Transactional
    public void getSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/{id}", survey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.surveyName").value(DEFAULT_SURVEY_NAME.toString()))
            .andExpect(jsonPath("$.surveyDescription").value(DEFAULT_SURVEY_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contactsDescription").value(DEFAULT_CONTACTS_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.friendlyURL").value(DEFAULT_FRIENDLY_URL.toString()))
            .andExpect(jsonPath("$.photoURL").value(DEFAULT_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.logosURL").value(DEFAULT_LOGOS_URL.toString()))
            .andExpect(jsonPath("$.formURL").value(DEFAULT_FORM_URL.toString()))
            .andExpect(jsonPath("$.challengersPool1URL").value(DEFAULT_CHALLENGERS_POOL_1_URL.toString()))
            .andExpect(jsonPath("$.challengersPool2URL").value(DEFAULT_CHALLENGERS_POOL_2_URL.toString()))
            .andExpect(jsonPath("$.challengersPool3URL").value(DEFAULT_CHALLENGERS_POOL_3_URL.toString()))
            .andExpect(jsonPath("$.numberOfMatchesPerPool").value(DEFAULT_NUMBER_OF_MATCHES_PER_POOL))
            .andExpect(jsonPath("$.matchesDescription").value(DEFAULT_MATCHES_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.uniqueChallengers").value(DEFAULT_UNIQUE_CHALLENGERS.booleanValue()));
    }

    @Test
    @Transactional
    public void getSurveyByFriendlyUrl() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);
        Survey surveyWithSameFriendlyUrl = createEntity(em);

        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/friendly-url/{friendlyURL}", survey.getFriendlyURL()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(survey.getId().intValue()))
            .andExpect(jsonPath("$.surveyName").value(DEFAULT_SURVEY_NAME.toString()))
            .andExpect(jsonPath("$.surveyDescription").value(DEFAULT_SURVEY_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contactsDescription").value(DEFAULT_CONTACTS_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.friendlyURL").value(DEFAULT_FRIENDLY_URL.toString()))
            .andExpect(jsonPath("$.photoURL").value(DEFAULT_PHOTO_URL.toString()))
            .andExpect(jsonPath("$.logosURL").value(DEFAULT_LOGOS_URL.toString()))
            .andExpect(jsonPath("$.formURL").value(DEFAULT_FORM_URL.toString()))
            .andExpect(jsonPath("$.challengersPool1URL").value(DEFAULT_CHALLENGERS_POOL_1_URL.toString()))
            .andExpect(jsonPath("$.challengersPool2URL").value(DEFAULT_CHALLENGERS_POOL_2_URL.toString()))
            .andExpect(jsonPath("$.challengersPool3URL").value(DEFAULT_CHALLENGERS_POOL_3_URL.toString()))
            .andExpect(jsonPath("$.numberOfMatchesPerPool").value(DEFAULT_NUMBER_OF_MATCHES_PER_POOL))
            .andExpect(jsonPath("$.matchesDescription").value(DEFAULT_MATCHES_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.open").value(DEFAULT_OPEN.booleanValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.uniqueChallengers").value(DEFAULT_UNIQUE_CHALLENGERS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSurvey() throws Exception {
        // Get the survey
        restSurveyMockMvc.perform(get("/api/surveys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Update the survey
        Survey updatedSurvey = surveyRepository.findById(survey.getId()).get();
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
            .matchesDescription(UPDATED_MATCHES_DESCRIPTION)
            .open(UPDATED_OPEN)
            .language(UPDATED_LANGUAGE)
            .uniqueChallengers(UPDATED_UNIQUE_CHALLENGERS);
        SurveyDTO surveyDTO = surveyMapper.toDto(updatedSurvey);

        restSurveyMockMvc.perform(put("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isOk());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
        Survey testSurvey = surveyList.get(surveyList.size() - 1);
        assertThat(testSurvey.getSurveyName()).isEqualTo(UPDATED_SURVEY_NAME);
        assertThat(testSurvey.getSurveyDescription()).isEqualTo(UPDATED_SURVEY_DESCRIPTION);
        assertThat(testSurvey.getContactsDescription()).isEqualTo(UPDATED_CONTACTS_DESCRIPTION);
        assertThat(testSurvey.getFriendlyURL()).isEqualTo(UPDATED_FRIENDLY_URL);
        assertThat(testSurvey.getPhotoURL()).isEqualTo(UPDATED_PHOTO_URL);
        assertThat(testSurvey.getLogosURL()).isEqualTo(UPDATED_LOGOS_URL);
        assertThat(testSurvey.getFormURL()).isEqualTo(UPDATED_FORM_URL);
        assertThat(testSurvey.getChallengersPool1URL()).isEqualTo(UPDATED_CHALLENGERS_POOL_1_URL);
        assertThat(testSurvey.getChallengersPool2URL()).isEqualTo(UPDATED_CHALLENGERS_POOL_2_URL);
        assertThat(testSurvey.getChallengersPool3URL()).isEqualTo(UPDATED_CHALLENGERS_POOL_3_URL);
        assertThat(testSurvey.getNumberOfMatchesPerPool()).isEqualTo(UPDATED_NUMBER_OF_MATCHES_PER_POOL);
        assertThat(testSurvey.getMatchesDescription()).isEqualTo(UPDATED_MATCHES_DESCRIPTION);
        assertThat(testSurvey.isOpen()).isEqualTo(UPDATED_OPEN);
        assertThat(testSurvey.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testSurvey.isUniqueChallengers()).isEqualTo(UPDATED_UNIQUE_CHALLENGERS);
    }

    @Test
    @Transactional
    public void updateNonExistingSurvey() throws Exception {
        int databaseSizeBeforeUpdate = surveyRepository.findAll().size();

        // Create the Survey
        SurveyDTO surveyDTO = surveyMapper.toDto(survey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSurveyMockMvc.perform(put("/api/surveys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(surveyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Survey in the database
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSurvey() throws Exception {
        // Initialize the database
        surveyRepository.saveAndFlush(survey);

        int databaseSizeBeforeDelete = surveyRepository.findAll().size();

        // Get the survey
        restSurveyMockMvc.perform(delete("/api/surveys/{id}", survey.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Survey> surveyList = surveyRepository.findAll();
        assertThat(surveyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Survey.class);
        Survey survey1 = new Survey();
        survey1.setId(1L);
        Survey survey2 = new Survey();
        survey2.setId(survey1.getId());
        assertThat(survey1).isEqualTo(survey2);
        survey2.setId(2L);
        assertThat(survey1).isNotEqualTo(survey2);
        survey1.setId(null);
        assertThat(survey1).isNotEqualTo(survey2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyDTO.class);
        SurveyDTO surveyDTO1 = new SurveyDTO();
        surveyDTO1.setId(1L);
        SurveyDTO surveyDTO2 = new SurveyDTO();
        assertThat(surveyDTO1).isNotEqualTo(surveyDTO2);
        surveyDTO2.setId(surveyDTO1.getId());
        assertThat(surveyDTO1).isEqualTo(surveyDTO2);
        surveyDTO2.setId(2L);
        assertThat(surveyDTO1).isNotEqualTo(surveyDTO2);
        surveyDTO1.setId(null);
        assertThat(surveyDTO1).isNotEqualTo(surveyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(surveyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(surveyMapper.fromId(null)).isNull();
    }
}
