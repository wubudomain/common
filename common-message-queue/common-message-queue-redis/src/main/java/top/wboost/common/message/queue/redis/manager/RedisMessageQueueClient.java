package top.wboost.common.message.queue.redis.manager;

import com.alibaba.fastjson.JSONObject;
import top.wboost.common.cache.redis.RedisUtil;
import top.wboost.common.message.queue.interfaces.MessageQueueClient;
import top.wboost.common.message.queue.redis.entity.RedisMessageReceive;
import top.wboost.common.message.queue.redis.entity.RedisMessageSend;

public class RedisMessageQueueClient implements MessageQueueClient<RedisMessageSend, RedisMessageReceive> {

    private static final String messageQueueName = "message:queue:";

    @Override
    public Object sendMessage(RedisMessageSend messageSend) {
        if (messageSend == null) {
            return null;
        }
        RedisUtil.getJedisCommands().rpush(messageQueueName, JSONObject.toJSONString(messageSend));
        return true;
    }

    @Override
    public RedisMessageReceive getMessage() {
        String message = RedisUtil.getJedisCommands().lpop(messageQueueName);
        if (message == null) {
            return null;
        }
        return JSONObject.parseObject(message, RedisMessageReceive.class);
    }

}
