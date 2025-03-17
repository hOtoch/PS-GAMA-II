package com.hugootoch.myapp.service.mapper;

import com.hugootoch.myapp.domain.Localizacao;
import com.hugootoch.myapp.service.dto.LocalizacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Localizacao} and its DTO {@link LocalizacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocalizacaoMapper extends EntityMapper<LocalizacaoDTO, Localizacao> {}
