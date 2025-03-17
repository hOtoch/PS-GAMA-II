package com.hugootoch.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.hugootoch.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalizacaoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalizacaoDTO.class);
        LocalizacaoDTO localizacaoDTO1 = new LocalizacaoDTO();
        localizacaoDTO1.setId(1L);
        LocalizacaoDTO localizacaoDTO2 = new LocalizacaoDTO();
        assertThat(localizacaoDTO1).isNotEqualTo(localizacaoDTO2);
        localizacaoDTO2.setId(localizacaoDTO1.getId());
        assertThat(localizacaoDTO1).isEqualTo(localizacaoDTO2);
        localizacaoDTO2.setId(2L);
        assertThat(localizacaoDTO1).isNotEqualTo(localizacaoDTO2);
        localizacaoDTO1.setId(null);
        assertThat(localizacaoDTO1).isNotEqualTo(localizacaoDTO2);
    }
}
