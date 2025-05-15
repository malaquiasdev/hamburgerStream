
package dev.malaquias.order.infra.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedConsumeDTO(
        Integer orderCode,
        Integer customerId,
        List<ItemDTO> items
) {
    public record ItemDTO(
            String product,
            Integer quantity,
            BigDecimal price
    ) {}
}
