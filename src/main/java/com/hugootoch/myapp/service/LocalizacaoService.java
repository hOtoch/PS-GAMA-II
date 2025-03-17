package com.hugootoch.myapp.service;

import com.hugootoch.myapp.domain.Localizacao;
import com.hugootoch.myapp.repository.LocalizacaoRepository;
import com.hugootoch.myapp.service.dto.LocalizacaoDTO;
import com.hugootoch.myapp.service.mapper.LocalizacaoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hugootoch.myapp.domain.Localizacao}.
 */
@Service
@Transactional
public class LocalizacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(LocalizacaoService.class);

    private final LocalizacaoRepository localizacaoRepository;

    private final LocalizacaoMapper localizacaoMapper;

    public LocalizacaoService(LocalizacaoRepository localizacaoRepository, LocalizacaoMapper localizacaoMapper) {
        this.localizacaoRepository = localizacaoRepository;
        this.localizacaoMapper = localizacaoMapper;
    }

    /**
     * Save a localizacao.
     *
     * @param localizacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalizacaoDTO save(LocalizacaoDTO localizacaoDTO) {
        LOG.debug("Request to save Localizacao : {}", localizacaoDTO);
        Localizacao localizacao = localizacaoMapper.toEntity(localizacaoDTO);
        localizacao = localizacaoRepository.save(localizacao);
        return localizacaoMapper.toDto(localizacao);
    }

    /**
     * Update a localizacao.
     *
     * @param localizacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public LocalizacaoDTO update(LocalizacaoDTO localizacaoDTO) {
        LOG.debug("Request to update Localizacao : {}", localizacaoDTO);
        Localizacao localizacao = localizacaoMapper.toEntity(localizacaoDTO);
        localizacao = localizacaoRepository.save(localizacao);
        return localizacaoMapper.toDto(localizacao);
    }

    /**
     * Partially update a localizacao.
     *
     * @param localizacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LocalizacaoDTO> partialUpdate(LocalizacaoDTO localizacaoDTO) {
        LOG.debug("Request to partially update Localizacao : {}", localizacaoDTO);

        return localizacaoRepository
            .findById(localizacaoDTO.getId())
            .map(existingLocalizacao -> {
                localizacaoMapper.partialUpdate(existingLocalizacao, localizacaoDTO);

                return existingLocalizacao;
            })
            .map(localizacaoRepository::save)
            .map(localizacaoMapper::toDto);
    }

    /**
     * Get all the localizacaos.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LocalizacaoDTO> findAll() {
        LOG.debug("Request to get all Localizacaos");
        return localizacaoRepository.findAll().stream().map(localizacaoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the localizacaos where Aluno is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<LocalizacaoDTO> findAllWhereAlunoIsNull() {
        LOG.debug("Request to get all localizacaos where Aluno is null");
        return StreamSupport.stream(localizacaoRepository.findAll().spliterator(), false)
            .filter(localizacao -> localizacao.getAluno() == null)
            .map(localizacaoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one localizacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LocalizacaoDTO> findOne(Long id) {
        LOG.debug("Request to get Localizacao : {}", id);
        return localizacaoRepository.findById(id).map(localizacaoMapper::toDto);
    }

    /**
     * Delete the localizacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Localizacao : {}", id);
        localizacaoRepository.deleteById(id);
    }
}
