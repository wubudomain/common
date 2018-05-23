package top.wboost.common.spring.boot.swagger.config;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import top.wboost.common.annotation.Explain;
import top.wboost.common.base.annotation.AutoWebApplicationConfig;

@AutoWebApplicationConfig
@Order(Ordered.HIGHEST_PRECEDENCE - 10)
public class ExplainOperationBuilderPlugin implements OperationBuilderPlugin {
    // private final ModelAttributeParameterExpander expander;
    // private final EnumTypeDeterminer enumTypeDeterminer;

    // @Autowired
    // private DocumentationPluginsManager pluginsManager;

    /*@Autowired
    public ExplainOperationBuilderPlugin(ModelAttributeParameterExpander expander,
            EnumTypeDeterminer enumTypeDeterminer) {
        this.expander = expander;
        this.enumTypeDeterminer = enumTypeDeterminer;
    }*/

    @Override
    public void apply(OperationContext context) {
        List<ApiOperation> list = context.findAllAnnotations(ApiOperation.class);
        if (list.size() == 0) {
            List<Explain> explainList = context.findAllAnnotations(Explain.class);
            if (explainList.size() > 0) {
                Explain explain = explainList.get(0);
                context.operationBuilder().summary(explain.value());
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

}
