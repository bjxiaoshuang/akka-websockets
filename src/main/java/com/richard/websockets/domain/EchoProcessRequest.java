package com.richard.websockets.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.security.Principal;
import java.util.List;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonDeserialize(builder = EchoProcessRequest.EchoProcessRequestBuilder.class)
@JsonSerialize
@ToString
public class EchoProcessRequest {
  @Singular
  private final List<String> artifacts;
  private final Principal principal;

  @JsonPOJOBuilder(withPrefix = "")
  public static final class EchoProcessRequestBuilder {
  }
}
