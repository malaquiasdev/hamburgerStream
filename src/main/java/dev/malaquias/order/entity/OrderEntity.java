package dev.malaquias.order.entity;

import dev.malaquias.order.infra.dto.OrderCreatedConsumeDTO;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders",
        indexes = {
                @Index(name = "idx_order_code", columnList = "orderCode"),
                @Index(name = "idx_customer_id", columnList = "customerId")
        })
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private Integer orderCode;
    private Integer customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    public static OrderEntity fromDTO(OrderCreatedConsumeDTO dto) {
        OrderEntity order = new OrderEntity();
        order.setOrderCode(dto.orderCode());
        order.setCustomerId(dto.customerId());

        List<OrderItemEntity> items = dto.items().stream().map(itemDto -> {
            OrderItemEntity item = new OrderItemEntity();
            item.setProduct(itemDto.product());
            item.setQuantity(itemDto.quantity());
            item.setPrice(itemDto.price());
            item.setOrder(order);
            return item;
        }).toList();

        order.setItems(items);
        return order;
    }

    public UUID getId() {
        return id;
    }

    public Integer getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Integer orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
        if (items != null) {
            items.forEach(i -> i.setOrder(this));
        }
    }
}