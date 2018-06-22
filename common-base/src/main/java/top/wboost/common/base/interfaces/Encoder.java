package top.wboost.common.base.interfaces;

/**
 * 编码器
 * @className Encoder
 * @author jwSun
 * @date 2018年6月19日 下午2:15:46
 * @version 1.0.0
 * @param <K> 编码值
 * @param <V> 编码结果
 */
public interface Encoder<K, V> {

    public V encode(K value);

}
