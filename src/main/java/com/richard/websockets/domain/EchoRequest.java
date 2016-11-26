package com.richard.websockets.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.List;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonDeserialize(builder = EchoRequest.EchoRequestBuilder.class)
@JsonSerialize
@ToString
public class EchoRequest {
  @Singular private final List<String> artifacts;

  @JsonPOJOBuilder(withPrefix = "")
  public static final class EchoRequestBuilder {
  }
}
