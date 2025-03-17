package com.hugootoch.myapp.web.rest;

import com.hugootoch.myapp.repository.LocalizacaoRepository;
import com.hugootoch.myapp.service.LocalizacaoService;
import com.hugootoch.myapp.service.dto.LocalizacaoDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hugootoch.myapp.domain.Localizacao}.
 */
@RestController
@RequestMapping("/api/localizacaos")
public class LocalizacaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(LocalizacaoResource.class);

    private static final String ENTITY_NAME = "localizacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalizacaoService localizacaoService;

    private final LocalizacaoRepository localizacaoRepository;

    public LocalizacaoResource(LocalizacaoService localizacaoService, LocalizacaoRepository localizacaoRepository) {
        this.localizacaoService = localizacaoService;
        this.localizacaoRepository = localizacaoRepository;
    }

    /**
     * {@code POST  /localizacaos} : Create a new localizacao.
     *
     * @param localizacaoDTO the localizacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localizacaoDTO, or with status {@code 400 (Bad Request)} if the localizacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LocalizacaoDTO> createLocalizacao(@Valid @RequestBody LocalizacaoDTO localizacaoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Localizacao : {}", localizacaoDTO);
        if (localizacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new localizacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        localizacaoDTO = localizacaoService.save(localizacaoDTO);
        return ResponseEntity.created(new URI("/api/localizacaos/" + localizacaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, localizacaoDTO.getId().toString()))
            .body(localizacaoDTO);
    }

    /**
     * {@code PUT  /localizacaos/:id} : Updates an existing localizacao.
     *
     * @param id the id of the localizacaoDTO to save.
     * @param localizacaoDTO the localizacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localizacaoDTO,
     * or with status {@code 400 (Bad Request)} if the localizacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localizacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocalizacaoDTO> updateLocalizacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocalizacaoDTO localizacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Localizacao : {}, {}", id, localizacaoDTO);
        if (localizacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localizacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localizacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        localizacaoDTO = localizacaoService.update(localizacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localizacaoDTO.getId().toString()))
            .body(localizacaoDTO);
    }

    /**
     * {@code PATCH  /localizacaos/:id} : Partial updates given fields of an existing localizacao, field will ignore if it is null
     *
     * @param id the id of the localizacaoDTO to save.
     * @param localizacaoDTO the localizacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localizacaoDTO,
     * or with status {@code 400 (Bad Request)} if the localizacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the localizacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the localizacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocalizacaoDTO> partialUpdateLocalizacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocalizacaoDTO localizacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Localizacao partially : {}, {}", id, localizacaoDTO);
        if (localizacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localizacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localizacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocalizacaoDTO> result = localizacaoService.partialUpdate(localizacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localizacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /localizacaos} : get all the localizacaos.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localizacaos in body.
     */
    @GetMapping("")
    public List<LocalizacaoDTO> getAllLocalizacaos(@RequestParam(name = "filter", required = false) String filter) {
        if ("aluno-is-null".equals(filter)) {
            LOG.debug("REST request to get all Localizacaos where aluno is null");
            return localizacaoService.findAllWhereAlunoIsNull();
        }
        LOG.debug("REST request to get all Localizacaos");
        return localizacaoService.findAll();
    }

    /**
     * {@code GET  /localizacaos/:id} : get the "id" localizacao.
     *
     * @param id the id of the localizacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localizacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LocalizacaoDTO> getLocalizacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Localizacao : {}", id);
        Optional<LocalizacaoDTO> localizacaoDTO = localizacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localizacaoDTO);
    }

    /**
     * {@code DELETE  /localizacaos/:id} : delete the "id" localizacao.
     *
     * @param id the id of the localizacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalizacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Localizacao : {}", id);
        localizacaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
