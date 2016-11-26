package com.richard.websockets.domain;

import lombok.Builder;
import lombok.Value;

import java.security.Principal;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Builder
@Value
public class EchoChildRequest {
  private final String artifact;
  private final Principal principal;
}
