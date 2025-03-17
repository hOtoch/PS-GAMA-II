package com.hugootoch.myapp.domain;

import static com.hugootoch.myapp.domain.ProfessorTestSamples.*;
import static com.hugootoch.myapp.domain.TurmaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hugootoch.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProfessorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professor.class);
        Professor professor1 = getProfessorSample1();
        Professor professor2 = new Professor();
        assertThat(professor1).isNotEqualTo(professor2);

        professor2.setId(professor1.getId());
        assertThat(professor1).isEqualTo(professor2);

        professor2 = getProfessorSample2();
        assertThat(professor1).isNotEqualTo(professor2);
    }

    @Test
    void turmaTest() {
        Professor professor = getProfessorRandomSampleGenerator();
        Turma turmaBack = getTurmaRandomSampleGenerator();

        professor.addTurma(turmaBack);
        assertThat(professor.getTurmas()).containsOnly(turmaBack);
        assertThat(turmaBack.getProfessors()).containsOnly(professor);

        professor.removeTurma(turmaBack);
        assertThat(professor.getTurmas()).doesNotContain(turmaBack);
        assertThat(turmaBack.getProfessors()).doesNotContain(professor);

        professor.turmas(new HashSet<>(Set.of(turmaBack)));
        assertThat(professor.getTurmas()).containsOnly(turmaBack);
        assertThat(turmaBack.getProfessors()).containsOnly(professor);

        professor.setTurmas(new HashSet<>());
        assertThat(professor.getTurmas()).doesNotContain(turmaBack);
        assertThat(turmaBack.getProfessors()).doesNotContain(professor);
    }
}
