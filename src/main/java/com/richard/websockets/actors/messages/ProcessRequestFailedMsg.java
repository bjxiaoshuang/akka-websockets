package com.richard.websockets.actors.messages;

import lombok.Builder;
import lombok.Value;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Value
@Builder
public class ProcessRequestFailedMsg {
  private final String artifact;
}
