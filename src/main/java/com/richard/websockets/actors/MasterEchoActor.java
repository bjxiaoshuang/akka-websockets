package com.richard.websockets.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.richard.websockets.actors.messages.ChildRequestMsg;
import com.richard.websockets.actors.messages.ProcessRequestCompleteMsg;
import com.richard.websockets.actors.messages.ProcessRequestFailedMsg;
import com.richard.websockets.actors.messages.ProcessRequestStartMsg;
import com.richard.websockets.domain.EchoChildRequest;
import com.richard.websockets.domain.EchoProcessResponse;
import com.richard.websockets.util.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.richard.websockets.context.akka.SpringExtension.SpringExtProvider;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Component("masterEchoActor")
@Scope("prototype")
public class MasterEchoActor extends AbstractActor {
  private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

  private List<String> requestItems;
  private List<String> responsedItems;
  private String destination;

  @Autowired
  public MasterEchoActor(ActorSystem actorSystem,
                         SimpMessagingTemplate simpMessagingTemplate,
                         AtomicLong actorId) {
    receive(ReceiveBuilder
      .match(ProcessRequestStartMsg.class, processRequestStartMsg -> {
        requestItems = processRequestStartMsg.getEchoProcessRequest().getArtifacts();
        responsedItems = new ArrayList<>(requestItems.size());
        destination = processRequestStartMsg
          .getEchoProcessRequest().getPrincipal().getName();

        String message = "Master Echo Actor Beginning processing Request "
          + processRequestStartMsg.getEchoProcessRequest().getArtifacts().size()
          + " Artifacts";

        simpMessagingTemplate.convertAndSendToUser(destination,
          Topics.ECHO_TOPIC,
          EchoProcessResponse.builder()
            .message(message)
            .completed(false)
            .timestamp(LocalDateTime.now())
            .build());

        requestItems.stream()
          .map(requestItem -> EchoChildRequest.builder()
            .artifact(requestItem)
            .principal(processRequestStartMsg.getEchoProcessRequest().getPrincipal())
            .build())
          .map(echoChildRequest -> ChildRequestMsg.builder()
            .childRequest(echoChildRequest)
            .path(self().path())
            .build())
          .forEach(childRequestMsg -> {
            final ActorRef childEchoActor = actorSystem.actorOf(
              SpringExtProvider.get(actorSystem).props("childEchoActor"),
              "childEchoActor-" + actorId.incrementAndGet());

            childEchoActor.tell(childRequestMsg, self());
          });

       /* //simulate sleeping for 5 seconds
        TimeUnit.SECONDS.sleep(5);

        message = "Master Echo Actor Done processing Request "
          + processRequestStartMsg.getEchoProcessRequest().getArtifacts().size()
          + " Artifacts";

        simpMessagingTemplate.convertAndSendToUser(destination,
          Topics.ECHO_TOPIC,
          EchoProcessResponse.builder()
            .message(message)
            .completed(true)
            .timestamp(LocalDateTime.now())
            .build());
        context().stop(self());*/
      })
      .match(ProcessRequestCompleteMsg.class, processReqCompleteMsg -> {
        String artifact = processReqCompleteMsg.getArtifact();
        responsedItems.add(artifact);
        String message = "MasterEchoActor: Received Completed Response for: " + artifact;

        simpMessagingTemplate.convertAndSendToUser(destination,
          Topics.ECHO_TOPIC,
          EchoProcessResponse.builder()
            .message(message)
            .completed(false)
            .timestamp(LocalDateTime.now())
            .build());

        if (responsedItems.size() == requestItems.size()) {
          message = "MasterEchoActor: Received All Responses for this request.";

          simpMessagingTemplate.convertAndSendToUser(destination,
            Topics.ECHO_TOPIC,
            EchoProcessResponse.builder()
              .message(message)
              .completed(true)
              .timestamp(LocalDateTime.now())
              .build());
        }
      })
      .match(ProcessRequestFailedMsg.class, processReqFailedMsg -> {
        String artifact = processReqFailedMsg.getArtifact();
        responsedItems.add(artifact);
        String message = "MasterEchoActor: Received Failed Response for: " + artifact;

        simpMessagingTemplate.convertAndSendToUser(destination,
          Topics.ECHO_TOPIC,
          EchoProcessResponse.builder()
            .message(message)
            .completed(false)
            .timestamp(LocalDateTime.now())
            .build());

        if (responsedItems.size() == requestItems.size()) {
          message = "MasterEchoActor: Received All Responses for this request.";

          simpMessagingTemplate.convertAndSendToUser(destination,
            Topics.ECHO_TOPIC,
            EchoProcessResponse.builder()
              .message(message)
              .completed(true)
              .timestamp(LocalDateTime.now())
              .build());
        }
      })
      .matchAny((err) -> {
        log.error("An error occurred processing Master Echo Actor.");
        this.unhandled("An exception occurred.");
      })
      .build());
  }
}
