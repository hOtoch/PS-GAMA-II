package com.hugootoch.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Turma.
 */
@Entity
@Table(name = "turma")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Turma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome_turma", nullable = false)
    private String nomeTurma;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "turma")
    @JsonIgnoreProperties(value = { "localizacao", "metas", "turma" }, allowSetters = true)
    private Set<Aluno> alunos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_turma__professor",
        joinColumns = @JoinColumn(name = "turma_id"),
        inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    @JsonIgnoreProperties(value = { "turmas" }, allowSetters = true)
    private Set<Professor> professors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Turma id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTurma() {
        return this.nomeTurma;
    }

    public Turma nomeTurma(String nomeTurma) {
        this.setNomeTurma(nomeTurma);
        return this;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Turma descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(Set<Aluno> alunos) {
        if (this.alunos != null) {
            this.alunos.forEach(i -> i.setTurma(null));
        }
        if (alunos != null) {
            alunos.forEach(i -> i.setTurma(this));
        }
        this.alunos = alunos;
    }

    public Turma alunos(Set<Aluno> alunos) {
        this.setAlunos(alunos);
        return this;
    }

    public Turma addAluno(Aluno aluno) {
        this.alunos.add(aluno);
        aluno.setTurma(this);
        return this;
    }

    public Turma removeAluno(Aluno aluno) {
        this.alunos.remove(aluno);
        aluno.setTurma(null);
        return this;
    }

    public Set<Professor> getProfessors() {
        return this.professors;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

    public Turma professors(Set<Professor> professors) {
        this.setProfessors(professors);
        return this;
    }

    public Turma addProfessor(Professor professor) {
        this.professors.add(professor);
        return this;
    }

    public Turma removeProfessor(Professor professor) {
        this.professors.remove(professor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turma)) {
            return false;
        }
        return getId() != null && getId().equals(((Turma) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Turma{" +
            "id=" + getId() +
            ", nomeTurma='" + getNomeTurma() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
