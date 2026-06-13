package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest (controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {


    //we need restclient in order to interact with api in FluxAndMonoController class , so in webflux
// we are going to use webtest client autowiring webtestclient
    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux() {

        webTestClient.get()
                .uri("/flux")
                .exchange()  //this method will actually invoke the endpoint
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(3);

    }
}