package com.hugootoch.myapp.web.rest;

import static com.hugootoch.myapp.domain.TurmaAsserts.*;
import static com.hugootoch.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugootoch.myapp.IntegrationTest;
import com.hugootoch.myapp.domain.Turma;
import com.hugootoch.myapp.repository.TurmaRepository;
import com.hugootoch.myapp.service.TurmaService;
import com.hugootoch.myapp.service.dto.TurmaDTO;
import com.hugootoch.myapp.service.mapper.TurmaMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TurmaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TurmaResourceIT {

    private static final String DEFAULT_NOME_TURMA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_TURMA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/turmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TurmaRepository turmaRepository;

    @Mock
    private TurmaRepository turmaRepositoryMock;

    @Autowired
    private TurmaMapper turmaMapper;

    @Mock
    private TurmaService turmaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurmaMockMvc;

    private Turma turma;

    private Turma insertedTurma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createEntity() {
        return new Turma().nomeTurma(DEFAULT_NOME_TURMA).descricao(DEFAULT_DESCRICAO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createUpdatedEntity() {
        return new Turma().nomeTurma(UPDATED_NOME_TURMA).descricao(UPDATED_DESCRICAO);
    }

    @BeforeEach
    public void initTest() {
        turma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTurma != null) {
            turmaRepository.delete(insertedTurma);
            insertedTurma = null;
        }
    }

    @Test
    @Transactional
    void createTurma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);
        var returnedTurmaDTO = om.readValue(
            restTurmaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turmaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TurmaDTO.class
        );

        // Validate the Turma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTurma = turmaMapper.toEntity(returnedTurmaDTO);
        assertTurmaUpdatableFieldsEquals(returnedTurma, getPersistedTurma(returnedTurma));

        insertedTurma = returnedTurma;
    }

    @Test
    @Transactional
    void createTurmaWithExistingId() throws Exception {
        // Create the Turma with an existing ID
        turma.setId(1L);
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeTurmaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        turma.setNomeTurma(null);

        // Create the Turma, which fails.
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turmaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurmas() throws Exception {
        // Initialize the database
        insertedTurma = turmaRepository.saveAndFlush(turma);

        // Get all the turmaList
        restTurmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turma.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeTurma").value(hasItem(DEFAULT_NOME_TURMA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTurmasWithEagerRelationshipsIsEnabled() throws Exception {
        when(turmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTurmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(turmaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTurmasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(turmaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTurmaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(turmaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTurma() throws Exception {
        // Initialize the database
        insertedTurma = turmaRepository.saveAndFlush(turma);

        // Get the turma
        restTurmaMockMvc
            .perform(get(ENTITY_API_URL_ID, turma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turma.getId().intValue()))
            .andExpect(jsonPath("$.nomeTurma").value(DEFAULT_NOME_TURMA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingTurma() throws Exception {
        // Get the turma
        restTurmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTurma() throws Exception {
        // Initialize the database
        insertedTurma = turmaRepository.saveAndFlush(turma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turma
        Turma updatedTurma = turmaRepository.findById(turma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTurma are not directly saved in db
        em.detach(updatedTurma);
        updatedTurma.nomeTurma(UPDATED_NOME_TURMA).descricao(UPDATED_DESCRICAO);
        TurmaDTO turmaDTO = turmaMapper.toDto(updatedTurma);

        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turmaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turmaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTurmaToMatchAllProperties(updatedTurma);
    }

    @Test
    @Transactional
    void putNonExistingTurma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turma.setId(longCount.incrementAndGet());

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turmaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turma.setId(longCount.incrementAndGet());

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(turmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turma.setId(longCount.incrementAndGet());

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(turmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurmaWithPatch() throws Exception {
        // Initialize the database
        insertedTurma = turmaRepository.saveAndFlush(turma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turma using partial update
        Turma partialUpdatedTurma = new Turma();
        partialUpdatedTurma.setId(turma.getId());

        partialUpdatedTurma.descricao(UPDATED_DESCRICAO);

        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurmaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTurma, turma), getPersistedTurma(turma));
    }

    @Test
    @Transactional
    void fullUpdateTurmaWithPatch() throws Exception {
        // Initialize the database
        insertedTurma = turmaRepository.saveAndFlush(turma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the turma using partial update
        Turma partialUpdatedTurma = new Turma();
        partialUpdatedTurma.setId(turma.getId());

        partialUpdatedTurma.nomeTurma(UPDATED_NOME_TURMA).descricao(UPDATED_DESCRICAO);

        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTurmaUpdatableFieldsEquals(partialUpdatedTurma, getPersistedTurma(partialUpdatedTurma));
    }

    @Test
    @Transactional
    void patchNonExistingTurma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turma.setId(longCount.incrementAndGet());

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turmaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turma.setId(longCount.incrementAndGet());

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(turmaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        turma.setId(longCount.incrementAndGet());

        // Create the Turma
        TurmaDTO turmaDTO = turmaMapper.toDto(turma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(turmaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurma() throws Exception {
        // Initialize the database
        insertedTurma = turmaRepository.saveAndFlush(turma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the turma
        restTurmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, turma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return turmaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Turma getPersistedTurma(Turma turma) {
        return turmaRepository.findById(turma.getId()).orElseThrow();
    }

    protected void assertPersistedTurmaToMatchAllProperties(Turma expectedTurma) {
        assertTurmaAllPropertiesEquals(expectedTurma, getPersistedTurma(expectedTurma));
    }

    protected void assertPersistedTurmaToMatchUpdatableProperties(Turma expectedTurma) {
        assertTurmaAllUpdatablePropertiesEquals(expectedTurma, getPersistedTurma(expectedTurma));
    }
}
