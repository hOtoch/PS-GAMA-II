package com.hugootoch.myapp.service.mapper;

import com.hugootoch.myapp.domain.Aluno;
import com.hugootoch.myapp.domain.Turma;
import com.hugootoch.myapp.service.dto.AlunoDTO;
import com.hugootoch.myapp.service.dto.TurmaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Aluno} and its DTO {@link AlunoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlunoMapper extends EntityMapper<AlunoDTO, Aluno> {
    @Mapping(target = "turma", source = "turma", qualifiedByName = "turmaId")
    AlunoDTO toDto(Aluno s);

    @Named("turmaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TurmaDTO toDtoTurmaId(Turma turma);
}
