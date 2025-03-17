package com.hugootoch.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.hugootoch.myapp.domain.Turma} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TurmaDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomeTurma;

    private String descricao;

    private Set<ProfessorDTO> professors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<ProfessorDTO> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<ProfessorDTO> professors) {
        this.professors = professors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TurmaDTO)) {
            return false;
        }

        TurmaDTO turmaDTO = (TurmaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, turmaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TurmaDTO{" +
            "id=" + getId() +
            ", nomeTurma='" + getNomeTurma() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", professors=" + getProfessors() +
            "}";
    }
}
