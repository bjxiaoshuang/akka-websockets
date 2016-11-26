package com.richard.websockets.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonDeserialize(builder = EchoProcessResponse.EchoProcessResponseBuilder.class)
@JsonSerialize
@ToString
public class EchoProcessResponse {
  private final boolean completed;
  private final LocalDateTime timestamp;
  private final String message;
  private final String artifact;


  @JsonPOJOBuilder(withPrefix = "")
  public static final class EchoProcessResponseBuilder {
  }
}
