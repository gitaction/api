package com.gitaction.api.application.event;

import com.gitaction.api.domain.ddd.DomainEvent;
import com.gitaction.api.domain.ddd.DomainEvents;
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
