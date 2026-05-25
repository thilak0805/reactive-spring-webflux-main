package com.reactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    //Flux is reactive , it is Asynchronous and non blocking when compared to blocking code using ResponseEntity.
    //below is logs for the Flux, basically it is handled invidiually by separate server threads, it handles both request and response.
    // the server thread is not changed at all,in this example it doesn't have any blocking operations at this point.
    /**
 [ctor-http-nio-2] reactor.Flux.Array.1                     : | onNext(4)
 [ctor-http-nio-2] reactor.Flux.Array.1                     : | onNext(5)
 [ctor-http-nio-2] reactor.Flux.Array.1                     : | onNext(6)
    **/
    @GetMapping("/flux")
    public Flux<Integer> flux(){
        return Flux.just(4,5,6)
                .log();
    }

    @GetMapping("/mono")
    public Mono<String> helloWorldMono(){
        return Mono.just("hello world")
                .log();
    }

    // for every second the below api is continously sents data to the client.
    // the below MediaType.TEXT_EVENT_STREAM_VALUE -going to instructs this endpoint to produce a stream of data to the client
    // after execting the api, the output will be printed incrementally, starts with 1,2..
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream(){
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }


}
