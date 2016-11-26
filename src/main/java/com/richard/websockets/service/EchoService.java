package com.richard.websockets.service;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.richard.websockets.actors.messages.ProcessRequestStartMsg;
import com.richard.websockets.domain.EchoProcessRequest;
import com.richard.websockets.domain.EchoProcessResponse;
import com.richard.websockets.util.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

import static com.richard.websockets.context.akka.SpringExtension.SpringExtProvider;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Service
public class EchoService {

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final ActorSystem actorSystem;

  private final AtomicLong actorId;

  @Autowired
  public EchoService(SimpMessagingTemplate simpMessagingTemplate, ActorSystem actorSystem, AtomicLong actorId) {
    this.simpMessagingTemplate = simpMessagingTemplate;
    this.actorSystem = actorSystem;
    this.actorId = actorId;
  }

  public void echo() {

  }

  public void process(EchoProcessRequest processRequest) {
    simpMessagingTemplate.convertAndSendToUser(processRequest.getPrincipal().getName(),
      Topics.ECHO_TOPIC,
      EchoProcessResponse.builder()
        .message("Begin processing Request " + processRequest.getArtifacts().size() + " Artifacts")
        .completed(false)
        .timestamp(LocalDateTime.now())
        .build());

    final ActorRef masterEchoActor = actorSystem.actorOf(
      SpringExtProvider.get(actorSystem).props("masterEchoActor"),
      "masterEchoActor-" + actorId.incrementAndGet());

    masterEchoActor.tell(ProcessRequestStartMsg.builder()
      .echoProcessRequest(processRequest)
      .path(masterEchoActor.path())
      .build(), ActorRef.noSender());
  }
}
