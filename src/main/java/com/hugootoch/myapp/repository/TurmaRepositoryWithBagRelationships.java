package com.hugootoch.myapp.repository;

import com.hugootoch.myapp.domain.Turma;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TurmaRepositoryWithBagRelationships {
    Optional<Turma> fetchBagRelationships(Optional<Turma> turma);

    List<Turma> fetchBagRelationships(List<Turma> turmas);

    Page<Turma> fetchBagRelationships(Page<Turma> turmas);
}
