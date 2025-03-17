package com.hugootoch.myapp.domain;

import static com.hugootoch.myapp.domain.AlunoTestSamples.*;
import static com.hugootoch.myapp.domain.ProfessorTestSamples.*;
import static com.hugootoch.myapp.domain.TurmaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.hugootoch.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TurmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Turma.class);
        Turma turma1 = getTurmaSample1();
        Turma turma2 = new Turma();
        assertThat(turma1).isNotEqualTo(turma2);

        turma2.setId(turma1.getId());
        assertThat(turma1).isEqualTo(turma2);

        turma2 = getTurmaSample2();
        assertThat(turma1).isNotEqualTo(turma2);
    }

    @Test
    void alunoTest() {
        Turma turma = getTurmaRandomSampleGenerator();
        Aluno alunoBack = getAlunoRandomSampleGenerator();

        turma.addAluno(alunoBack);
        assertThat(turma.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getTurma()).isEqualTo(turma);

        turma.removeAluno(alunoBack);
        assertThat(turma.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getTurma()).isNull();

        turma.alunos(new HashSet<>(Set.of(alunoBack)));
        assertThat(turma.getAlunos()).containsOnly(alunoBack);
        assertThat(alunoBack.getTurma()).isEqualTo(turma);

        turma.setAlunos(new HashSet<>());
        assertThat(turma.getAlunos()).doesNotContain(alunoBack);
        assertThat(alunoBack.getTurma()).isNull();
    }

    @Test
    void professorTest() {
        Turma turma = getTurmaRandomSampleGenerator();
        Professor professorBack = getProfessorRandomSampleGenerator();

        turma.addProfessor(professorBack);
        assertThat(turma.getProfessors()).containsOnly(professorBack);

        turma.removeProfessor(professorBack);
        assertThat(turma.getProfessors()).doesNotContain(professorBack);

        turma.professors(new HashSet<>(Set.of(professorBack)));
        assertThat(turma.getProfessors()).containsOnly(professorBack);

        turma.setProfessors(new HashSet<>());
        assertThat(turma.getProfessors()).doesNotContain(professorBack);
    }
}
