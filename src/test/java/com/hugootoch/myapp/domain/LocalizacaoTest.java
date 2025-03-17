package com.hugootoch.myapp.domain;

import static com.hugootoch.myapp.domain.AlunoTestSamples.*;
import static com.hugootoch.myapp.domain.LocalizacaoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hugootoch.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalizacaoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Localizacao.class);
        Localizacao localizacao1 = getLocalizacaoSample1();
        Localizacao localizacao2 = new Localizacao();
        assertThat(localizacao1).isNotEqualTo(localizacao2);

        localizacao2.setId(localizacao1.getId());
        assertThat(localizacao1).isEqualTo(localizacao2);

        localizacao2 = getLocalizacaoSample2();
        assertThat(localizacao1).isNotEqualTo(localizacao2);
    }

    @Test
    void alunoTest() {
        Localizacao localizacao = getLocalizacaoRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        localizacao.setAluno(alunoBack);
        assertThat(localizacao.getAluno()).isEqualTo(alunoBack);
        assertThat(alunoBack.getLocalizacao()).isEqualTo(localizacao);

        localizacao.aluno(null);
        assertThat(localizacao.getAluno()).isNull();
        assertThat(alunoBack.getLocalizacao()).isNull();
    }
}
