package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    //publisher
    public Flux<String> namesFlux(){
    // this list may be from db or remote service call
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .log(); // log each events between the publisher and subscribers

    }

    public Mono<String> namesMono(){
        return Mono.just("alex")
                .log();
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

        //only way to access this elements is to subscribe it, so we calling subscribe method
        // subscriber as we subscribe to it the publisher
        // no events will flow until you call the subscribe method to it
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
