package dev.malaquias.order.infra.http;

import dev.malaquias.order.OrderService;
import dev.malaquias.order.infra.dto.OrderResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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

    @Path("/")
    @GET
    public Response list(
            @QueryParam("customerId") Integer customerId,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("5") int size
    ) {
        if (customerId == null) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("Query parameter 'customerId' is required.")
                    .build();
        }

        return Response
                .ok(service.findAll(customerId, page, size))
                .build();
    }
}
