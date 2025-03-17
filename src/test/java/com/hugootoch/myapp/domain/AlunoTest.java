package com.hugootoch.myapp.domain;

import static com.hugootoch.myapp.domain.AlunoTestSamples.*;
import static com.hugootoch.myapp.domain.LocalizacaoTestSamples.*;
import static com.hugootoch.myapp.domain.MetaTestSamples.*;
import static com.hugootoch.myapp.domain.TurmaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hugootoch.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aluno.class);
        Aluno aluno1 = getAlunoSample1();
        Aluno aluno2 = new Aluno();
        assertThat(aluno1).isNotEqualTo(aluno2);

        aluno2.setId(aluno1.getId());
        assertThat(aluno1).isEqualTo(aluno2);

        aluno2 = getAlunoSample2();
        assertThat(aluno1).isNotEqualTo(aluno2);
    }

    @Test
    void localizacaoTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Localizacao localizacaoBack = getLocalizacaoRandomSampleGenerator();

        aluno.setLocalizacao(localizacaoBack);
        assertThat(aluno.getLocalizacao()).isEqualTo(localizacaoBack);

        aluno.localizacao(null);
        assertThat(aluno.getLocalizacao()).isNull();
    }

    @Test
    void metaTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Meta metaBack = getMetaRandomSampleGenerator();

        aluno.addMeta(metaBack);
        assertThat(aluno.getMetas()).containsOnly(metaBack);
        assertThat(metaBack.getAluno()).isEqualTo(aluno);

        aluno.removeMeta(metaBack);
        assertThat(aluno.getMetas()).doesNotContain(metaBack);
        assertThat(metaBack.getAluno()).isNull();

        aluno.metas(new HashSet<>(Set.of(metaBack)));
        assertThat(aluno.getMetas()).containsOnly(metaBack);
        assertThat(metaBack.getAluno()).isEqualTo(aluno);

        aluno.setMetas(new HashSet<>());
        assertThat(aluno.getMetas()).doesNotContain(metaBack);
        assertThat(metaBack.getAluno()).isNull();
    }

    @Test
    void turmaTest() {
        Aluno aluno = getAlunoRandomSampleGenerator();
        Turma turmaBack = getTurmaRandomSampleGenerator();

        aluno.setTurma(turmaBack);
        assertThat(aluno.getTurma()).isEqualTo(turmaBack);

        aluno.turma(null);
        assertThat(aluno.getTurma()).isNull();
    }
}
