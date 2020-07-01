package top.fsky.crawler.application.event;

import top.fsky.crawler.domain.ddd.DomainEvent;
import top.fsky.crawler.domain.ddd.DomainEvents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DomainEventPublisher implements DomainEvents {

    private final ApplicationEventPublisher publisher;

    public DomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent<?> event) {
        publisher.publishEvent(event);
    }
}
