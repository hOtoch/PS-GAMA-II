package com.hugootoch.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TurmaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Turma getTurmaSample1() {
        return new Turma().id(1L).nomeTurma("nomeTurma1").descricao("descricao1");
    }

    public static Turma getTurmaSample2() {
        return new Turma().id(2L).nomeTurma("nomeTurma2").descricao("descricao2");
    }

    public static Turma getTurmaRandomSampleGenerator() {
        return new Turma().id(longCount.incrementAndGet()).nomeTurma(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
