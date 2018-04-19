package top.wboost.common.base.interfaces;

public interface Resolver<K, V> {

    public V resolve(K source);

}
