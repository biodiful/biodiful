package org.biodiful.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.biodiful.security.AuthoritiesConstants;
import org.biodiful.service.AnswerService;
import org.biodiful.web.rest.errors.BadRequestAlertException;
import org.biodiful.web.rest.util.HeaderUtil;
import org.biodiful.web.rest.util.PaginationUtil;
import org.biodiful.service.dto.AnswerDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Answer.
 */
@RestController
@RequestMapping("/api")
public class AnswerResource {

    private final Logger log = LoggerFactory.getLogger(AnswerResource.class);

    private static final String ENTITY_NAME = "answer";

    private final AnswerService answerService;

    public AnswerResource(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * POST  /answers : Create a new answer.
     *
     * @param answerDTO the answerDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new answerDTO, or with status 400 (Bad Request) if the answer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/answers")
    @Timed
    @PermitAll
    public ResponseEntity<AnswerDTO> createAnswer(@Valid @RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        log.debug("REST request to save Answer : {}", answerDTO);
        if (answerDTO.getId() != null) {
            throw new BadRequestAlertException("A new answer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnswerDTO result = answerService.save(answerDTO);
        return ResponseEntity.created(new URI("/api/answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /answers : Updates an existing answer.
     *
     * @param answerDTO the answerDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated answerDTO,
     * or with status 400 (Bad Request) if the answerDTO is not valid,
     * or with status 500 (Internal Server Error) if the answerDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/answers")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AnswerDTO> updateAnswer(@Valid @RequestBody AnswerDTO answerDTO) throws URISyntaxException {
        log.debug("REST request to update Answer : {}", answerDTO);
        if (answerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnswerDTO result = answerService.save(answerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, answerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /answers : get all the answers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of answers in body
     */
    @GetMapping("/answers")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<AnswerDTO>> getAllAnswers(Pageable pageable) {
        log.debug("REST request to get a page of Answers");
        Page<AnswerDTO> page = answerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/answers");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /answers/:id : get the "id" answer.
     *
     * @param id the id of the answerDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the answerDTO, or with status 404 (Not Found)
     */
    @GetMapping("/answers/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<AnswerDTO> getAnswer(@PathVariable Long id) {
        log.debug("REST request to get Answer : {}", id);
        Optional<AnswerDTO> answerDTO = answerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(answerDTO);
    }

    /**
     * DELETE  /answers/:id : delete the "id" answer.
     *
     * @param id the id of the answerDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/answers/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        log.debug("REST request to delete Answer : {}", id);
        answerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
