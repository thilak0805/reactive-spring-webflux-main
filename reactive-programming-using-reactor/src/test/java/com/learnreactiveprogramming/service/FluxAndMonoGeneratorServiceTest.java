package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new
            FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        // StepVerifier is reactor test library
        //the below create method takes care of calling subscribe call
        StepVerifier.create(namesFlux)
                //.expectNext("alex","ben","chloe")
               // .expectNextCount(3)
                //Mix and Match first get one element and then try with count
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }
}