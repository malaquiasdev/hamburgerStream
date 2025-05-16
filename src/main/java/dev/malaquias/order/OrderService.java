package dev.malaquias.order;

import dev.malaquias.order.entity.Item;
import dev.malaquias.order.entity.Order;
import dev.malaquias.order.infra.dto.OrderCreatedConsumeDTO;
import dev.malaquias.order.infra.dto.OrderResponse;
import dev.malaquias.order.infra.dto.PagedResponse;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Inject
    OrderRepository orderRepository;

    @Transactional
    public void save(OrderCreatedConsumeDTO dto) {
        var previousOrder = findByCode(dto.orderCode());
        if (previousOrder.isPresent()) {
            log.warn("skipping duplicated order — orderCode: {}", dto.orderCode());
            return;
        }
        orderRepository.persist(orderFromDTO(dto));
    }

    public Optional<OrderResponse> findByCode(Integer orderCode) {
        return orderRepository
                .find("orderCode", orderCode)
                .firstResultOptional()
                .map(this::toDTO);
    }

    public PagedResponse<OrderResponse> findAll(Integer customerId, int page, int size) {
        var query = orderRepository.find("customerId = :customerId",
                Parameters.with("customerId", customerId));
        long total = query.count();

        List<OrderResponse> data = query
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(this::toDTO)
                .toList();

        int totalPages = (int) Math.ceil((double) total / size);
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;

        return new PagedResponse<>(
                data,
                page,
                size,
                total,
                totalPages,
                nextPage,
                previousPage
        );
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

    private OrderResponse toDTO(Order order) {
        List<OrderResponse.ItemResponse> itemDTOs = order.getItems().stream()
                .map(item -> new OrderResponse.ItemResponse(
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();

        return new OrderResponse(
                order.getOrderCode(),
                order.getCustomerId(),
                order.getTotalPrice(),
                itemDTOs
        );
    }

}
