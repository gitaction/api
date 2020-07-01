package com.gitaction.api.domain.exceptions;

public abstract class DomainException extends RuntimeException {
    private static final long serialVersionUID = 4672877643155024497L;

    public DomainException(String message) {
        super(message);
    }
}
