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

    public Flux<String> namesFlux_map(){
        // this list may be from db or remote service call
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
               // .map(s->s.toUpperCase())
                .log(); // log each events between the publisher and subscribers
    }

    public Flux<String> namesFlux_map(int stringLength){
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength) // 4-ALEX , 5-CHLOE
                .map(s->s.length() + "-"+s)
                .log();
    }

    public Flux<String> namesFlux_flatmap(int stringLength){
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength) //
                //ALEX => Flux(A,L,E,X)
                .flatMap(s->splitString(s)) //it's getting the flux value and flatten it like A,L,E,X
                .log();
    }

    // the below method returns ALEX => Flux(A,L,E,X)
     public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
     }

    public Flux<String> namesFlux_immutability(){
        // this list may be from db or remote service call
        var namesFlux =  Flux.fromIterable(List.of("alex","ben","chloe"));
        namesFlux.map(String::toUpperCase);
        return namesFlux;
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
