package top.wboost.common.context;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;

import top.wboost.common.base.annotation.AutoConfig;
import top.wboost.common.context.generator.ConfigAnnotationBeanNameGenerator;

/**
 * 增加AutoConfig注解扫描
 * @author jwsun
 */
@Deprecated
public class AnnotationConfigEmbeddedWebApplicationContextSupport extends EmbeddedWebApplicationContextSupport {

	private final AnnotatedBeanDefinitionReader reader;

	private final ClassPathBeanDefinitionScanner scanner;

	private Class<?>[] annotatedClasses;

	private String[] basePackages;

	/**
	 * Create a new {@link AnnotationConfigEmbeddedWebApplicationContext} that
	 * needs to be populated through {@link #register} calls and then manually
	 * {@linkplain #refresh refreshed}.
	 */
	public AnnotationConfigEmbeddedWebApplicationContextSupport() {
		super();
		this.reader = new AnnotatedBeanDefinitionReader(this);
		this.scanner = createClassPathBeanDefinitionScanner();
		setBeanNameGenerator(new ConfigAnnotationBeanNameGenerator());
		scan("com.chinaoly", "top.wboost");
	}

	/**
	 * Create a new {@link AnnotationConfigEmbeddedWebApplicationContext},
	 * deriving bean definitions from the given annotated classes and
	 * automatically refreshing the context.
	 * 
	 * @param annotatedClasses
	 *            one or more annotated classes, e.g. {@code @Configuration}
	 *            classes
	 */
	public AnnotationConfigEmbeddedWebApplicationContextSupport(Class<?>... annotatedClasses) {
		this();
		register(annotatedClasses);
		refresh();
	}

	/**
	 * Create a new {@link AnnotationConfigEmbeddedWebApplicationContext},
	 * scanning for bean definitions in the given packages and automatically
	 * refreshing the context.
	 * 
	 * @param basePackages
	 *            the packages to check for annotated classes
	 */
	public AnnotationConfigEmbeddedWebApplicationContextSupport(String... basePackages) {
		this();
		scan(basePackages);
		refresh();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Delegates given environment to underlying
	 * {@link AnnotatedBeanDefinitionReader} and
	 * {@link ClassPathBeanDefinitionScanner} members.
	 */
	@Override
	public void setEnvironment(ConfigurableEnvironment environment) {
		super.setEnvironment(environment);
		this.reader.setEnvironment(environment);
		this.scanner.setEnvironment(environment);
	}

	/**
	 * Provide a custom {@link BeanNameGenerator} for use with
	 * {@link AnnotatedBeanDefinitionReader} and/or
	 * {@link ClassPathBeanDefinitionScanner} , if any.
	 * <p>
	 * Default is
	 * {@link org.springframework.context.annotation.AnnotationBeanNameGenerator}.
	 * <p>
	 * Any call to this method must occur prior to calls to
	 * {@link #register(Class...)} and/or {@link #scan(String...)}.
	 * 
	 * @param beanNameGenerator
	 *            the bean name generator
	 * @see AnnotatedBeanDefinitionReader#setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner#setBeanNameGenerator
	 */
	public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
		this.reader.setBeanNameGenerator(beanNameGenerator);
		this.scanner.setBeanNameGenerator(beanNameGenerator);
		this.getBeanFactory().registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR,
				beanNameGenerator);
	}

	/**
	 * Set the {@link ScopeMetadataResolver} to use for detected bean classes.
	 * <p>
	 * The default is an {@link AnnotationScopeMetadataResolver}.
	 * <p>
	 * Any call to this method must occur prior to calls to
	 * {@link #register(Class...)} and/or {@link #scan(String...)}.
	 * 
	 * @param scopeMetadataResolver
	 *            the scope metadata resolver
	 */
	public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
		this.reader.setScopeMetadataResolver(scopeMetadataResolver);
		this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
	}

	/**
	 * Register one or more annotated classes to be processed. Note that
	 * {@link #refresh()} must be called in order for the context to fully
	 * process the new class.
	 * <p>
	 * Calls to {@code #register} are idempotent; adding the same annotated
	 * class more than once has no additional effect.
	 * 
	 * @param annotatedClasses
	 *            one or more annotated classes, e.g. {@code @Configuration}
	 *            classes
	 * @see #scan(String...)
	 * @see #refresh()
	 */
	public final void register(Class<?>... annotatedClasses) {
		this.annotatedClasses = annotatedClasses;
		Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
	}

	/**
	 * Perform a scan within the specified base packages. Note that
	 * {@link #refresh()} must be called in order for the context to fully
	 * process the new class.
	 * 
	 * @param basePackages
	 *            the packages to check for annotated classes
	 * @see #register(Class...)
	 * @see #refresh()
	 */
	public final void scan(String... basePackages) {
		this.basePackages = basePackages;
		Assert.notEmpty(basePackages, "At least one base package must be specified");
	}

	@Override
	protected void prepareRefresh() {
		this.scanner.clearCache();
		super.prepareRefresh();
	}

	@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		super.postProcessBeanFactory(beanFactory);
		if (this.basePackages != null && this.basePackages.length > 0) {
			this.scanner.scan(this.basePackages);
		}
		if (this.annotatedClasses != null && this.annotatedClasses.length > 0) {
			this.reader.register(this.annotatedClasses);
		}
	}

	protected ClassPathBeanDefinitionScanner createClassPathBeanDefinitionScanner() {
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this);
		ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
		scanner.addIncludeFilter(createTypeFilter("annotation", AutoConfig.class.getName(), classLoader));
		scanner.setBeanNameGenerator(new ConfigAnnotationBeanNameGenerator());
		return scanner;
	}

	@SuppressWarnings("unchecked")
	protected TypeFilter createTypeFilter(String filterType, String expression, ClassLoader classLoader) {
		try {
			if ("annotation".equals(filterType)) {
				return new AnnotationTypeFilter((Class<Annotation>) classLoader.loadClass(expression));
			} else if ("assignable".equals(filterType)) {
				return new AssignableTypeFilter(classLoader.loadClass(expression));
			} else if ("aspectj".equals(filterType)) {
				return new AspectJTypeFilter(expression, classLoader);
			} else if ("regex".equals(filterType)) {
				return new RegexPatternTypeFilter(Pattern.compile(expression));
			} else if ("custom".equals(filterType)) {
				Class<?> filterClass = classLoader.loadClass(expression);
				if (!TypeFilter.class.isAssignableFrom(filterClass)) {
					throw new IllegalArgumentException(
							"Class is not assignable to [" + TypeFilter.class.getName() + "]: " + expression);
				}
				return (TypeFilter) BeanUtils.instantiateClass(filterClass);
			} else {
				throw new IllegalArgumentException("Unsupported filter type: " + filterType);
			}
		} catch (ClassNotFoundException ex) {
			throw new FatalBeanException("Type filter class not found: " + expression, ex);
		}
	}

}
