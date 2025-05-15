package dev.malaquias.order;

import dev.malaquias.order.entity.OrderEntity;
import dev.malaquias.order.infra.dto.OrderCreatedConsumeDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        orderRepository.persist(OrderEntity.fromDTO(dto));
    }

}
