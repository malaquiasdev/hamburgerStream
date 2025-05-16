package dev.malaquias.order.infra.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Integer orderCode,
        Integer customerId,
        BigDecimal totalPrice,
        List<ItemResponse> items
) {
    public record ItemResponse(
            String product,
            Integer quantity,
            BigDecimal price
    ) {}
}