package top.fsky.crawler.domain.ddd;
/**
 * 具有生命周期
 * 有唯一标识
 * 通过ID判断同等性
 * 增删改查 - 持久化
 * 状态/值可变
 */
public interface Entity<T> {
    boolean sameIdentityAs(T other);
}
