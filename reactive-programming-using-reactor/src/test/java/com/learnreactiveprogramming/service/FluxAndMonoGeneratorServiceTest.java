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

    @Test
    void namesFlux_map() {
        var namesFluxMap = fluxAndMonoGeneratorService.namesFlux_map();

        StepVerifier.create(namesFluxMap)
                .expectNext("ALEX","BEN","CHLOE")
                .verifyComplete();

    }

    @Test
    void namesFlux_immutability() {
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_immutability();
        //then
        StepVerifier.create(namesFlux)
               /* .expectNext("ALEX","BEN","CHLOE")*/
                .expectNext("alex","ben","chloe")
                .verifyComplete();

    }

    @Test
    void testNamesFlux_map() {

        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_map(stringLength);
        //then
        StepVerifier.create(namesFlux)
                //.expectNext("ALEX","BEN","CHLOE")//test case failes because BEN string is only 3 we are expecting >3
               // .expectNext("ALEX","CHLOE")
                .expectNext("4-ALEX","5-CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatmap() {
        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X","C","H","L","O","E")
                .verifyComplete();
    }
}