package top.wboost.common.system.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import top.wboost.common.util.AnnotationUtil;

public class JoinPointUtil {

    public static <T extends Annotation> T getAnnotationByJoinPoint(JoinPoint joinPoint, Class<T> annotationClass) {
        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        Method domethod = methodSignature.getMethod();
        T annotation = AnnotationUtil.getAnnotation(domethod, annotationClass);
        if (annotation != null) {
            return annotation;
        }
        return null;
    }

}
