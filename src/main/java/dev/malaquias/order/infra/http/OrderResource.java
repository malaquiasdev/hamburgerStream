package dev.malaquias.order.infra.http;

import dev.malaquias.order.OrderService;
import dev.malaquias.order.infra.dto.OrderResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

@Path("orders")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class OrderResource {

    @Inject
    OrderService service;

    @Path("/{orderCode}")
    @GET
    public OrderResponse get(Integer orderCode) {
        return service.findByCode(orderCode).orElse(null);
    }
}
