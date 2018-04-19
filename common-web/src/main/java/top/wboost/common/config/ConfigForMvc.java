package top.wboost.common.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;
import top.wboost.common.system.spring.converter.FormattingConversionServiceSupportFactoryBean;

@Configuration
@AutoRootApplicationConfig
public class ConfigForMvc implements BeanFactoryPostProcessor {

    @Bean("conversionServiceSupport")
    public FormattingConversionServiceFactoryBean getConversionService() {
        FormattingConversionServiceSupportFactoryBean bean = new FormattingConversionServiceSupportFactoryBean();
        return bean;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        FormattingConversionService service = (FormattingConversionService) beanFactory
                .getBean("conversionServiceSupport");
        beanFactory.getBeansOfType(Converter.class, true, false).forEach((beanName, converter) -> {
            service.addConverter(converter);
        });
    }

}
