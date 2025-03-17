package com.hugootoch.myapp.service.mapper;

import com.hugootoch.myapp.domain.Professor;
import com.hugootoch.myapp.domain.Turma;
import com.hugootoch.myapp.service.dto.ProfessorDTO;
import com.hugootoch.myapp.service.dto.TurmaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Professor} and its DTO {@link ProfessorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfessorMapper extends EntityMapper<ProfessorDTO, Professor> {
    @Mapping(target = "turmas", source = "turmas", qualifiedByName = "turmaIdSet")
    ProfessorDTO toDto(Professor s);

    @Mapping(target = "turmas", ignore = true)
    @Mapping(target = "removeTurma", ignore = true)
    Professor toEntity(ProfessorDTO professorDTO);

    @Named("turmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TurmaDTO toDtoTurmaId(Turma turma);

    @Named("turmaIdSet")
    default Set<TurmaDTO> toDtoTurmaIdSet(Set<Turma> turma) {
        return turma.stream().map(this::toDtoTurmaId).collect(Collectors.toSet());
    }
}
