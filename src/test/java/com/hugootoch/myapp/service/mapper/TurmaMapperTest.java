package com.hugootoch.myapp.service.mapper;

import static com.hugootoch.myapp.domain.TurmaAsserts.*;
import static com.hugootoch.myapp.domain.TurmaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TurmaMapperTest {

    private TurmaMapper turmaMapper;

    @BeforeEach
    void setUp() {
        turmaMapper = new TurmaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTurmaSample1();
        var actual = turmaMapper.toEntity(turmaMapper.toDto(expected));
        assertTurmaAllPropertiesEquals(expected, actual);
    }
}
