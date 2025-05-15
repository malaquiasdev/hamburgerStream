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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private Integer orderCode;
    private Integer customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    public static Order fromDTO(OrderCreatedConsumeDTO dto) {
        Order order = new Order();
        order.setOrderCode(dto.orderCode());
        order.setCustomerId(dto.customerId());

        List<Item> items = dto.items().stream().map(itemDto -> {
            Item item = new Item();
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
        if (items != null) {
            items.forEach(i -> i.setOrder(this));
        }
    }
}