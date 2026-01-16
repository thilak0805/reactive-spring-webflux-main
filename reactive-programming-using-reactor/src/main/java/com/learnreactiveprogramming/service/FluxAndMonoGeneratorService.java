package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux(){

        return Flux.fromIterable(List.of("alex","ben","chloe")); // this list may be from db or remote service call

    }

    public Mono<String> namesMono(){
        return Mono.just("alex");
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        //only way to access this elements is to subscribe it, so we calling subscribe method

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name ->{
                    System.out.println("name is :"+name);
                });

        fluxAndMonoGeneratorService.namesMono()
                .subscribe(name->{
                    System.out.println("Mono name is :"+name);
                });

    }

}
