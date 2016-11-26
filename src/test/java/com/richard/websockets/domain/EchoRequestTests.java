package com.richard.websockets.domain;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
public class EchoRequestTests {

  @Test
  public void testEchoRequest(){
    EchoRequest echoRequest = EchoRequest.builder().artifacts(Arrays.asList("Item-01", "Item-02")).build();
    assertThat(echoRequest).isNotNull();
    assertThat(echoRequest.getArtifacts()).isNotEmpty();
    assertThat(echoRequest.getArtifacts().size()).isEqualTo(2);
  }
}
