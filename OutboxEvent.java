@Entity
public class OutboxEvent {
    @Id
    private UUID id;
    private String eventType;
    private String payload;
    private Instant timestamp;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
        this.timestamp = Instant.now();
    }
}

@Service
public class OrderService {
    
    @Transactional
    public void createOrder(Order order) {
        orderRepository.save(order);
        
        OutboxEvent event = new OutboxEvent("ORDER_CREATED", serialize(order));
        outboxRepository.save(event);
    }
    
    private String serialize(Order order) {
        return new ObjectMapper().writeValueAsString(order);
    }
}
