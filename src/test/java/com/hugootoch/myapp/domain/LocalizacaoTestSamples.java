package com.hugootoch.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocalizacaoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Localizacao getLocalizacaoSample1() {
        return new Localizacao().id(1L).endereco("endereco1").cidade("cidade1").estado("estado1").cep("cep1");
    }

    public static Localizacao getLocalizacaoSample2() {
        return new Localizacao().id(2L).endereco("endereco2").cidade("cidade2").estado("estado2").cep("cep2");
    }

    public static Localizacao getLocalizacaoRandomSampleGenerator() {
        return new Localizacao()
            .id(longCount.incrementAndGet())
            .endereco(UUID.randomUUID().toString())
            .cidade(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .cep(UUID.randomUUID().toString());
    }
}
