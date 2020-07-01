package top.fsky.crawler.domain.ddd;
/**
 * 起描述性作用
 * 没有唯一标识
 * 通过属性判断同等性
 * 即时创建/用完可毁
 * 不可变
 */
public interface ValueObject<T> {
    boolean sameValueAs(T other);
}
