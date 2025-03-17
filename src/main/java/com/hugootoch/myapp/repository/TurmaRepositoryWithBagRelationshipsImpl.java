package com.hugootoch.myapp.repository;

import com.hugootoch.myapp.domain.Turma;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TurmaRepositoryWithBagRelationshipsImpl implements TurmaRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TURMAS_PARAMETER = "turmas";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Turma> fetchBagRelationships(Optional<Turma> turma) {
        return turma.map(this::fetchProfessors);
    }

    @Override
    public Page<Turma> fetchBagRelationships(Page<Turma> turmas) {
        return new PageImpl<>(fetchBagRelationships(turmas.getContent()), turmas.getPageable(), turmas.getTotalElements());
    }

    @Override
    public List<Turma> fetchBagRelationships(List<Turma> turmas) {
        return Optional.of(turmas).map(this::fetchProfessors).orElse(Collections.emptyList());
    }

    Turma fetchProfessors(Turma result) {
        return entityManager
            .createQuery("select turma from Turma turma left join fetch turma.professors where turma.id = :id", Turma.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Turma> fetchProfessors(List<Turma> turmas) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, turmas.size()).forEach(index -> order.put(turmas.get(index).getId(), index));
        List<Turma> result = entityManager
            .createQuery("select turma from Turma turma left join fetch turma.professors where turma in :turmas", Turma.class)
            .setParameter(TURMAS_PARAMETER, turmas)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
