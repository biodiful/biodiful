package org.biodiful.web.rest;

import org.biodiful.BiodifulApp;

import org.biodiful.domain.Answer;
import org.biodiful.repository.AnswerRepository;
import org.biodiful.service.AnswerService;
import org.biodiful.service.dto.AnswerDTO;
import org.biodiful.service.mapper.AnswerMapper;
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

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


import static org.biodiful.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnswerResource REST controller.
 *
 * @see AnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiodifulApp.class)
public class AnswerResourceIntTest {

    private static final String DEFAULT_JUDGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_JUDGE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CHALLENGER_1 = "AAAAAAAAAA";
    private static final String UPDATED_CHALLENGER_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CHALLENGER_2 = "AAAAAAAAAA";
    private static final String UPDATED_CHALLENGER_2 = "BBBBBBBBBB";

    private static final String DEFAULT_WINNER = "AAAAAAAAAA";
    private static final String UPDATED_WINNER = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_POOL_NUMBER = 1;
    private static final Integer UPDATED_POOL_NUMBER = 2;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerMapper answerMapper;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnswerMockMvc;

    private Answer answer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnswerResource answerResource = new AnswerResource(answerService);
        this.restAnswerMockMvc = MockMvcBuilders.standaloneSetup(answerResource)
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
    public static Answer createEntity(EntityManager em) {
        Answer answer = new Answer()
            .judgeID(DEFAULT_JUDGE_ID)
            .challenger1(DEFAULT_CHALLENGER_1)
            .challenger2(DEFAULT_CHALLENGER_2)
            .winner(DEFAULT_WINNER)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .poolNumber(DEFAULT_POOL_NUMBER);
        return answer;
    }

    public static Answer createSecondEntity(EntityManager em) {
        Answer answer = new Answer()
            .judgeID(UPDATED_JUDGE_ID)
            .challenger1(UPDATED_CHALLENGER_1)
            .challenger2(UPDATED_CHALLENGER_2)
            .winner(UPDATED_WINNER)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .poolNumber(UPDATED_POOL_NUMBER);
        return answer;
    }

    @Before
    public void initTest() {
        answer = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);
        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getJudgeID()).isEqualTo(DEFAULT_JUDGE_ID);
        assertThat(testAnswer.getChallenger1()).isEqualTo(DEFAULT_CHALLENGER_1);
        assertThat(testAnswer.getChallenger2()).isEqualTo(DEFAULT_CHALLENGER_2);
        assertThat(testAnswer.getWinner()).isEqualTo(DEFAULT_WINNER);
        assertThat(testAnswer.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAnswer.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAnswer.getPoolNumber()).isEqualTo(DEFAULT_POOL_NUMBER);
    }

    @Test
    @Transactional
    public void createAllAnswers() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        List<AnswerDTO> answersDTO = new ArrayList<AnswerDTO>();

        // Create the Answer
        answersDTO.add(answerMapper.toDto(answer));
        answersDTO.add(answerMapper.toDto(createSecondEntity(em)));
        restAnswerMockMvc.perform(post("/api/answers/all")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answersDTO)))
            .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate + 2);
        Answer testAnswer = answerList.get(answerList.size() - 2);
        assertThat(testAnswer.getJudgeID()).isEqualTo(DEFAULT_JUDGE_ID);
        assertThat(testAnswer.getChallenger1()).isEqualTo(DEFAULT_CHALLENGER_1);
        assertThat(testAnswer.getChallenger2()).isEqualTo(DEFAULT_CHALLENGER_2);
        assertThat(testAnswer.getWinner()).isEqualTo(DEFAULT_WINNER);
        assertThat(testAnswer.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testAnswer.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testAnswer.getPoolNumber()).isEqualTo(DEFAULT_POOL_NUMBER);

        Answer testAnswer2 = answerList.get(answerList.size() - 1);
        assertThat(testAnswer2.getJudgeID()).isEqualTo(UPDATED_JUDGE_ID);
        assertThat(testAnswer2.getChallenger1()).isEqualTo(UPDATED_CHALLENGER_1);
        assertThat(testAnswer2.getChallenger2()).isEqualTo(UPDATED_CHALLENGER_2);
        assertThat(testAnswer2.getWinner()).isEqualTo(UPDATED_WINNER);
        assertThat(testAnswer2.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAnswer2.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAnswer2.getPoolNumber()).isEqualTo(UPDATED_POOL_NUMBER);
    }

    @Test
    @Transactional
    public void createAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer with an existing ID
        answer.setId(1L);
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkJudgeIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setJudgeID(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChallenger1IsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setChallenger1(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkChallenger2IsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setChallenger2(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWinnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setWinner(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setStartTime(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setEndTime(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPoolNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setPoolNumber(null);

        // Create the Answer, which fails.
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        restAnswerMockMvc.perform(post("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answerList
        restAnswerMockMvc.perform(get("/api/answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
            .andExpect(jsonPath("$.[*].judgeID").value(hasItem(DEFAULT_JUDGE_ID.toString())))
            .andExpect(jsonPath("$.[*].challenger1").value(hasItem(DEFAULT_CHALLENGER_1.toString())))
            .andExpect(jsonPath("$.[*].challenger2").value(hasItem(DEFAULT_CHALLENGER_2.toString())))
            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].poolNumber").value(hasItem(DEFAULT_POOL_NUMBER)));
    }

    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.judgeID").value(DEFAULT_JUDGE_ID.toString()))
            .andExpect(jsonPath("$.challenger1").value(DEFAULT_CHALLENGER_1.toString()))
            .andExpect(jsonPath("$.challenger2").value(DEFAULT_CHALLENGER_2.toString()))
            .andExpect(jsonPath("$.winner").value(DEFAULT_WINNER.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.poolNumber").value(DEFAULT_POOL_NUMBER));
    }

    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        Answer updatedAnswer = answerRepository.findById(answer.getId()).get();
        // Disconnect from session so that the updates on updatedAnswer are not directly saved in db
        em.detach(updatedAnswer);
        updatedAnswer
            .judgeID(UPDATED_JUDGE_ID)
            .challenger1(UPDATED_CHALLENGER_1)
            .challenger2(UPDATED_CHALLENGER_2)
            .winner(UPDATED_WINNER)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .poolNumber(UPDATED_POOL_NUMBER);
        AnswerDTO answerDTO = answerMapper.toDto(updatedAnswer);

        restAnswerMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answerList.get(answerList.size() - 1);
        assertThat(testAnswer.getJudgeID()).isEqualTo(UPDATED_JUDGE_ID);
        assertThat(testAnswer.getChallenger1()).isEqualTo(UPDATED_CHALLENGER_1);
        assertThat(testAnswer.getChallenger2()).isEqualTo(UPDATED_CHALLENGER_2);
        assertThat(testAnswer.getWinner()).isEqualTo(UPDATED_WINNER);
        assertThat(testAnswer.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testAnswer.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testAnswer.getPoolNumber()).isEqualTo(UPDATED_POOL_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingAnswer() throws Exception {
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Create the Answer
        AnswerDTO answerDTO = answerMapper.toDto(answer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnswerMockMvc.perform(put("/api/answers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(answerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Answer in the database
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Get the answer
        restAnswerMockMvc.perform(delete("/api/answers/{id}", answer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Answer.class);
        Answer answer1 = new Answer();
        answer1.setId(1L);
        Answer answer2 = new Answer();
        answer2.setId(answer1.getId());
        assertThat(answer1).isEqualTo(answer2);
        answer2.setId(2L);
        assertThat(answer1).isNotEqualTo(answer2);
        answer1.setId(null);
        assertThat(answer1).isNotEqualTo(answer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnswerDTO.class);
        AnswerDTO answerDTO1 = new AnswerDTO();
        answerDTO1.setId(1L);
        AnswerDTO answerDTO2 = new AnswerDTO();
        assertThat(answerDTO1).isNotEqualTo(answerDTO2);
        answerDTO2.setId(answerDTO1.getId());
        assertThat(answerDTO1).isEqualTo(answerDTO2);
        answerDTO2.setId(2L);
        assertThat(answerDTO1).isNotEqualTo(answerDTO2);
        answerDTO1.setId(null);
        assertThat(answerDTO1).isNotEqualTo(answerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(answerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(answerMapper.fromId(null)).isNull();
    }
}
