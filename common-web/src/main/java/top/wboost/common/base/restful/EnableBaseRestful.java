
package top.wboost.common.base.restful;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启标准Restful增删改查接口
 * <pre>
 * 1.GET:     /{inspectName}/{id}   根据主键查询 
 * 2.GET:     /{inspectName}        列表查询
 * 3.DELETE:  /{inspectName}        根据主键删除
 * 4.POST:    /{inspectName}        保存数据
 * 5.PUT:     /{inspectName}/{id}   更新数据
 * </pre>
 * @className EnableBaseRestful
 * @author jwSun
 * @date 2018年4月13日 下午4:25:32
 * @version 1.0.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableBaseRestful {

    /**
     * <pre>
     *  实体名
     *  如表为 T_USER,实体类名为User,则填User.
     *  default: UserController -> User
     * </pre>
     */
    String entity() default "";

    /**
     * default: entity字段值 如User则service使用UserService.
     */
    String service() default "";

    /**
     * default: 默认使用模糊查询字段名
     */
    String[] likeFields() default {};

    /**
     * 不包含自动生成方法 若使用此,则不使用includes选择
     * 优先级 excludes > includes
     */
    AutoRequestMehthodType[] excludes() default {};

    /**
     * 包含自动生成方法,若使用此属性，则忽略未选择方法，若使用了excludes则此属性无效
     */
    AutoRequestMehthodType[] includes() default {};

}