package top.fsky.crawler.domain.ddd;

import java.time.Instant;
import java.util.UUID;

/**
 * 有唯一标识
 * 没有生命周期
 * 通知型事件 / 命令型事件
 */
public interface DomainEvent<T> {
  boolean sameEventAs(T other);

  Entity<?> getEventBody();
  
  UUID getEventId();

  Instant getCreatedAt();
}
