package com.richard.websockets.context;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.richard.websockets.context.akka.SpringExtension;
import com.richard.websockets.util.jackson.JSR310DateTimeSerializer;
import com.richard.websockets.util.jackson.JSR310LocalDateDeserializer;
import com.richard.websockets.util.jackson.JSR310LocalDateTimeConverters;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Configuration
public class AppConfiguration {

  @Bean
  public AtomicLong actorId() {
    return new AtomicLong(0);
  }

  @Bean
  public ActorSystem actorSystem(ApplicationContext applicationContext) {
    ActorSystem system = ActorSystem.create("AkkaJavaSpring");
    // initialize the application context in the Akka Spring Extension
    SpringExtension
      .SpringExtProvider.get(system)
      .initialize(applicationContext);

    return system;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);

    JavaTimeModule module = new JavaTimeModule();
    module.addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE);
    module.addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE);
    //module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);

    module.addSerializer(LocalDateTime.class, new JSR310LocalDateTimeConverters.LocalDateTimeSerializer());
    module.addDeserializer(LocalDateTime.class, new JSR310LocalDateTimeConverters.LocalDateTimeDeserializer());
    module.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);
    module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
    module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
    objectMapper.registerModule(module);

    //Allow jackson to introspect any json annotations in the objects
    objectMapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector());
    return objectMapper;
  }
}
