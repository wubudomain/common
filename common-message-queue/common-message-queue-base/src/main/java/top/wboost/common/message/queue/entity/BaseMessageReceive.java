package top.wboost.common.message.queue.entity;

/**
 * 基础消息返回实体类
 * @author jwSun
 * @date 2017年6月21日 下午3:38:02
 */
public interface BaseMessageReceive {

    public Object getBody();

    public void setBody(Object body);

}
