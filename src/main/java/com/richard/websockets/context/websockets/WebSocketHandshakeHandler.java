package com.richard.websockets.context.websockets;

import com.sun.security.auth.UserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Component("webSocketHandshakeHandler")
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

  @Override
  protected Principal determineUser(final ServerHttpRequest request,
                                    final WebSocketHandler wsHandler,
                                    final Map<String, Object> attributes) {
    Principal principal = super.determineUser(request, wsHandler, attributes);

    Principal randomPrincipal = new UserPrincipal(UUID.randomUUID().toString());
    System.out.println("Random Principal: " + randomPrincipal.getName());
    return principal != null ? principal : randomPrincipal;
  }
}
