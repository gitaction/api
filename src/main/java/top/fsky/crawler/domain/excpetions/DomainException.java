package top.fsky.crawler.domain.excpetions;

public abstract class DomainException extends RuntimeException {
    private static final long serialVersionUID = 4672877643155024497L;

    public DomainException(String message) {
        super(message);
    }
}
