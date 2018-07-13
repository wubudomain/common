package top.wboost.common.context.classLoader;

public class ByteArrayClassLoader extends ClassLoader {

    public ByteArrayClassLoader() {
    }

    public Class<?> defineClass(String name, byte[] data) {
        return defineClass(name, data, 0, data.length);
    }

}