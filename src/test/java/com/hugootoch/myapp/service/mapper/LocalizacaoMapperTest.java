package com.hugootoch.myapp.service.mapper;

import static com.hugootoch.myapp.domain.LocalizacaoAsserts.*;
import static com.hugootoch.myapp.domain.LocalizacaoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalizacaoMapperTest {

    private LocalizacaoMapper localizacaoMapper;

    @BeforeEach
    void setUp() {
        localizacaoMapper = new LocalizacaoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLocalizacaoSample1();
        var actual = localizacaoMapper.toEntity(localizacaoMapper.toDto(expected));
        assertLocalizacaoAllPropertiesEquals(expected, actual);
    }
}
