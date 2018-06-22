package top.wboost.common.base.interfaces;

/**
 * 解码器
 * @className Decoder
 * @author jwSun
 * @date 2018年6月19日 下午2:15:20
 * @version 1.0.0
 * @param <K> 解码值
 * @param <V> 解码结果
 */
public interface Decoder<K, V> {

    public V decode(K val);

}
