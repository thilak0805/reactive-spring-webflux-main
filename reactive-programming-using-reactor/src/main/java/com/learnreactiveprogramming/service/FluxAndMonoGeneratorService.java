package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;

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

    // after introducing constant delay of 1000ms, the below method executed in 5second, wheareas the namesFlux_concatmap executes in 9ms
    // its a tradeoff when to use flatmap and concatmap
    public Flux<String> namesFlux_flatmapAsync(int stringLength){
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength) //
                //ALEX => Flux(A,L,E,X)
                .flatMap(s->splitStringWithDelay(s)) //it's getting the flux value and flatten it like A,L,E,X
                .log();
    }

    //concatMap preserves ordering of the elements in reactive streams, best used for asynchronous operations
    //overall time take to execute by concatMap is higher than flatMap operator
    //eventhough the calls were made async, it waits for each and every element to complete and goes to next element
    public Flux<String> namesFlux_concatmap(int stringLength){
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength) //
                //ALEX => Flux(A,L,E,X)
                .concatMap(s->splitStringWithDelay(s)) //it's getting the flux value and flatten it like A,L,E,X
                .log();
    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength);
    }

    //======================when do you flatMap====================================
    //use flatmap if the transformation involves making a REST API call or any kind of
    //functionality that can be done asynchronously. or any function that return another Mono.

    //flatMapMono=> Anytime you are using Mono and  if your  transformation if you are calling a function again returns another Mono,
    // then in this case you will be using flatmap

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .flatMap(this::splitStringMono) ;// need to have Mono<List of A, L, E, x>
    }

    //-when mono transformation logic returns a flux then we use flatMapMany()
    //flatMapMany(this::splitStringMono (this is mono)) so instead use flatMapMany(this::splitString) will only use the function which returns flux
    //any time if we have a transformation that returns a flux in your mono pipeline then we use flatMapMany
    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s->s.length() > stringLength)
                .flatMapMany(this::splitString) ;// need to have Mono<List of A, L, E, x>
    }
    
    private Mono<List<String>> splitStringMono(String s){
        var charArray = s.split("");
        var charList = List.of(charArray); //ALEX -> will be List of (A,L,E,X)
        return Mono.just(charList);
    }

    // the below method returns ALEX => Flux(A,L,E,X)
     public Flux<String> splitString(String name){
        var charArray = name.split("");
        return Flux.fromArray(charArray);
     }

    public Flux<String> splitStringWithDelay(String name){
        var charArray = name.split("");
       // var delay = new Random().nextInt(10000);
        var delay = 1000;
        return Flux.fromArray(charArray)
                .delayElements(Duration.ofMillis(delay));
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
