package dev.malaquias.order.infra.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.malaquias.order.OrderService;
import dev.malaquias.order.infra.dto.OrderCreatedConsumeDTO;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.io.IOException;

@ApplicationScoped
public class OrderConsumer {

    @Inject
    ObjectMapper mapper;
    @Inject
    OrderService service;

    @Incoming("order-created")
    @Blocking
    public void consume(byte[] message) throws IOException {
        OrderCreatedConsumeDTO dto = mapper.readValue(message, OrderCreatedConsumeDTO.class);
        service.save(dto);
    }
}