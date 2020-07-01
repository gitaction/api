package com.gitaction.api.domain.ddd;

import java.util.List;

public interface DomainEvents {

    void publish(DomainEvent<?> event);

    default void publish(List<DomainEvent<?>> events) {
        events.forEach(this::publish);
    }
}
