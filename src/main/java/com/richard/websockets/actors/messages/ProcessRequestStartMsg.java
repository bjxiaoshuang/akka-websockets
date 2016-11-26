package com.richard.websockets.actors.messages;

import akka.actor.ActorPath;
import com.richard.websockets.domain.EchoProcessRequest;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Value
@Builder
public class ProcessRequestStartMsg implements Serializable{
  private final EchoProcessRequest echoProcessRequest;
  private final ActorPath path;
}
