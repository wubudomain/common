package top.wboost.common.spring.boot.swagger.template;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class QueryPageTemplate {

    private PageTemplate page;

    @Data
    class PageTemplate {
        @ApiParam("页数")
        protected Integer pageNumber;
        @ApiParam("每页显示数量")
        protected Integer pageSize;
    }

}
