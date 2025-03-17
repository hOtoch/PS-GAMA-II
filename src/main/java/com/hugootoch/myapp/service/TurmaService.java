package com.hugootoch.myapp.service;

import com.hugootoch.myapp.domain.Turma;
import com.hugootoch.myapp.repository.TurmaRepository;
import com.hugootoch.myapp.service.dto.TurmaDTO;
import com.hugootoch.myapp.service.mapper.TurmaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hugootoch.myapp.domain.Turma}.
 */
@Service
@Transactional
public class TurmaService {

    private static final Logger LOG = LoggerFactory.getLogger(TurmaService.class);

    private final TurmaRepository turmaRepository;

    private final TurmaMapper turmaMapper;

    public TurmaService(TurmaRepository turmaRepository, TurmaMapper turmaMapper) {
        this.turmaRepository = turmaRepository;
        this.turmaMapper = turmaMapper;
    }

    /**
     * Save a turma.
     *
     * @param turmaDTO the entity to save.
     * @return the persisted entity.
     */
    public TurmaDTO save(TurmaDTO turmaDTO) {
        LOG.debug("Request to save Turma : {}", turmaDTO);
        Turma turma = turmaMapper.toEntity(turmaDTO);
        turma = turmaRepository.save(turma);
        return turmaMapper.toDto(turma);
    }

    /**
     * Update a turma.
     *
     * @param turmaDTO the entity to save.
     * @return the persisted entity.
     */
    public TurmaDTO update(TurmaDTO turmaDTO) {
        LOG.debug("Request to update Turma : {}", turmaDTO);
        Turma turma = turmaMapper.toEntity(turmaDTO);
        turma = turmaRepository.save(turma);
        return turmaMapper.toDto(turma);
    }

    /**
     * Partially update a turma.
     *
     * @param turmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TurmaDTO> partialUpdate(TurmaDTO turmaDTO) {
        LOG.debug("Request to partially update Turma : {}", turmaDTO);

        return turmaRepository
            .findById(turmaDTO.getId())
            .map(existingTurma -> {
                turmaMapper.partialUpdate(existingTurma, turmaDTO);

                return existingTurma;
            })
            .map(turmaRepository::save)
            .map(turmaMapper::toDto);
    }

    /**
     * Get all the turmas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TurmaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Turmas");
        return turmaRepository.findAll(pageable).map(turmaMapper::toDto);
    }

    /**
     * Get all the turmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TurmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return turmaRepository.findAllWithEagerRelationships(pageable).map(turmaMapper::toDto);
    }

    /**
     * Get one turma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TurmaDTO> findOne(Long id) {
        LOG.debug("Request to get Turma : {}", id);
        return turmaRepository.findOneWithEagerRelationships(id).map(turmaMapper::toDto);
    }

    /**
     * Delete the turma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Turma : {}", id);
        turmaRepository.deleteById(id);
    }
}
