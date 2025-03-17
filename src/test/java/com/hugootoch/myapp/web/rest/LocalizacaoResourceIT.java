package com.hugootoch.myapp.web.rest;

import static com.hugootoch.myapp.domain.LocalizacaoAsserts.*;
import static com.hugootoch.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugootoch.myapp.IntegrationTest;
import com.hugootoch.myapp.domain.Localizacao;
import com.hugootoch.myapp.repository.LocalizacaoRepository;
import com.hugootoch.myapp.service.dto.LocalizacaoDTO;
import com.hugootoch.myapp.service.mapper.LocalizacaoMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link LocalizacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocalizacaoResourceIT {

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/localizacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Autowired
    private LocalizacaoMapper localizacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalizacaoMockMvc;

    private Localizacao localizacao;

    private Localizacao insertedLocalizacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localizacao createEntity() {
        return new Localizacao().endereco(DEFAULT_ENDERECO).cidade(DEFAULT_CIDADE).estado(DEFAULT_ESTADO).cep(DEFAULT_CEP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localizacao createUpdatedEntity() {
        return new Localizacao().endereco(UPDATED_ENDERECO).cidade(UPDATED_CIDADE).estado(UPDATED_ESTADO).cep(UPDATED_CEP);
    }

    @BeforeEach
    public void initTest() {
        localizacao = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocalizacao != null) {
            localizacaoRepository.delete(insertedLocalizacao);
            insertedLocalizacao = null;
        }
    }

    @Test
    @Transactional
    void createLocalizacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);
        var returnedLocalizacaoDTO = om.readValue(
            restLocalizacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LocalizacaoDTO.class
        );

        // Validate the Localizacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLocalizacao = localizacaoMapper.toEntity(returnedLocalizacaoDTO);
        assertLocalizacaoUpdatableFieldsEquals(returnedLocalizacao, getPersistedLocalizacao(returnedLocalizacao));

        insertedLocalizacao = returnedLocalizacao;
    }

    @Test
    @Transactional
    void createLocalizacaoWithExistingId() throws Exception {
        // Create the Localizacao with an existing ID
        localizacao.setId(1L);
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalizacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnderecoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localizacao.setEndereco(null);

        // Create the Localizacao, which fails.
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        restLocalizacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localizacao.setCidade(null);

        // Create the Localizacao, which fails.
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        restLocalizacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localizacao.setEstado(null);

        // Create the Localizacao, which fails.
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        restLocalizacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localizacao.setCep(null);

        // Create the Localizacao, which fails.
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        restLocalizacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalizacaos() throws Exception {
        // Initialize the database
        insertedLocalizacao = localizacaoRepository.saveAndFlush(localizacao);

        // Get all the localizacaoList
        restLocalizacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localizacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)));
    }

    @Test
    @Transactional
    void getLocalizacao() throws Exception {
        // Initialize the database
        insertedLocalizacao = localizacaoRepository.saveAndFlush(localizacao);

        // Get the localizacao
        restLocalizacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, localizacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localizacao.getId().intValue()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP));
    }

    @Test
    @Transactional
    void getNonExistingLocalizacao() throws Exception {
        // Get the localizacao
        restLocalizacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocalizacao() throws Exception {
        // Initialize the database
        insertedLocalizacao = localizacaoRepository.saveAndFlush(localizacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localizacao
        Localizacao updatedLocalizacao = localizacaoRepository.findById(localizacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocalizacao are not directly saved in db
        em.detach(updatedLocalizacao);
        updatedLocalizacao.endereco(UPDATED_ENDERECO).cidade(UPDATED_CIDADE).estado(UPDATED_ESTADO).cep(UPDATED_CEP);
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(updatedLocalizacao);

        restLocalizacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localizacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localizacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocalizacaoToMatchAllProperties(updatedLocalizacao);
    }

    @Test
    @Transactional
    void putNonExistingLocalizacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localizacao.setId(longCount.incrementAndGet());

        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalizacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localizacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalizacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localizacao.setId(longCount.incrementAndGet());

        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalizacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalizacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localizacao.setId(longCount.incrementAndGet());

        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalizacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalizacaoWithPatch() throws Exception {
        // Initialize the database
        insertedLocalizacao = localizacaoRepository.saveAndFlush(localizacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localizacao using partial update
        Localizacao partialUpdatedLocalizacao = new Localizacao();
        partialUpdatedLocalizacao.setId(localizacao.getId());

        partialUpdatedLocalizacao.endereco(UPDATED_ENDERECO).cidade(UPDATED_CIDADE);

        restLocalizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalizacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocalizacao))
            )
            .andExpect(status().isOk());

        // Validate the Localizacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalizacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocalizacao, localizacao),
            getPersistedLocalizacao(localizacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateLocalizacaoWithPatch() throws Exception {
        // Initialize the database
        insertedLocalizacao = localizacaoRepository.saveAndFlush(localizacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localizacao using partial update
        Localizacao partialUpdatedLocalizacao = new Localizacao();
        partialUpdatedLocalizacao.setId(localizacao.getId());

        partialUpdatedLocalizacao.endereco(UPDATED_ENDERECO).cidade(UPDATED_CIDADE).estado(UPDATED_ESTADO).cep(UPDATED_CEP);

        restLocalizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalizacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocalizacao))
            )
            .andExpect(status().isOk());

        // Validate the Localizacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalizacaoUpdatableFieldsEquals(partialUpdatedLocalizacao, getPersistedLocalizacao(partialUpdatedLocalizacao));
    }

    @Test
    @Transactional
    void patchNonExistingLocalizacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localizacao.setId(longCount.incrementAndGet());

        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localizacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(localizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalizacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localizacao.setId(longCount.incrementAndGet());

        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalizacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(localizacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalizacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localizacao.setId(longCount.incrementAndGet());

        // Create the Localizacao
        LocalizacaoDTO localizacaoDTO = localizacaoMapper.toDto(localizacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalizacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(localizacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Localizacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalizacao() throws Exception {
        // Initialize the database
        insertedLocalizacao = localizacaoRepository.saveAndFlush(localizacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the localizacao
        restLocalizacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, localizacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return localizacaoRepository.count();
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

    protected Localizacao getPersistedLocalizacao(Localizacao localizacao) {
        return localizacaoRepository.findById(localizacao.getId()).orElseThrow();
    }

    protected void assertPersistedLocalizacaoToMatchAllProperties(Localizacao expectedLocalizacao) {
        assertLocalizacaoAllPropertiesEquals(expectedLocalizacao, getPersistedLocalizacao(expectedLocalizacao));
    }

    protected void assertPersistedLocalizacaoToMatchUpdatableProperties(Localizacao expectedLocalizacao) {
        assertLocalizacaoAllUpdatablePropertiesEquals(expectedLocalizacao, getPersistedLocalizacao(expectedLocalizacao));
    }
}
