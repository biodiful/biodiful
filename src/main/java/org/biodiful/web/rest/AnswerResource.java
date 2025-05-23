package org.biodiful.web.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.biodiful.repository.AnswerRepository;
import org.biodiful.security.AuthoritiesConstants;
import org.biodiful.service.AnswerService;
import org.biodiful.service.dto.AnswerDTO;
import org.biodiful.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link org.biodiful.domain.Answer}.
 */
@RestController
@RequestMapping("/api/answers")
public class AnswerResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnswerResource.class);

    private static final String ENTITY_NAME = "answer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnswerService answerService;

    private final AnswerRepository answerRepository;

    public AnswerResource(AnswerService answerService, AnswerRepository answerRepository) {
        this.answerService = answerService;
        this.answerRepository = answerRepository;
    }

    /**
     * {@code POST  /answers} : Create a new answer.
     *
     * @param answerDTO the answerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new answerDTO, or with status {@code 400 (Bad Request)} if the answer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PermitAll
    public ResponseEntity<AnswerDTO> createAnswer(@Valid @RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        LOG.debug("REST request to save Answer : {}", answerDTO);
        if (answerDTO.getId() != null) {
            throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        answerDTO = answerService.save(answerDTO);
        return ResponseEntity.created(new URI("/api/answers/" + answerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, answerDTO.getId().toString()))
            .body(answerDTO);
    }

    /**
     * {@code PUT  /answers/:id} : Updates an existing answer.
     *
     * @param id the id of the answerDTO to save.
     * @param answerDTO the answerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerDTO,
     * or with status {@code 400 (Bad Request)} if the answerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the answerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AnswerDTO> updateAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnswerDTO answerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Answer : {}, {}", id, answerDTO);
        if (answerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        answerDTO = answerService.update(answerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerDTO.getId().toString()))
            .body(answerDTO);
    }

    /**
     * POST  /answers : Create a list of answers.
     *
     * @param answersDTO the list of answerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new answerDTO, or with status 400 (Bad Request) if the answers have already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/all")
    @PermitAll
    public ResponseEntity<List<AnswerDTO>> createAnswers(@Valid @RequestBody List<AnswerDTO> answersDTO) throws URISyntaxException {
        LOG.debug("REST request to save all Answers : {}", answersDTO);
        for (AnswerDTO answer : answersDTO) {
            if (answer.getId() != null) {
                throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
            }
        }
        List<AnswerDTO> results = answerService.saveAll(answersDTO);
        return ResponseEntity.created(new URI("/api/answers/all" + results.get(0).getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, results.get(0).getId().toString()))
            .body(results);
    }

    /**
     * {@code PATCH  /answers/:id} : Partial updates given fields of an existing answer, field will ignore if it is null
     *
     * @param id the id of the answerDTO to save.
     * @param answerDTO the answerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated answerDTO,
     * or with status {@code 400 (Bad Request)} if the answerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the answerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the answerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AnswerDTO> partialUpdateAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnswerDTO answerDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Answer partially : {}, {}", id, answerDTO);
        if (answerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, answerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!answerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnswerDTO> result = answerService.partialUpdate(answerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, answerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /answers} : get all the answers.
     *
     * @param pageable the pagination information.
     * @param surveyId the ID of the survey to restrict the list of answers (optional)
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of answers in body.
     */
    @GetMapping("")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AnswerDTO>> getAllAnswers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "surveyId", required = false) Optional<Long> surveyId
    ) {
        LOG.debug("REST request to get a page of Answers");
        List<AnswerDTO> answers;
        HttpHeaders headers;
        if (surveyId.isPresent()) {
            answers = answerService.findBySurveyId(surveyId.get());
            headers = new HttpHeaders();
            headers.add("X-Total-Count", Integer.toString(answers.size()));
        } else {
            Page<AnswerDTO> page = answerService.findAll(pageable);
            headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            answers = page.getContent();
        }
        return ResponseEntity.ok().headers(headers).body(answers);
    }

    /**
     * {@code GET  /answers/:id} : get the "id" answer.
     *
     * @param id the id of the answerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the answerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AnswerDTO> getAnswer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Answer : {}", id);
        Optional<AnswerDTO> answerDTO = answerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerDTO);
    }

    /**
     * {@code DELETE  /answers/:id} : delete the "id" answer.
     *
     * @param id the id of the answerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAnswer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Answer : {}", id);
        answerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
