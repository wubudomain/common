package top.wboost.common.spring.boot.swagger.template;

import io.swagger.annotations.ApiParam;
import lombok.Data;

@Data
public class ResultEntityTemplate {

    @ApiParam("返回数据")
    private Object data;
    /**返回系统提示参数(返回码,提示信息)**/
    private ReturnInfoTemplate info = new ReturnInfoTemplate();
    @ApiParam("验证")
    private Boolean validate;
    @ApiParam("状态")
    private int status;

    class ReturnInfoTemplate {
        @ApiParam("返回码")
        private Integer code;
        @ApiParam("提示信息")
        private String message;
    }

}
