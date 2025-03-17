package com.hugootoch.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AlunoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Aluno getAlunoSample1() {
        return new Aluno().id(1L).nome("nome1").idade(1).email("email1").celular("celular1");
    }

    public static Aluno getAlunoSample2() {
        return new Aluno().id(2L).nome("nome2").idade(2).email("email2").celular("celular2");
    }

    public static Aluno getAlunoRandomSampleGenerator() {
        return new Aluno()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .idade(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .celular(UUID.randomUUID().toString());
    }
}
