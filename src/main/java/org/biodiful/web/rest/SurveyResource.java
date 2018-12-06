package org.biodiful.web.rest;

import com.codahale.metrics.annotation.Timed;

import org.biodiful.security.AuthoritiesConstants;
import org.biodiful.service.SurveyService;
import org.biodiful.web.rest.errors.BadRequestAlertException;
import org.biodiful.web.rest.errors.CustomParameterizedException;
import org.biodiful.web.rest.util.HeaderUtil;
import org.biodiful.web.rest.util.PaginationUtil;
import org.biodiful.service.dto.SurveyDTO;
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
 * REST controller for managing Survey.
 */
@RestController
@RequestMapping("/api")
public class SurveyResource {

    private final Logger log = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "survey";

    private final SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * POST  /surveys : Create a new survey.
     *
     * @param surveyDTO the surveyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new surveyDTO, or with status 400 (Bad Request) if the survey has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/surveys")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<SurveyDTO> createSurvey(@Valid @RequestBody SurveyDTO surveyDTO) throws URISyntaxException {
        log.debug("REST request to save Survey : {}", surveyDTO);
        if (surveyDTO.getId() != null) {
            throw new BadRequestAlertException("A new survey cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (surveyDTO.getFriendlyURL() != null) {
            Optional<SurveyDTO> surveyWithSameFriendlyURL = surveyService.findOneByFriendlyURL(surveyDTO.getFriendlyURL());
            if (surveyWithSameFriendlyURL.isPresent()) {
                SurveyDTO sameURL = surveyWithSameFriendlyURL.get();
                throw new CustomParameterizedException("error.friendlyurlexists", sameURL.getSurveyName(), sameURL.getFriendlyURL());
            }
        }

        SurveyDTO result = surveyService.save(surveyDTO);
        return ResponseEntity.created(new URI("/api/surveys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /surveys : Updates an existing survey.
     *
     * @param surveyDTO the surveyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated surveyDTO,
     * or with status 400 (Bad Request) if the surveyDTO is not valid,
     * or with status 500 (Internal Server Error) if the surveyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/surveys")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<SurveyDTO> updateSurvey(@Valid @RequestBody SurveyDTO surveyDTO) throws URISyntaxException {
        log.debug("REST request to update Survey : {}", surveyDTO);
        if (surveyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (surveyDTO.getFriendlyURL() != null) {
            Optional<SurveyDTO> surveyWithSameFriendlyURL = surveyService.findOneByFriendlyURL(surveyDTO.getFriendlyURL());
            if (surveyWithSameFriendlyURL.isPresent()) {
                SurveyDTO sameURL = surveyWithSameFriendlyURL.get();

                // Check that the survey with the same URL is not the one we're currently editing
                if (surveyDTO.getId() != sameURL.getId()) {
                    throw new CustomParameterizedException("error.friendlyurlexists", sameURL.getSurveyName(), sameURL.getFriendlyURL());
                }
            }
        }

        SurveyDTO result = surveyService.save(surveyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, surveyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /surveys : get all the surveys.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of surveys in body
     */
    @GetMapping("/surveys")
    @Timed
    @PermitAll
    public ResponseEntity<List<SurveyDTO>> getAllSurveys(Pageable pageable) {
        log.debug("REST request to get a page of Surveys");
        Page<SurveyDTO> page = surveyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/surveys");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /surveys/:id : get the "id" survey.
     *
     * @param id the id of the surveyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the surveyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/surveys/{id}")
    @Timed
    @PermitAll
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable Long id) {
        log.debug("REST request to get Survey : {}", id);
        Optional<SurveyDTO> surveyDTO = surveyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surveyDTO);
    }

    /**
     * GET  /surveys/:id : get the survey from its friendly URL.
     *
     * @param friendlyURL the friendlyURL of the surveyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the surveyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/surveys/friendly-url/{friendlyURL}")
    @Timed
    @PermitAll
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable String friendlyURL) {
        log.debug("REST request to get Survey from friendlyURL: {}", friendlyURL);
        Optional<SurveyDTO> surveyDTO = surveyService.findOneByFriendlyURL(friendlyURL);
        return ResponseUtil.wrapOrNotFound(surveyDTO);
    }

    /**
     * GET  /surveys/:id : get the number of judges that have answered a survey.
     *
     * @param id the id of the survey
     * @return the ResponseEntity with status 200 (OK) and with body the number of judges that have answered this survey
     */
    @GetMapping("/surveys/{id}/judges-count")
    @Timed
    @PermitAll
    public ResponseEntity<Long> getSurveyJudgesCount(@PathVariable Long id) {
        log.debug("REST request to get the number of jusges that have answered the Survey : {}", id);
        return ResponseEntity.ok().body(surveyService.countJudgesForSurvey(id));
    }

    /**
     * DELETE  /surveys/:id : delete the "id" survey.
     *
     * @param id the id of the surveyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/surveys/{id}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        log.debug("REST request to delete Survey : {}", id);
        surveyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
