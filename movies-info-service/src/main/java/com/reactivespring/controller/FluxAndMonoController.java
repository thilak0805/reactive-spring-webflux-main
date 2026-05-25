package com.reactivespring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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


}
