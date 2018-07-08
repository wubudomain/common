package top.wboost.common.context.classLoader;

public class ByteClassLoader extends ClassLoader {

    public ByteClassLoader() {
    }

    public Class<?> defineClass(String name, byte[] data) {
        return defineClass(name, data, 0, data.length);
    }

}