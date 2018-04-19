package top.wboost.common.message.queue.redis.manager;

import com.alibaba.fastjson.JSONObject;
import top.wboost.common.cache.redis.RedisUtil;
import top.wboost.common.message.queue.entity.BaseMessageReceive;
import top.wboost.common.message.queue.entity.BaseMessageSend;
import top.wboost.common.message.queue.interfaces.MessageQueueManager;
import top.wboost.common.message.queue.redis.entity.RedisMessageReceive;

public class RedisMessageQueueManager implements MessageQueueManager {

    private static final String messageQueueName = "message:queue:";

    @Override
    public <T extends BaseMessageSend> Object sendMessage(T messageSend) {
        if (messageSend == null) {
            return null;
        }
        RedisUtil.getJedisCommands().rpush(messageQueueName, JSONObject.toJSONString(messageSend));
        return true;
    }

    @Override
    public BaseMessageReceive getMessage() {
        return getMessageReal();
    }

    public RedisMessageReceive getMessageReal() {
        String message = RedisUtil.getJedisCommands().lpop(messageQueueName);
        if (message == null) {
            return null;
        }
        return JSONObject.parseObject(message, RedisMessageReceive.class);
    }

}
