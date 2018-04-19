package top.wboost.common.context.config;

import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import top.wboost.common.base.annotation.AutoRootApplicationConfig;

public class RootSupportsComponentScanBeanDefinitionParser extends SupportsComponentScanBeanDefinitionParser {

    protected void parseTypeFilters(Element element, ClassPathBeanDefinitionScanner scanner,
            ParserContext parserContext) {
        super.parseTypeFilters(element, scanner, parserContext);
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        scanner.addIncludeFilter(
                createTypeFilter("annotation", AutoRootApplicationConfig.class.getName(), classLoader, parserContext));
        if (Boolean.parseBoolean(element.getAttribute("use-default-IncludeFilter"))) {
            scanner.addIncludeFilter(
                    createTypeFilter("annotation", Service.class.getName(), classLoader, parserContext));
        }
    }

}
