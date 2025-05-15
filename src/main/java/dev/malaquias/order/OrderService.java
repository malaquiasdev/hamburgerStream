package dev.malaquias.order;

import dev.malaquias.order.entity.Item;
import dev.malaquias.order.entity.Order;
import dev.malaquias.order.infra.dto.OrderCreatedConsumeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Inject
    OrderRepository orderRepository;

    @Transactional
    public void save(OrderCreatedConsumeDTO dto) {
        var previousOrder = orderRepository.find("orderCode", dto.orderCode()).firstResultOptional();
        if(previousOrder.isPresent()) {
            log.warn("skipping duplicated order — orderCode: {}", dto.orderCode());
            return;
        }
        orderRepository.persist(orderFromDTO(dto));
    }

    private Order orderFromDTO(OrderCreatedConsumeDTO dto) {
        Order order = new Order();
        order.setOrderCode(dto.orderCode());
        order.setCustomerId(dto.customerId());

        List<Item> items = dto.items()
                .stream()
                .map(itemDto -> itemFromDTO(itemDto, order))
                .toList();

        order.setItems(items);
        order.setTotalPrice(calculateTotalOrderPrice(items));
        return order;
    }

    private static BigDecimal calculateTotalOrderPrice(List<Item> items) {
        return items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private Item itemFromDTO(OrderCreatedConsumeDTO.ItemDTO itemDto, Order order) {
        Item item = new Item();
        item.setProduct(itemDto.product());
        item.setQuantity(itemDto.quantity());
        item.setPrice(itemDto.price());
        item.setOrder(order);
        return item;
    }

}
