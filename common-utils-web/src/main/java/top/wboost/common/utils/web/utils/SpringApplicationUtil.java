package top.wboost.common.utils.web.utils;

import java.lang.annotation.Annotation;

import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

public class SpringApplicationUtil {

	public static boolean isBootApplicationContext(ContextRefreshedEvent event) {
		if (event.getApplicationContext() instanceof AnnotationConfigEmbeddedWebApplicationContext) {
			return true;
		}
		return false;
	}

	public static <A extends Annotation> Object getBeansWithAnnotations(ContextRefreshedEvent event,
			Class<A> annotationType) {
		return null;
	}

	/*
	 * Set<String> controllerInit = new HashSet<>(); String[] controllers =
	 * event.getApplicationContext().getBeanNamesForAnnotation(Controller.class)
	 * ; String[] restControllers =
	 * event.getApplicationContext().getBeanNamesForAnnotation(RestController.
	 * class); controllerInit.addAll(Arrays.asList(controllers));
	 * controllerInit.addAll(Arrays.asList(restControllers));
	 * controllerInit.forEach(name -> { Object controller =
	 * event.getApplicationContext().getBean(name); EnableBaseRestful
	 * baseRestful = AnnotationUtils.findAnnotation(controller.getClass(),
	 * EnableBaseRestful.class); });
	 */

}
