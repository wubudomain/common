package top.wboost.common.netty;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public final class MarshallingCodeCFactory {

    /**
         * 创建Jboss Marshalling解码器MarshallingDecoder
         * @return MarshallingDecoder
    
         */

    public static MarshallingDecoder buildMarshallingDecoder() {

        //首先通过Marshalling工具类的精通方法获取Marshalling实例对象 参数serial标识创建的是java序列化工厂对象。

        MarshallerFactory MarshallerFactorymarshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        //创建了MarshallingConfiguration对象，配置了版本号为5

        MarshallingConfiguration MarshallingConfigurationconfiguration = new MarshallingConfiguration();

        MarshallingConfigurationconfiguration.setVersion(5);

        //根据marshallerFactory和configuration创建provider

        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(MarshallerFactorymarshallerFactory,
                MarshallingConfigurationconfiguration);

        //构建Netty的MarshallingDecoder对象，俩个参数分别为provider和单个消息序列化后的最大长度

        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024 * 1024);

        return decoder;

    }

    /**
    
         * 创建Jboss Marshalling编码器MarshallingEncoder
    
         * @return MarshallingEncoder
    
         */

    public static MarshallingEncoder buildMarshallingEncoder() {

        MarshallerFactory MarshallerFactorymarshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        MarshallingConfiguration MarshallingConfigurationconfiguration = new MarshallingConfiguration();

        MarshallingConfigurationconfiguration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(MarshallerFactorymarshallerFactory,
                MarshallingConfigurationconfiguration);

        //构建Netty的MarshallingEncoder对象，MarshallingEncoder用于实现序列化接口的POJO对象序列化为二进制数组

        MarshallingEncoder encoder = new MarshallingEncoder(provider);

        return encoder;
    }

}
