package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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

    /**
     *
     *
     * transform
     * -used to transform from one type to another
     * -Accepts Function functional interface
     * -Function functional interface released as part of java8
     * -input - Publisher(Flux or Mono)
     * -Output - Publisher (Flux or Mono)
     * --advantage we can extract the functionality and assign it to a variable and resuse the logic
     * if you have similar kind of logic thats needed for the whole project
     */
    public Flux<String> namesFlux_transform(int stringLength){

        Function<Flux<String>, Flux<String>> filterMap = name-> name.map(String::toUpperCase)
                .filter(s->s.length() > stringLength);

       // Flux.empty();

        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .transform(filterMap)
                .flatMap(s-> splitString(s)) //ALEX => Flux(A,L,E,X)
                /*
                * defaultIfEmpty
---------------------------------
1. it is not mandatory for a data source to emit the data always, for example when we give a call to db which will return flux or mono
but for the request thats been made, there might be not data to return, in this case we will get onComplete() event there is no onNext() event/
2. In this case we can use defaultIfEmpty or switchIfEmpty operator to provide default values.
* */
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_transform_switchIfEmpty(int stringLength){

        Function<Flux<String>, Flux<String>> filterMap =
                name-> name.map(String::toUpperCase)
                            .filter(s->s.length() > stringLength)
                                 .flatMap(s-> splitString(s));

        //create a publisher
       var defaultFlux =  Flux.just("default")
                .transform(filterMap) ; // this will return "D","E","F","A","U","L","T"
                //transform allows you to reuse the functional interface that is alrady defined with the implemented functionality in it
                // this is to make sure when you give a default value, that is also going to behave the same way as per the other implementation
        //filter the string whose length is greater than 3
        return Flux.fromIterable(List.of("alex","ben","chloe"))
                .transform(filterMap)
                //ALEX => Flux(A,L,E,X)
                //switchIfEmpty accepts a publisher, the publisher could be flux or mono , in our case we are creating a Flux and it will be Flux("default") String
                // and then we applying the same transform operation check lino.146 .transform(filterMap)
                .switchIfEmpty(defaultFlux)
                .log();
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


    //concat and concatwith()
    /*
    *  Concat and ConcatWith()
 -----------------------------
 used to combine two reactive streams into one

 app-> api1 => flux(a,b,c)
 app -> api2 => flux(d,e,f)
 we need to combine these two flux and produce one output =>flux(a,b,c,d,e,f)
 -concatination of reactive streams happens in sequence
  - first one is subscribed first and completes.
  -second reactive stream will wait for oncompletion signal from the first one , then second one is subscribed after and completes.
 - concat() static method in flux
 - concatwith() instance method with flux and mono
 - both of these above operators work similarly

 * */

    public Flux<String> explore_concat(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> explore_concatWith(){
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        //concatination happening with the instance method
        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_mono(){
        var aMono = Mono.just("A");
        var bMono = Flux.just("B");
        //concatination happening with the instance method
        //it returns a Flux with Flux of A,B
        return aMono.concatWith(bMono).log();
    }

}
