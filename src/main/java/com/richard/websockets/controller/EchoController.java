package com.richard.websockets.controller;

import com.richard.websockets.domain.EchoProcessRequest;
import com.richard.websockets.domain.EchoRequest;
import com.richard.websockets.service.EchoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Controller
@Slf4j
public class EchoController {

  private final EchoService echoService;

  @Autowired
  public EchoController(EchoService echoService) {
    this.echoService = echoService;
  }

  @MessageMapping("/echo")
  public void echo(EchoRequest echoRequest, Principal principal) {
    log.info("Received Request for {} and principal: {}", echoRequest, principal.getName());
    EchoProcessRequest processRequest = EchoProcessRequest
      .builder()
      .artifacts(echoRequest.getArtifacts())
      .principal(principal)
      .build();

    echoService.process(processRequest);
  }
}
