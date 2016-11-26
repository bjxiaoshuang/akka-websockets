package com.richard.websockets.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.richard.websockets.actors.messages.ChildRequestMsg;
import com.richard.websockets.actors.messages.ProcessRequestCompleteMsg;
import com.richard.websockets.domain.EchoChildRequest;
import com.richard.websockets.domain.EchoProcessResponse;
import com.richard.websockets.util.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Component("childEchoActor")
@Scope("prototype")
public class ChildEchoActor extends AbstractActor {
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  @Autowired
  public ChildEchoActor(SimpMessagingTemplate simpMessagingTemplate) {
    receive(ReceiveBuilder
      .match(ChildRequestMsg.class, childRequestMsg -> {
        EchoChildRequest childRequest = childRequestMsg.getChildRequest();
        String destination = childRequest
          .getPrincipal().getName();

        String message = "ChildEchoActor: Processing Artifact " + childRequest.getArtifact();

        simpMessagingTemplate.convertAndSendToUser(destination,
          Topics.ECHO_TOPIC,
          EchoProcessResponse.builder()
            .message(message)
            .completed(false)
            .timestamp(LocalDateTime.now())
            .build());

        ActorSelection actorSelection = context().actorSelection(childRequestMsg.getPath());
        ProcessRequestCompleteMsg processRequestCompleteMsg =
          ProcessRequestCompleteMsg.builder()
            .artifact(childRequest.getArtifact())
            .build();
        actorSelection.tell(processRequestCompleteMsg, ActorRef.noSender());

        context().stop(self());
      })
      .matchAny((err) -> {

      }).build());
  }
}
