package top.wboost.common.annotation;

public interface AnnotationFunction<T, K> {

    public K resolveAnnotation(T t);

}
