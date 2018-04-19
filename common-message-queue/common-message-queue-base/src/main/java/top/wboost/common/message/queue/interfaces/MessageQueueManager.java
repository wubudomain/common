package top.wboost.common.message.queue.interfaces;

import top.wboost.common.message.queue.entity.BaseMessageReceive;
import top.wboost.common.message.queue.entity.BaseMessageSend;

/**
 * 消息队列管理
 * @author jwSun
 * @date 2017年6月21日 下午3:30:22
 */
public interface MessageQueueManager {

    /**
     * 发送消息
     * @author jwSun
     * @date 2017年6月21日 下午3:45:15
     * @param messageSend messageSend
     * @return
     */
    public <T extends BaseMessageSend> Object sendMessage(T messageSend);

    /**
     * 接收消息
     * @author jwSun
     * @date 2017年6月21日 下午3:42:42
     * @return
     */
    public BaseMessageReceive getMessage();

}
