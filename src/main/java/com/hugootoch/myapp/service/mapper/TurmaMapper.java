package com.hugootoch.myapp.service.mapper;

import com.hugootoch.myapp.domain.Professor;
import com.hugootoch.myapp.domain.Turma;
import com.hugootoch.myapp.service.dto.ProfessorDTO;
import com.hugootoch.myapp.service.dto.TurmaDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Turma} and its DTO {@link TurmaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TurmaMapper extends EntityMapper<TurmaDTO, Turma> {
    @Mapping(target = "professors", source = "professors", qualifiedByName = "professorIdSet")
    TurmaDTO toDto(Turma s);

    @Mapping(target = "removeProfessor", ignore = true)
    Turma toEntity(TurmaDTO turmaDTO);

    @Named("professorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfessorDTO toDtoProfessorId(Professor professor);

    @Named("professorIdSet")
    default Set<ProfessorDTO> toDtoProfessorIdSet(Set<Professor> professor) {
        return professor.stream().map(this::toDtoProfessorId).collect(Collectors.toSet());
    }
}
