package top.fsky.crawler.domain.excpetions;


import top.fsky.crawler.domain.ddd.Entity;

import java.util.Locale;
import java.util.UUID;

public class EntityNotFoundException extends DomainException {
    private static final long serialVersionUID = 7696145497841898823L;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public <T extends Entity> EntityNotFoundException(Class<T> entityClass, UUID id) {
        super("cannot find the " + entityClass.getSimpleName().toLowerCase(Locale.getDefault()) + " with id " + id);
    }
}
