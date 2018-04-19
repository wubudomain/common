package top.wboost.common.base.interfaces;

public interface Checker<K, V> {

    public V check(K source, Object... args);

}
