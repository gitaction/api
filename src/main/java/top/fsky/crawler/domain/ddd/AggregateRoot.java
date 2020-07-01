package top.fsky.crawler.domain.ddd;

/**
 * 负责执行业务规则
 * 有全局唯一标识
 * 聚合外对象只能引用该聚合，不能直接引用聚合内对象
 * 边界内对象可以执有其它聚合根引用
 * 边界内对象需要和聚合根有相同生命周期
 * 只有聚合根与持久化层直接交互
 */
public interface AggregateRoot<T> extends Entity<T> {
}
