package com.hugootoch.myapp.web.rest;

import com.hugootoch.myapp.repository.TurmaRepository;
import com.hugootoch.myapp.service.TurmaService;
import com.hugootoch.myapp.service.dto.TurmaDTO;
import com.hugootoch.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hugootoch.myapp.domain.Turma}.
 */
@RestController
@RequestMapping("/api/turmas")
public class TurmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(TurmaResource.class);

    private static final String ENTITY_NAME = "turma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TurmaService turmaService;

    private final TurmaRepository turmaRepository;

    public TurmaResource(TurmaService turmaService, TurmaRepository turmaRepository) {
        this.turmaService = turmaService;
        this.turmaRepository = turmaRepository;
    }

    /**
     * {@code POST  /turmas} : Create a new turma.
     *
     * @param turmaDTO the turmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new turmaDTO, or with status {@code 400 (Bad Request)} if the turma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TurmaDTO> createTurma(@Valid @RequestBody TurmaDTO turmaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Turma : {}", turmaDTO);
        if (turmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new turma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        turmaDTO = turmaService.save(turmaDTO);
        return ResponseEntity.created(new URI("/api/turmas/" + turmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, turmaDTO.getId().toString()))
            .body(turmaDTO);
    }

    /**
     * {@code PUT  /turmas/:id} : Updates an existing turma.
     *
     * @param id the id of the turmaDTO to save.
     * @param turmaDTO the turmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turmaDTO,
     * or with status {@code 400 (Bad Request)} if the turmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the turmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TurmaDTO> updateTurma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TurmaDTO turmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Turma : {}, {}", id, turmaDTO);
        if (turmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        turmaDTO = turmaService.update(turmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, turmaDTO.getId().toString()))
            .body(turmaDTO);
    }

    /**
     * {@code PATCH  /turmas/:id} : Partial updates given fields of an existing turma, field will ignore if it is null
     *
     * @param id the id of the turmaDTO to save.
     * @param turmaDTO the turmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated turmaDTO,
     * or with status {@code 400 (Bad Request)} if the turmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the turmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the turmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TurmaDTO> partialUpdateTurma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TurmaDTO turmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Turma partially : {}, {}", id, turmaDTO);
        if (turmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, turmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!turmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TurmaDTO> result = turmaService.partialUpdate(turmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, turmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /turmas} : get all the turmas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of turmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TurmaDTO>> getAllTurmas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Turmas");
        Page<TurmaDTO> page;
        if (eagerload) {
            page = turmaService.findAllWithEagerRelationships(pageable);
        } else {
            page = turmaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /turmas/:id} : get the "id" turma.
     *
     * @param id the id of the turmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the turmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TurmaDTO> getTurma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Turma : {}", id);
        Optional<TurmaDTO> turmaDTO = turmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(turmaDTO);
    }

    /**
     * {@code DELETE  /turmas/:id} : delete the "id" turma.
     *
     * @param id the id of the turmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTurma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Turma : {}", id);
        turmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
