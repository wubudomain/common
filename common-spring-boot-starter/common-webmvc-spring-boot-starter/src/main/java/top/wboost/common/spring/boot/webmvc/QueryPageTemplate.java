package top.wboost.common.spring.boot.webmvc;

import lombok.Data;

@Data
public class QueryPageTemplate {

    private PageTemplate page;

    @Data
    class PageTemplate {
        protected Integer pageNumber;
        protected Integer pageSize;
    }

}
