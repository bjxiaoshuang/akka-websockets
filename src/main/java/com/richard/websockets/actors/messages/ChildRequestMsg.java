package com.richard.websockets.actors.messages;

import akka.actor.ActorPath;
import com.richard.websockets.domain.EchoChildRequest;
import lombok.Builder;
import lombok.Value;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Builder
@Value
public class ChildRequestMsg {
  private final ActorPath path;
  private final EchoChildRequest childRequest;
}
