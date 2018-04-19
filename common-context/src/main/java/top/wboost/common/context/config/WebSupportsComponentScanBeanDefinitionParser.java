package top.wboost.common.context.config;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Controller;
import org.w3c.dom.Element;

import top.wboost.common.base.annotation.AutoWebApplicationConfig;

public class WebSupportsComponentScanBeanDefinitionParser extends SupportsComponentScanBeanDefinitionParser {

    protected void parseTypeFilters(Element element, ClassPathBeanDefinitionScanner scanner,
            ParserContext parserContext) {
        super.parseTypeFilters(element, scanner, parserContext);
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        scanner.addIncludeFilter(
                createTypeFilter("annotation", AutoWebApplicationConfig.class.getName(), classLoader, parserContext));
        if (Boolean.parseBoolean(element.getAttribute("use-default-IncludeFilter"))) {
            scanner.addIncludeFilter(
                    createTypeFilter("annotation", Controller.class.getName(), classLoader, parserContext));
        }
    }

}
