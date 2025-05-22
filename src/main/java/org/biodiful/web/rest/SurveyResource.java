package org.biodiful.web.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.biodiful.repository.SurveyRepository;
import org.biodiful.security.AuthoritiesConstants;
import org.biodiful.service.SurveyService;
import org.biodiful.service.dto.SurveyDTO;
import org.biodiful.web.rest.errors.BadRequestAlertException;
import org.biodiful.web.rest.errors.FriendlyURLExistsException;
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
 * REST controller for managing {@link org.biodiful.domain.Survey}.
 */
@RestController
@RequestMapping("/api/surveys")
public class SurveyResource {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyResource.class);

    private static final String ENTITY_NAME = "survey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurveyService surveyService;

    private final SurveyRepository surveyRepository;

    public SurveyResource(SurveyService surveyService, SurveyRepository surveyRepository) {
        this.surveyService = surveyService;
        this.surveyRepository = surveyRepository;
    }

    /**
     * {@code POST  /surveys} : Create a new survey.
     *
     * @param surveyDTO the surveyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surveyDTO, or with status {@code 400 (Bad Request)} if the survey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<SurveyDTO> createSurvey(@Valid @RequestBody SurveyDTO surveyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Survey : {}", surveyDTO);
        if (surveyDTO.getId() != null) {
            throw new BadRequestAlertException("A new survey cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (surveyDTO.getFriendlyURL() != null) {
            Optional<SurveyDTO> surveyWithSameFriendlyURL = surveyService.findOneByFriendlyURL(surveyDTO.getFriendlyURL());
            if (surveyWithSameFriendlyURL.isPresent()) {
                SurveyDTO sameURL = surveyWithSameFriendlyURL.get();
                throw new FriendlyURLExistsException(sameURL.getSurveyName(), sameURL.getFriendlyURL());
            }
        }

        surveyDTO = surveyService.save(surveyDTO);
        return ResponseEntity.created(new URI("/api/surveys/" + surveyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, surveyDTO.getId().toString()))
            .body(surveyDTO);
    }

    /**
     * {@code PUT  /surveys/:id} : Updates an existing survey.
     *
     * @param id the id of the surveyDTO to save.
     * @param surveyDTO the surveyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyDTO,
     * or with status {@code 400 (Bad Request)} if the surveyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surveyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<SurveyDTO> updateSurvey(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SurveyDTO surveyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Survey : {}, {}", id, surveyDTO);
        if (surveyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if (surveyDTO.getFriendlyURL() != null) {
            Optional<SurveyDTO> surveyWithSameFriendlyURL = surveyService.findOneByFriendlyURL(surveyDTO.getFriendlyURL());
            if (surveyWithSameFriendlyURL.isPresent()) {
                SurveyDTO sameURL = surveyWithSameFriendlyURL.get();

                // Check that the survey with the same URL is not the one we're currently editing
                if (!Objects.equals(surveyDTO.getId(), sameURL.getId())) {
                    throw new FriendlyURLExistsException(sameURL.getSurveyName(), sameURL.getFriendlyURL());
                }
            }
        }

        surveyDTO = surveyService.update(surveyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surveyDTO.getId().toString()))
            .body(surveyDTO);
    }

    /**
     * {@code PATCH  /surveys/:id} : Partial updates given fields of an existing survey, field will ignore if it is null
     *
     * @param id the id of the surveyDTO to save.
     * @param surveyDTO the surveyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surveyDTO,
     * or with status {@code 400 (Bad Request)} if the surveyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the surveyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the surveyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<SurveyDTO> partialUpdateSurvey(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SurveyDTO surveyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Survey partially : {}, {}", id, surveyDTO);
        if (surveyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surveyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surveyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurveyDTO> result = surveyService.partialUpdate(surveyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surveyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /surveys} : get all the surveys.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surveys in body.
     */
    @GetMapping("")
    @PermitAll
    public ResponseEntity<List<SurveyDTO>> getAllSurveys(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Surveys");
        Page<SurveyDTO> page = surveyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /surveys/:id} : get the "id" survey.
     *
     * @param id the id of the surveyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surveyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PermitAll
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Survey : {}", id);
        Optional<SurveyDTO> surveyDTO = surveyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surveyDTO);
    }

    /**
     * GET  /surveys/:id : get the survey from its friendly URL.
     *
     * @param friendlyURL the friendlyURL of the surveyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the surveyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/friendly-url/{friendlyURL}")
    @PermitAll
    public ResponseEntity<SurveyDTO> getSurvey(@PathVariable String friendlyURL) {
        LOG.debug("REST request to get Survey from friendlyURL: {}", friendlyURL);
        Optional<SurveyDTO> surveyDTO = surveyService.findOneByFriendlyURL(friendlyURL);
        return ResponseUtil.wrapOrNotFound(surveyDTO);
    }

    /**
     * GET  /surveys/:id : get the number of judges that have answered a survey.
     *
     * @param id the id of the survey
     * @return the ResponseEntity with status 200 (OK) and with body the number of judges that have answered this survey
     */
    @GetMapping("/{id}/judges-count")
    @PermitAll
    public ResponseEntity<Long> getSurveyJudgesCount(@PathVariable Long id) {
        LOG.debug("REST request to get the number of jusges that have answered the Survey : {}", id);
        return ResponseEntity.ok().body(surveyService.countJudgesForSurvey(id));
    }

    /**
     * {@code DELETE  /surveys/:id} : delete the "id" survey.
     *
     * @param id the id of the surveyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteSurvey(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Survey : {}", id);
        surveyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
